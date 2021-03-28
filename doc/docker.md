# 安装mysql
docker run -v /home/mysql/:/var/lib/mysql \
-p 3306:3306 -e MYSQL_ROOT_PASSWORD=dqjdda1996 \
--privileged=true --restart=always --name mariadb -d mariadb

# 安装Redis
docker run -itd --name redis --restart=always -p 6379:6379 redis

# nginx 容器
docker run -d \
--name nginx --restart always \
-p 80:80 -p 443:443 \
-e "TZ=Asia/Shanghai" \
-v /home/nginx/nginx.conf:/etc/nginx/nginx.conf \
-v /home/nginx/conf.d:/etc/nginx/conf.d \
-v /home/nginx/logs:/var/log/nginx \
-v /home/nginx/cert:/etc/nginx/cert \
-v /home/nginx/html:/usr/share/nginx/html \
nginx:alpine
# 注
/home/nginx/conf.d 用于存放配置文件
/home/nginx/cert 用于存放 https 证书
/home/nginx/html 用于存放网页文件
/home/nginx/logs 用于存放日志