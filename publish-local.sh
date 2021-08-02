#!/usr/bin/env bash
mvn clean package -Dmaven.test.skip=true
echo '项目打包完成'
docker build -t registry.cn-qingdao.aliyuncs.com/zcj-oss/gitlab-dingding-sonarqube:latest .
echo '镜像创建完成'
docker push registry.cn-qingdao.aliyuncs.com/zcj-oss/gitlab-dingding-sonarqube:latest
echo '镜像推送完成'
docker rmi registry.cn-qingdao.aliyuncs.com/zcj-oss/gitlab-dingding-sonarqube:latest
echo '镜像已清理'

