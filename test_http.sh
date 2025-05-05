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

function read_request_body() {
  printf 'Running test for Stage #QV8 (Read request body)\n'
  content="apples, oranges, kiwi"
  response=$(curl -siXPOST -H "Content-Type: application/octet-stream" --data "$content" $url/files/fruit_salad)
  status_code=$(echo "$response" | grep -i HTTP/1.1 | awk '{print $2}')
  if [[ "$status_code" -ne 201 ]] ; then
    printf 'Expected status_code 201, got %s\nTest Failed' "$status_code"
    exit 1
  fi
  if [ ! -f /tmp/fruit_salad ]; then
    printf 'Expected file /tmp/fruit_salad, but file not found\nTest Failed'
    exit 1
  fi
  file_content=$(cat /tmp/fruit_salad)
  if [ "$content" != "$file_content" ] ; then
    printf 'Expected contents %s, got %s\nTest Failed' "$content" "$file_content"
    exit 1
  fi
  rm -f /tmp/fruit_salad
  printf 'Found file /tmp/fruit_salad with content %s\nTest Passed\n' "$content"
}

function compression_headers() {
  printf 'Running test for Stage #DF4 (HTTP Compression - Compression headers)\n'
  response=$(curl -siXGET --compressed -H "Accept-Encoding: gzip" $url/echo/apple)
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
  content_encoding=$(echo "$response" | grep -i content-encoding: | sed 's/^.*: //')
  if [[ -z "$content_encoding" ]] ; then
    printf 'Expected Content-Encoding header in response\nTest Failed'
    exit 1
  elif [[ ! $content_encoding =~ gzip ]]; then
    printf 'Expected Content-Encoding header value to be gzip, got %s\nTest Failed' "$content_encoding"
    exit 1
  else
    printf 'Content-Encoding: gzip header is present\n'
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

function multiple_compression_schemes() {
  printf 'Running test for Stage #DF4 (HTTP Compression - Multiple compression schemes)\n'
  response=$(curl -siXGET --compressed -H "Accept-Encoding: invalid-encoding, gzip, another-invalid-encoding" $url/echo/apple)
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
  content_encoding=$(echo "$response" | grep -i content-encoding: | sed 's/^.*: //')
  if [[ -z "$content_encoding" ]] ; then
    printf 'Expected Content-Encoding header in response\nTest Failed'
    exit 1
  elif [[ ! $content_encoding =~ gzip ]]; then
    printf 'Expected Content-Encoding header value to be gzip, got %s\nTest Failed' "$content_encoding"
    exit 1
  else
    printf 'Content-Encoding: gzip header is present\n'
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

function gzip_compression() {
  printf 'Running test for Stage #CR8 (HTTP Compression - Gzip compression)\n'
  response=$(curl -siXGET --compressed -H "Accept-Encoding: gzip" $url/echo/apple)
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
  content_encoding=$(echo "$response" | grep -i content-encoding: | sed 's/^.*: //')
  if [[ -z "$content_encoding" ]] ; then
    printf 'Expected Content-Encoding header in response\nTest Failed'
    exit 1
  elif [[ ! $content_encoding =~ gzip ]]; then
    printf 'Expected Content-Encoding header value to be gzip, got %s\nTest Failed' "$content_encoding"
    exit 1
  else
    printf 'Content-Encoding: gzip header is present\n'
  fi
  body=$(echo "$response" | tail -n 1)
  if [[ -z "$body" ]] ; then
    printf 'Expected body in response\nTest Failed'
    exit 1
  elif [[ $body != "apple" ]] ; then
    printf 'Expected body apple, got %s\nTest Failed' "$body"
    exit 1
  else
    printf 'Body apple is present\n'
  fi
  printf 'Test Passed\n'
}

function persistent_connection() {
  printf 'Running test for Stage #AG9 (Persistent connection)\n'
  response=$(curl -siXGET --http1.1 -v $url/echo/apple $url/echo/pear)
  if [[ $response == *"apple"* && $response == *"pear"* ]]; then
    printf 'Test Passed\n'
  else
    printf 'Expected to receive 1 body with apple and 1 body with pear, got %s\nTest Failed' "$response"
    exit 1
  fi
}

function multiple_persistent_connections() {
  printf 'Running test for Stage #UL1 (Multiple persistent connections)\n'
  (sleep 3 && persistent_connection) &
  (sleep 3 && persistent_connection) &
  (sleep 3 && persistent_connection) &
  wait
}

function connection_closure() {
  printf 'Running test for Stage #KH7 (Connection closure)\n'
  response=$(curl -siXGET --http1.1 -v $url/echo/apple --next $url/user-agent -H "Connection: close" -H "User-Agent: banana/strawberry")
  if [[ $response == *"apple"* && $response == *"banana/strawberry"* ]]; then
    printf 'Test Passed\n'
  else
    printf 'Expected to receive 1 body with apple and 1 body with banana/strawberry, got %s\nTest Failed' "$response"
    exit 1
  fi
}

function test() {
  respond_with_200
  printf '\n'
  extract_url_path
  printf '\n'
  respond_with_body
  printf '\n'
  read_header
  printf '\n'
  return_existent_file
  printf '\n'
  return_non_existent_file
  printf '\n'
  read_request_body
  printf '\n'
  compression_headers
  printf '\n'
  multiple_compression_schemes
  printf '\n'
  gzip_compression
  printf '\n'
  persistent_connection
  printf '\n'
  multiple_persistent_connections
  printf '\n'
  connection_closure
}

if [ $# -eq 0 ]; then
  test
fi

$1