#!/bin/bash

set +e 

launchable verify
carton exec prove -Ilib --formatter TAP::Formatter::JUnit -r -j4 --verbose --timer t > junit_output.xml
cat junit_output.xml
launchable record tests --build "$GITHUB_RUN_ID" prove junit_output.xml
