#!/bin/bash

url=http://localhost:4221

function respond_with_200() {
  echo 'Running test for Stage #IA4 (Respond with 200)'
  status_code=$(curl --write-out "%{http_code}\n" --silent --output /dev/null $url)
  if [[ "$status_code" -ne 200 ]] ; then
    printf 'Expected status code 200, received %s\nTest Failed' "$status_code"
  else
    printf 'Received response with status code 200\nTest Passed'
  fi
}

respond_with_200