#!/usr/bin/env bash

killall java;
find . -name \*.jar -exec cp {} ~/manufacturing-erp \;
cd ~/manufacturing-erp;
rm *.java;
mv application-0.0.1-SNAPSHOT.jar application.jar;
mv service-0.0.1-SNAPSHOT.jar service.jar;
./start