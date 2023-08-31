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
    REQUEST=$(curl http://127.0.0.1:8082) # green으로 request
    if [ -n "$REQUEST" ]; then            # 서비스 가능하면 health check 중지
      echo "health check success"
      break
    fi
  done

  echo "4. reload nginx"
  sudo cp -p /etc/nginx/config/nginx-green.conf /etc/nginx/nginx.conf || exit 1
  sudo systemctl restart nginx || exit 1

  if [ "$(docker ps -qf name=blue)" ]; then
    echo "5. blue container down"
    docker-compose -f /home/ubuntu/docker/docker-compose-app.yml stop web_blue
  fi

else
  echo "### GREEN => BLUE ###"

  echo "1. get blue image"
  docker-compose -f /home/ubuntu/docker/docker-compose-app.yml pull web_blue

  echo "2. blue container up"
  docker-compose -f /home/ubuntu/docker/docker-compose-app.yml up -d web_blue

  while [ 1 = 1 ]; do
    echo "3. blue health check..."
    sleep 3
    REQUEST=$(curl http://127.0.0.1:8081) # blue로 request
    if [ -n "$REQUEST" ]; then # 서비스 가능하면 health check 중지
      echo "health check success"
      break
    fi
  done

  echo "4. reload nginx"
  sudo cp -p /etc/nginx/config/nginx-blue.conf /etc/nginx/nginx.conf || exit 1
    sudo systemctl restart nginx || exit 1

  if [ "$(docker ps -qf name=green)" ]; then
      echo "5. green container down"
      docker-compose -f /home/ubuntu/docker/docker-compose-app.yml stop web_green
    fi
fi
