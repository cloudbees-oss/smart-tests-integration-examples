package com.launchable.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility to discover test methods and their @Tag annotations.
 * 
 * This demonstrates how to programmatically identify which tests
 * will be excluded by Maven's <excludedGroups> configuration.
 * 
 * Usage:
 *   mvn compile exec:java -Dexec.mainClass="com.launchable.demo.TestDiscoveryUtil" \
 *       -Dexec.args="IntegrationTest,SmokeTest"
 */
public class TestDiscoveryUtil {
    
    private static class TestInfo {
        String className;
        String methodName;
        Set<String> tags = new HashSet<>();
        
        String getFullName() {
            return className + "#" + methodName;
        }
        
        boolean hasAnyTag(Set<String> excludedTags) {
            return !Collections.disjoint(tags, excludedTags);
        }
    }
    
    public static void main(String[] args) {
        String excludedGroupsArg = args.length > 0 ? args[0] : "IntegrationTest,SmokeTest";
        Set<String> excludedGroups = Arrays.stream(excludedGroupsArg.split(","))
            .map(String::trim)
            .collect(Collectors.toSet());
        
        System.out.println("=".repeat(70));
        System.out.println("Test Discovery Utility");
        System.out.println("=".repeat(70));
        System.out.println();
        System.out.println("Excluded Groups: " + excludedGroups);
        System.out.println();
        
        try {
            List<TestInfo> allTests = discoverTests();
            
            List<TestInfo> includedTests = allTests.stream()
                .filter(test -> !test.hasAnyTag(excludedGroups))
                .collect(Collectors.toList());
                
            List<TestInfo> excludedTests = allTests.stream()
                .filter(test -> test.hasAnyTag(excludedGroups))
                .collect(Collectors.toList());
            
            System.out.println("TESTS THAT WILL RUN (" + includedTests.size() + " tests)");
            System.out.println("-".repeat(70));
            for (TestInfo test : includedTests) {
                String tagsStr = test.tags.isEmpty() ? "no tags" : "tags: " + test.tags;
                System.out.println("  ✓ " + test.getFullName() + " (" + tagsStr + ")");
            }
            
            System.out.println();
            System.out.println("TESTS THAT WILL BE EXCLUDED (" + excludedTests.size() + " tests)");
            System.out.println("-".repeat(70));
            for (TestInfo test : excludedTests) {
                System.out.println("  ✗ " + test.getFullName() + " (tags: " + test.tags + ")");
            }
            
            System.out.println();
            System.out.println("=".repeat(70));
            System.out.println("SUMMARY");
            System.out.println("=".repeat(70));
            System.out.println("Total Tests:        " + allTests.size());
            System.out.println("Tests to Run:       " + includedTests.size());
            System.out.println("Tests to Exclude:   " + excludedTests.size());
            System.out.println();
            
            // Generate JSON output
            generateJsonOutput(includedTests, excludedTests, excludedGroups);
            
        } catch (IOException e) {
            System.err.println("Error discovering tests: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static List<TestInfo> discoverTests() throws IOException {
        List<TestInfo> tests = new ArrayList<>();
        
        Path testSourceRoot = Paths.get("src/test/java");
        if (!Files.exists(testSourceRoot)) {
            System.err.println("Test source directory not found: " + testSourceRoot);
            return tests;
        }
        
        try (Stream<Path> paths = Files.walk(testSourceRoot)) {
            List<Path> testFiles = paths
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith("Test.java") || p.toString().endsWith("Tests.java"))
                .collect(Collectors.toList());
            
            for (Path file : testFiles) {
                tests.addAll(parseTestFile(file));
            }
        }
        
        return tests;
    }
    
    private static List<TestInfo> parseTestFile(Path file) throws IOException {
        List<TestInfo> tests = new ArrayList<>();
        List<String> lines = Files.readAllLines(file);
        
        // Extract package and class name
        String packageName = "";
        String className = "";
        
        for (String line : lines) {
            if (line.trim().startsWith("package ")) {
                packageName = line.trim()
                    .substring(8)
                    .replace(";", "")
                    .trim();
            }
        }
        
        className = file.getFileName().toString().replace(".java", "");
        String fullClassName = packageName.isEmpty() ? className : packageName + "." + className;
        
        // Extract class-level tags
        Set<String> classTags = new HashSet<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.startsWith("@Tag(")) {
                String tag = extractTag(line);
                if (tag != null) {
                    classTags.add(tag);
                }
            }
            if (line.startsWith("public class ") || line.startsWith("class ")) {
                break; // Found class declaration
            }
        }
        
