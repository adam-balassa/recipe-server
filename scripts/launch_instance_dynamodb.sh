#!/bin/bash
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
sudo apt-get update

sudo apt-get install -y docker-ce
sudo apt-get install -y ruby

wget https://aws-codedeploy-eu-central-1.s3.eu-central-1.amazonaws.com/latest/install
chmod +x ./install
sudo ./install auto > /tmp/logfile
sudo service codedeploy-agent start

aws ecr get-login-password --region eu-central-1 | sudo docker login --username AWS --password-stdin 445415027084.dkr.ecr.eu-central-1.amazonaws.com
sudo docker pull 445415027084.dkr.ecr.eu-central-1.amazonaws.com/recipe-server:2.0.0
sudo docker stop $(sudo docker ps -a -q)
sudo docker run -d -p 80:8080 445415027084.dkr.ecr.eu-central-1.amazonaws.com/recipe-server:2.0.0 -g 'daemon off;'