#!/bin/bash
rm -rf mysql1_data
rm -rf mysql2_data
rm -rf mysql3_data
docker-compose up --force-recreate