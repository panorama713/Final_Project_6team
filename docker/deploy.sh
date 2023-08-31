#!/bin/bash

IS_GREEN=$(docker ps | grep web_green)

if [ -z "$IS_GREEN" ]; then # blue라면

  echo "### BLUE => GREEN ###"

  echo "1. get green image"
  docker-compose -f /home/ubuntu/docker/docker-compose-app.yml pull web_green # green으로 이미지를 내려받습니다.

  echo "2. green container up"
  docker-compose -f /home/ubuntu/docker/docker-compose-app.yml up -d web_green # green 컨테이너 실행
  while [ 1 = 1 ]; do
    echo "3. green health check..."
    sleep 3

    REQUEST=$(curl http://web_green:8082) # green으로 request
    if [ -n "$REQUEST" ]; then            # 서비스 가능하면 health check 중지
      echo "health check success"
      break
    fi
  done

  echo "4. reload nginx"
  sudo cp /etc/nginx/conf.d/app/service-url-green.inc /etc/nginx/conf.d/app/service-url.inc
  sudo nginx -s reload

  echo "5. blue container down"
  docker-compose -f /home/ubuntu/docker/docker-compose-app.yml stop web_blue
else
  echo "### GREEN => BLUE ###"

  echo "1. get blue image"
  docker-compose -f /home/ubuntu/docker/docker-compose-app.yml pull web_blue

  echo "2. blue container up"
  docker-compose -f /home/ubuntu/docker/docker-compose-app.yml up -d web_blue

  while [ 1 = 1 ]; do
    echo "3. blue health check..."
    sleep 3
    REQUEST=$(curl http://web_blue:8081) # blue로 request

    if [ -n "$REQUEST" ]; then # 서비스 가능하면 health check 중지
      echo "health check success"
      break
    fi
  done

  echo "4. reload nginx"
  sudo cp /etc/nginx/conf.d/app/service-url-blue.inc /etc/nginx/conf.d/app/service-url.inc
  sudo nginx -s reload

  echo "5. green container down"
  docker-compose -f /home/ubuntu/docker/docker-compose-app.yml stop web_green
fi
