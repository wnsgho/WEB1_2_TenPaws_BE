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

# 업로드 디렉토리 설정
echo "업로드 디렉토리 설정..."
mkdir -p /home/ubuntu/instagram-server/uploads
chmod 777 /home/ubuntu/instagram-server/uploads || {
    echo "업로드 디렉토리 권한 설정 실패"
    exit 1
}

# Nginx 설정 파일 권한 확인
echo "Nginx 설정 파일 권한 확인..."
chmod 644 nginx.conf || {
    echo "Nginx 설정 파일 권한 설정 실패"
    exit 1
}

# 프론트엔드 빌드 디렉토리 확인 및 생성
echo "프론트엔드 빌드 디렉토리 확인..."
mkdir -p frontend/build
if [ ! -d "frontend/build" ]; then
    echo "프론트엔드 빌드 디렉토리 생성 실패"
    exit 1
fi

# 현재 Docker 상태 확인
echo "Docker 상태 확인..."
docker info
docker-compose version

# Docker 볼륨 상태 확인
echo "Docker 볼륨 상태 확인..."
docker volume ls

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

# Docker 볼륨 생성 확인
echo "Docker 볼륨 생성 상태 확인..."
docker volume create --name=uploads_data || echo "볼륨이 이미 존재하거나 생성됨"
docker volume create --name=mysql_data || echo "볼륨이 이미 존재하거나 생성됨"

echo "새 컨테이너 시작..."
docker-compose up -d || {
    echo "컨테이너 시작 실패"
    docker-compose logs
    exit 1
}

echo "컨테이너 상태 확인..."
docker ps
docker-compose ps

# 볼륨 마운트 상태 확인
echo "볼륨 마운트 상태 확인..."
docker inspect instagram-server_tenpaws-server_1 | grep Mounts -A 20

# Nginx 컨테이너 상태 확인
echo "Nginx 컨테이너 상태 확인..."
docker inspect instagram-server_nginx_1 || {
    echo "Nginx 컨테이너 상태 확인 실패"
    exit 1
}

echo "-------------서버 배포 완료 $(date)-------------"