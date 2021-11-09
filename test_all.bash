#!/usr/bin/env bash
#
# Sample usage:
#
#   HOST=localhost PORT=7000 ./test-em-all.bash
#
#When not using Docker
#: ${HOST=localhost}
#: ${PORT=7000}

#When using Docker
: ${HOST=localhost}
: ${PORT=8080}
function assertCurl() {

  local expectedHttpCode=$1
  local curlCmd="$2 -w \"%{http_code}\""
  local result=$(eval $curlCmd)
  local httpCode="${result:(-3)}"
  RESPONSE='' && (( ${#result} > 3 )) && RESPONSE="${result%???}"

  if [ "$httpCode" = "$expectedHttpCode" ]
  then
    if [ "$httpCode" = "200" ]
    then
      echo "Test OK (HTTP Code: $httpCode)"
    else
      echo "Test OK (HTTP Code: $httpCode, $RESPONSE)"
    fi
  else
      echo  "Test FAILED, EXPECTED HTTP Code: $expectedHttpCode, GOT: $httpCode, WILL ABORT!"
      echo  "- Failing command: $curlCmd"
      echo  "- Response Body: $RESPONSE"
      exit 1
  fi
}

function assertEqual() {

  local expected=$1
  local actual=$2

  if [ "$actual" = "$expected" ]
  then
    echo "Test OK (actual value: $actual)"
  else
    echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual, WILL ABORT"
    exit 1
  fi
}

function testUrl()
{
  url=$@
  if curl $url -ks -f -o /dev/null
  then
    echo "Ok"
    return 0
  else
    echo -n "not yet"
    return 1
  fi;
}

function setupTestdata() {
  body=\
'{"userId":1,"name":"user 1","lastName":"lastName","login":"login","password":"password","appointments":[
        {"appointmentId":1,"date":"date 1","time":"time 1"},
        {"appointmentId":2,"date":"date 2","time":"time 2"},
        {"appointmentId":3,"date":"date 3","time":"time 3"}
    ], "cars":[
        {"carId":1,"vin":"vin 1","make":"make 1","model":"model 1","year":"year 1","color":"color 1"},
        {"carId":2,"vin":"vin 2","make":"make 2","model":"model 2","year":"year 2","color":"color 2"},
        {"carId":3,"vin":"vin 3","make":"make 3","model":"model 3","year":"year 3","color":"color 3"}
    ]}'
    recreateComposite 1 "$body"

    body=\
'{"userId":113,"name":"user 113","lastName":"lastName","login":"login","password":"password", "cars":[
    {"carId":1,"vin":"vin 1","make":"make 1","model":"model 1","year":"year 1","color":"color 1"},
    {"carId":2,"vin":"vin 2","make":"make 2","model":"model 2","year":"year 2","color":"color 2"},
    {"carId":3,"vin":"vin 3","make":"make 3","model":"model 3","year":"year 3","color":"color 3"}
]}'
    recreateComposite 113 "$body"

    body=\
'{"userId":213,"name":"user 213","lastName":"lastName","login":"login","password":"password", "appointments":[
       {"appointmentId":4,"date":"date 1","time":"time 1"},
       {"appointmentId":5,"date":"date 2","time":"time 2"},
       {"appointmentId":6,"date":"date 3","time":"time 3"}
]}'
    recreateComposite 213 "$body"
}

#Expect response 200
function recreateComposite() {
    local userId=$1
    local composite=$2
    assertCurl 200 "curl -X DELETE http://$HOST:$PORT/user-composite/${userId} -s"
    curl -X POST http://$HOST:$PORT/user-composite -H "Content-Type:
    application/json" --data "$composite"
}

function waitForService()
{
  url=$@
  echo -n "Wait for: $url... "
    n=0
    until testUrl $url
    do
    n=$((n + 1))
    if [[ $n == 100 ]]
    then
    echo " Give up"
    exit 1
    else
    sleep 6
    echo -n ", retry #$n "
    fi
  done
}

set -e

echo "HOST=${HOST}"
echo "PORT=${PORT}"

if [[ $@ == *"start"* ]]
then
echo "Restarting the test
  environment..."
echo "$ docker-compose down"
docker-compose down
echo "$ docker-compose up -d"
docker-compose up -d
fi

#waitForService http://$HOST:${PORT}/user-composite/1

waitForService curl -X DELETE http://$HOST:$PORT/user-composite/13

setupTestdata

# Verify that a normal request works, expect three appointments and three cars
assertCurl 200 "curl http://$HOST:$PORT/user-composite/1 -s"
assertEqual 1 $(echo $RESPONSE | jq .userId)
assertEqual 3 $(echo $RESPONSE | jq ".appointments | length")
assertEqual 3 $(echo $RESPONSE | jq ".cars | length")
# Verify that a 404 (Not Found) error is returned for a non existing userId (13)
assertCurl 404 "curl http://$HOST:$PORT/user-composite/13 -s"

# Verify that no appointments are returned for userId 113
assertCurl 200 "curl http://$HOST:$PORT/user-composite/113 -s"
assertEqual 113 $(echo $RESPONSE | jq .userId)
assertEqual 0 $(echo $RESPONSE | jq ".appointments | length")
assertEqual 3 $(echo $RESPONSE | jq ".cars | length")

# Verify that no cars are returned for userId 213
assertCurl 200 "curl http://$HOST:$PORT/user-composite/213 -s"
assertEqual 213 $(echo $RESPONSE | jq .userId)
assertEqual 3 $(echo $RESPONSE | jq ".appointments | length")
assertEqual 0 $(echo $RESPONSE | jq ".cars | length")

# Verify that a 422 (Unprocessable Entity) error is returned for a userId that is out of range (-1)
assertCurl 422 "curl http://$HOST:$PORT/user-composite/-1 -s"
assertEqual "\"Invalid userId: -1\"" "$(echo $RESPONSE | jq .message)"

# Verify that a 400 (Bad Request) error error is returned for a userId that is not a number, i.e. invalid format
assertCurl 400 "curl http://$HOST:$PORT/user-composite/invalidProductId -s"
assertEqual "\"Type mismatch.\"" "$(echo $RESPONSE | jq .message)"

# 400 Bad Request -> Reserved Exception
#assertCurl 500 "curl http://$HOST:$PORT/user-composite/8 -s"
#assertEqual "\"Access denied. Private user found for Id: 8\"" "$(echo $RESPONSE | jq .message)"


if [[ $@ == *"stop"* ]]
then
  echo "We are done, stopping the test environment..."
  echo "$ docker-compose down"
  docker-compose down
fi