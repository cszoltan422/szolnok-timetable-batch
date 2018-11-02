#!/usr/bin/env bash
java -version
source ~/.profile
echo $DB_UPDATER_HOME
cd ${DB_UPDATER_HOME}
mvn spring-boot:run -Dspring.profiles.active=$1 -Dtimetable.resource.selected-buses=$2
