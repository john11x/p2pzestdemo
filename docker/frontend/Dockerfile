FROM nginx:stable-alpine

LABEL org.opencontainers.image.source="https://github.com/john11x/p2pzestdemo"

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY site/ /usr/share/nginx/html/