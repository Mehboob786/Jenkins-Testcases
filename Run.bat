@Echo local test execution
    Pushd "%~dp0"
    mvn clean test
    popd