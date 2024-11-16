#!/bin/bash

url=http://localhost:4221

function respond_with_200() {
  printf 'Running test for Stage #IA4 (Respond with 200)\n'
  status_code=$(curl --write-out "%{http_code}\n" --silent --output /dev/null $url)
  if [[ "$status_code" -ne 200 ]] ; then
    printf 'Expected status code 200, received %s\nTest Failed' "$status_code"
  else
    printf 'Received response with status code 200\nTest Passed'
  fi
}

function extract_url_path() {
  printf 'Running test for Stage #IH0 (Extract URL path)\n'
  status_code=$(curl --write-out "%{http_code}\n" --silent --output /dev/null $url/apple)
  if [[ "$status_code" -ne 404 ]] ; then
    printf 'Expected status code 404, received %s\nTest Failed' "$status_code"
  else
    printf 'Received response with status code 404\nTest Passed'
  fi
}

function test() {
  respond_with_200
  printf '\n\n'
  extract_url_path
}

if [ $# -eq 0 ]; then
  test
fi

$1