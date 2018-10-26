#!/bin/bash
echo "List applications, should be empty:"
bash applications.sh
echo ""
echo "Request application, should not be found:"
bash application.sh
echo ""
echo "Request test suites, should not be found:"
bash test-suites.sh
echo ""
echo "Request test suite, should not be found:"
bash test-suite.sh

echo ""
echo "Create application, should work:"
bash create-application.sh
echo ""
echo "List applications, should contain one element:"
bash applications.sh
echo ""
echo "Request application, should be found and contain test suites:"
bash application.sh
echo ""
echo "Request test suites, should be found and empty:"
bash test-suites.sh
echo ""
echo "Request test suite, should not be found:"
bash test-suite.sh

echo ""
echo "Create test suite, should work:"
bash create-test-suite.sh
echo ""
echo "Request test suites, should be found and contain one test suite:"
bash test-suites.sh
echo ""
echo "Request test suite, should be found:"
bash test-suite.sh

echo ""
echo "Delete test suite, should work:"
bash delete-test-suite.sh
echo ""
echo "Request test suite, should not be found:"
bash test-suite.sh

echo ""
echo "Delete application suite, should work:"
bash delete-application.sh
echo ""
echo "Request test suite, should not be found:"
bash application.sh