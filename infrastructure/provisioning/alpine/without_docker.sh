#!/bin/bash
if which python3; then 
    echo 'Python already installed lets move'; 
else 
    apk --update --no-cache add ca-certificates openssh-client openssl python3 rsync ca-certificates bash curl
    apk --update add --virtual .build-deps python3-dev libffi-dev openssl-dev build-base
    pip3 install --upgrade pip cffi
    pip3 install ansible
    apk del .build-deps
    rm -rf /var/cache/apk/*
fi