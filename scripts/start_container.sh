#!/bin/bash
sudo docker run -d -p 80:8080 445415027084.dkr.ecr.eu-central-1.amazonaws.com/recipe-server:latest -g 'daemon off;'