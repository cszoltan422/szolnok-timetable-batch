#!/bin/bash

# save the file as <git_directory>/.git/hooks/pre-commit

clear
echo "-----------------------------------------------------"
echo "                 Running Maven build                 "
# retrieving current working directory
CWD=`pwd`
MAIN_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# go to main project dir
cd "$MAIN_DIR"/../../ || exit
echo "${MAIN_DIR}"
# running maven clean test
mvn clean verify
if [ $? -ne 0 ]; then
  "Error while building the code"
  # go back to current working dir
  # shellcheck disable=SC2164
  cd "$CWD"
  exit 1
fi
# go back to current working dir
cd "$CWD" || exit
echo "-----------------------------------------------------"