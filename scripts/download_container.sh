#!/bin/bash
aws ecr get-login-password --region eu-central-1 | sudo docker login --username AWS --password-stdin 445415027084.dkr.ecr.eu-central-1.amazonaws.com
sudo docker pull 445415027084.dkr.ecr.eu-central-1.amazonaws.com/recipe-server:latest