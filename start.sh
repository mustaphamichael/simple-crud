#!/bin/bash

sbt docker:stage docker:publishLocal
docker-compose up --build -d