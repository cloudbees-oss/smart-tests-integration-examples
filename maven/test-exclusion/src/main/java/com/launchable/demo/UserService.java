package com.launchable.demo;

/**
 * User service class that would typically interact with a database.
 * Used for integration test demonstrations.
 */
public class UserService {
    
    public String getUserById(String userId) {
        // In real implementation, this would query a database
        return "User_" + userId;
    }
    
    public boolean saveUser(String userId, String name) {
        // In real implementation, this would save to database
        return true;
    }
    
    public boolean deleteUser(String userId) {
        // In real implementation, this would delete from database
        return true;
    }
}
