#!/bin/bash
if which python3; then 
    echo 'Python already installed lets move'; 
else 
    apk --update --no-cache add ca-certificates openssh-client openssl python3 rsync ca-certificates docker bash curl
    apk --update add --virtual .build-deps python3-dev libffi-dev openssl-dev build-base 
    pip3 install --upgrade pip cffi
    pip3 install ansible docker
    apk del .build-deps
    rm -rf /var/cache/apk/*
    rc-update add docker boot
    addgroup $USER docker
    service docker start
    sleep 20
fi