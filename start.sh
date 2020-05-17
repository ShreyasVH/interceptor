#!/usr/bin/env bash

 set -o allexport
 source .env;
 set +o allexport

activator -jvm-debug "8070" -Dhttps.port="10070" "run 9070";