#!/bin/bash
a=$(sudo docker ps -q)
if [ "$a" ]; then sudo docker kill "$a"; fi
