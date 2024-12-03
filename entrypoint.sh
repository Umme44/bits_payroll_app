#!/bin/sh
java -Dserver.port=8585 \
     -Dspring.config.additional-location=file:./application-prod.yml \
     -Dspring.profiles.active=prod,no-liquibase \
     -jar bits-hr-payroll-0.0.1-SNAPSHOT.jar > loggers/nohup_out.log 2>&1 &

# Ensure the script keeps running
tail -f /dev/null


