#!/bin/bash

apt update
apt install nginx -y
systemctl enable nginx
systemctl start nginx