        // Extract test methods and their tags
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            
            if (line.equals("@Test") || line.startsWith("@Test(")) {
                // Look ahead for method name
                Set<String> methodTags = new HashSet<>(classTags);
                
                // Look backwards for method-level tags
                for (int j = i - 1; j >= 0 && j > i - 10; j--) {
                    String prevLine = lines.get(j).trim();
                    if (prevLine.startsWith("@Tag(")) {
                        String tag = extractTag(prevLine);
                        if (tag != null) {
                            methodTags.add(tag);
                        }
                    }
                    if (prevLine.isEmpty() || prevLine.startsWith("//") || prevLine.startsWith("/*")) {
                        continue;
                    }
                    if (!prevLine.startsWith("@")) {
                        break;
                    }
                }
                
                // Find method name
                for (int j = i + 1; j < lines.size() && j < i + 5; j++) {
                    String nextLine = lines.get(j).trim();
                    if (nextLine.contains("void ") && nextLine.contains("(")) {
                        String methodName = extractMethodName(nextLine);
                        if (methodName != null) {
                            TestInfo test = new TestInfo();
                            test.className = fullClassName;
                            test.methodName = methodName;
                            test.tags = methodTags;
                            tests.add(test);
                            break;
                        }
                    }
                }
            }
        }
        
        return tests;
    }
    
    private static String extractTag(String line) {
        // Extract tag from @Tag("TagName")
        int start = line.indexOf("\"");
        int end = line.lastIndexOf("\"");
        if (start != -1 && end != -1 && start < end) {
            return line.substring(start + 1, end);
        }
        return null;
    }
    
    private static String extractMethodName(String line) {
        // Extract method name from "public void methodName()"
        int voidIndex = line.indexOf("void ");
        int parenIndex = line.indexOf("(");
        if (voidIndex != -1 && parenIndex != -1 && voidIndex < parenIndex) {
            return line.substring(voidIndex + 5, parenIndex).trim();
        }
        return null;
    }
    
    private static void generateJsonOutput(List<TestInfo> includedTests, 
                                           List<TestInfo> excludedTests,
                                           Set<String> excludedGroups) {
        try {
            Path outputPath = Paths.get("target/test-discovery.json");
            Files.createDirectories(outputPath.getParent());
            
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"exclusionRules\": {\n");
            json.append("    \"excludedGroups\": \"").append(String.join(",", excludedGroups)).append("\"\n");
            json.append("  },\n");
            json.append("  \"testSummary\": {\n");
            json.append("    \"totalTests\": ").append(includedTests.size() + excludedTests.size()).append(",\n");
            json.append("    \"includedTests\": ").append(includedTests.size()).append(",\n");
            json.append("    \"excludedTests\": ").append(excludedTests.size()).append("\n");
            json.append("  },\n");
            json.append("  \"excludedTestList\": [\n");
            
            for (int i = 0; i < excludedTests.size(); i++) {
                TestInfo test = excludedTests.get(i);
                json.append("    {\"test\": \"").append(test.getFullName()).append("\", ");
                json.append("\"excludedBy\": \"").append(String.join(",", test.tags)).append("\"}");
                if (i < excludedTests.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            
            json.append("  ],\n");
            json.append("  \"includedTestList\": [\n");
            
            for (int i = 0; i < includedTests.size(); i++) {
                TestInfo test = includedTests.get(i);
                json.append("    {\"test\": \"").append(test.getFullName()).append("\"}");
                if (i < includedTests.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            
            json.append("  ]\n");
            json.append("}\n");
            
            Files.write(outputPath, json.toString().getBytes());
            System.out.println("✓ JSON output written to: " + outputPath);
            
        } catch (IOException e) {
            System.err.println("Error writing JSON output: " + e.getMessage());
        }
    }
}
