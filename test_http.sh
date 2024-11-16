#!/bin/bash

url=http://localhost:4221

function respond_with_200() {
  printf 'Running test for Stage #IA4 (Respond with 200)\n'
  status_code=$(curl --write-out "%{http_code}\n" --silent --output /dev/null $url)
  if [[ "$status_code" -ne 200 ]] ; then
    printf 'Expected status code 200, got %s\nTest Failed' "$status_code"
    exit 1
  else
    printf 'Received response with status code 200\nTest Passed\n'
  fi
}

function extract_url_path() {
  printf 'Running test for Stage #IH0 (Extract URL path)\n'
  status_code=$(curl --write-out "%{http_code}\n" --silent --output /dev/null $url/apple)
  if [[ "$status_code" -ne 404 ]] ; then
    printf 'Expected status code 404, got %s\nTest Failed' "$status_code"
    exit 1
  else
    printf 'Received response with status code 404\nTest Passed\n'
  fi
}

function respond_with_body() {
  printf 'Running test for Stage #CN2 (Respond with body)\n'
  response=$(curl -siXGET $url/echo/apple)
  status_code=$(echo "$response" | grep -i HTTP/1.1 | awk '{print $2}')
  if [[ "$status_code" -ne 200 ]] ; then
    printf 'Expected status_code 200, got %s\nTest Failed' "$status_code"
    exit 1
  fi
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
  body=$(echo "$response" | tail -n 1)
  if [[ -z "$body" ]] ; then
    printf 'Expected body in response\nTest Failed'
    exit 1
  elif [[ $body != apple ]] ; then
    printf 'Expected body apple, got %s\nTest Failed' "$body"
    exit 1
  else
    printf 'Body apple is present\n'
  fi
  printf 'Test Passed\n'
}

function read_header() {
  printf 'Running test for Stage #FS3 (Read header)\n'
  response=$(curl -siXGET $url/user-agent -H "User-Agent: fruit/apple")
  status_code=$(echo "$response" | grep -i HTTP/1.1 | awk '{print $2}')
  if [[ "$status_code" -ne 200 ]] ; then
    printf 'Expected status_code 200, got %s\nTest Failed' "$status_code"
    exit 1
  fi
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
  elif [[ ! $content_length =~ 11 ]]; then
    printf 'Expected Content-Length header value to be 11, got %s\nTest Failed' "$content_length"
    exit 1
  else
    printf 'Content-Length: 9 header is present\n'
  fi
  body=$(echo "$response" | tail -n 1)
  if [[ -z "$body" ]] ; then
    printf 'Expected body in response\nTest Failed'
    exit 1
  elif [[ $body != fruit/apple ]] ; then
    printf 'Expected body fruit/apple, got %s\nTest Failed' "$body"
    exit 1
  else
    printf 'Body fruit/apple is present\n'
  fi
  printf 'Test Passed\n'
}

function concurrent_connections() {
  printf 'Running test for Stage #EJ5 (Concurrent connections)\n'
  (sleep 3 && respond_with_200) &
  (sleep 3 && respond_with_200) &
  (sleep 3 && respond_with_200) &
  wait
}

function return_existent_file() {
  printf 'Running test for Stage #AP6 (Return a file) for an existent file\n'
  content="apples, oranges, kiwi"
  echo -n "$content" > /tmp/fruit_salad
  response=$(curl -siXGET $url/files/fruit_salad)
  status_code=$(echo "$response" | grep -i HTTP/1.1 | awk '{print $2}')
  if [[ "$status_code" -ne 200 ]] ; then
    printf 'Expected status_code 200, got %s\nTest Failed' "$status_code"
    exit 1
  fi
  printf 'Received response with status code 200\n'
  content_type=$(echo "$response" | grep -i content-type: | sed 's/^.*: //')
  if [[ -z "$content_type" ]] ; then
    printf 'Expected Content-Type header in response\nTest Failed'
    exit 1
  elif [[ ! $content_type =~ application/octet-stream ]]; then
    printf 'Expected Content-Type header value to be application/octet-stream, got %s\nTest Failed' "$content_type"
    exit 1
  else
    printf 'Content-Type: application/octet-stream header is present\n'
  fi
  content_length=$(echo "$response" | grep -i content-length: | sed 's/^.*: //')
  if [[ -z "$content_length" ]] ; then
    printf 'Expected Content-Length header in response\nTest Failed'
    exit 1
  elif [[ ! $content_length =~ 21 ]]; then
    printf 'Expected Content-Length header value to be 21, got %s\nTest Failed' "$content_length"
    exit 1
  else
    printf 'Content-Length: 21 header is present\n'
  fi
  body=$(echo "$response" | tail -n 1)
  if [[ -z "$body" ]] ; then
    printf 'Expected body in response\nTest Failed'
    exit 1
  elif [[ $body != "$content" ]] ; then
    printf 'Expected %s, got %s\nTest Failed' "$content" "$body"
    exit 1
  else
    printf 'Body %s is present\n' "$content"
  fi
  printf 'Test Passed\n'
  rm -f /tmp/fruit_salad
}

function return_non_existent_file() {
  printf 'Running test for Stage #AP6 (Return a file) for a non existent file\n'
  status_code=$(curl --write-out "%{http_code}\n" --silent --output /dev/null $url/files/fruit_salad)
  if [[ "$status_code" -ne 404 ]] ; then
    printf 'Expected status code 404, got %s\nTest Failed' "$status_code"
    exit 1
  else
    printf 'Received response with status code 404\nTest Passed\n'
  fi
}

function return_file() {
  return_existent_file
  return_non_existent_file
}

function test() {
  respond_with_200
  extract_url_path
  respond_with_body
  read_header
  return_existent_file
  return_non_existent_file
}

if [ $# -eq 0 ]; then
  test
fi

$1