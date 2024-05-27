#!/bin/bash
cd /home/ubuntu/app
aws s3 cp s3://my-bucket-server/skillbridge-0.0.1-SNAPSHOT.jar .
java -jar skillbridge-0.0.1-SNAPSHOT.jar