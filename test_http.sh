#!/bin/bash

url=http://localhost:4221

function respond_with_200() {
  printf 'Running test for Stage #IA4 (Respond with 200)\n'
  status_code=$(curl --write-out "%{http_code}\n" --silent --output /dev/null $url)
  if [[ "$status_code" -ne 200 ]] ; then
    printf 'Expected status code 200, got %s\nTest Failed' "$status_code"
  else
    printf 'Received response with status code 200\nTest Passed'
  fi
}

function extract_url_path() {
  printf 'Running test for Stage #IH0 (Extract URL path)\n'
  status_code=$(curl --write-out "%{http_code}\n" --silent --output /dev/null $url/apple)
  if [[ "$status_code" -ne 404 ]] ; then
    printf 'Expected status code 404, got %s\nTest Failed' "$status_code"
  else
    printf 'Received response with status code 404\nTest Passed'
  fi
}

function respond_with_body() {
  printf 'Running test for Stage #CN2 (Respond with body)\n'
  response=$(curl -sIXGET $url/echo/apple)
  status_code=$(echo "$response" | grep -i HTTP/1.1 | awk '{print $2}')
  if [[ "$status_code" -ne 200 ]] ; then
    printf 'Expected status_code 200, got %s\nTest Failed' "$status_code"
  else
    printf 'Received response with status code 200\n'
    content_type=$(echo "$response" | grep -i content-type: | sed 's/^.*: //')
    if [[ -z "$content_type" ]] ; then
      printf 'Expected Content-Type header in response\nTest Failed'
      exit 1
    elif [[ ! $content_type =~ text/plain ]]; then
      printf 'Expected Content-Type header value to be text/plain, got %s\nTest Failed' "$content_type"
      exit 1
    else
      printf 'Content-Type: text/plain header is present\n'
    fi
    content_length=$(echo "$response" | grep -i content-length: | sed 's/^.*: //')
    if [[ -z "$content_length" ]] ; then
      printf 'Expected Content-Length header in response\nTest Failed'
      exit 1
    elif [[ ! $content_length =~ 5 ]]; then
      printf 'Expected Content-Length header value to be 5, got %s\nTest Failed' "$content_length"
      exit 1
    else
      printf 'Content-Length: 5 header is present\n'
    fi
    printf 'Test Passed'
  fi
}

function test() {
  respond_with_200
  printf '\n\n'
  extract_url_path
  printf '\n\n'
  respond_with_body
}

if [ $# -eq 0 ]; then
  test
fi

$1