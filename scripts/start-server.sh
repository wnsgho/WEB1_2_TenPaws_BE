#!/bin/bash

# 로그 파일 설정
LOG_FILE="/home/ubuntu/deploy.log"
exec 1> >(tee -a "$LOG_FILE") 2>&1


echo "-------------서버 시작 $(date)-------------"

# 작업 디렉토리 이동
echo "작업 디렉토리 이동..."
cd /home/ubuntu/instagram-server || {
    echo "디렉토리 이동 실패"
    exit 1
}

# 현재 Docker 상태 확인
echo "Docker 상태 확인..."
docker info
docker-compose version

# ECR 이미지 pull 시도
echo "도커 이미지 가져오기 시도..."
if ! docker pull 761018890747.dkr.ecr.ap-northeast-2.amazonaws.com/instagram-server:latest; then
    echo "이미지 pull 실패. credential helper 상태 확인..."
    cat ~/.docker/config.json
    exit 1
fi

echo "기존 컨테이너 정리..."
docker-compose down || {
    echo "컨테이너 정리 실패"
    exit 1
}

echo "새 컨테이너 시작..."
docker-compose up -d || {
    echo "컨테이너 시작 실패"
    docker-compose logs
    exit 1
}

echo "컨테이너 상태 확인..."
docker ps
docker-compose ps

echo "-------------서버 배포 완료 $(date)-------------"