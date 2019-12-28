# Mailcatcher
Ansible role for setting up [Mailcatcher](http://mailcatcher.me). 

## Requirements
No external requirements exist to this role.

## Role Variables

```
mailcatcher_ip: 0.0.0.0       # Set the ip address of both servers
mailcatcher_smtp-ip: 0.0.0.0  # Set the ip address of the smtp server
mailcatcher_smtp-port: 1025   # Set the ip address of the smtp server
mailcatcher_http-ip: 0.0.0.0  # Set the ip address of the http server
mailcatcher_http-port: 1080   # Set the port address of the http server
```

## Testing

To execute syntax checks and integration tests run:

```
echo "Requirementes:"
pip install ansible
gem install bundler

echo "Run checks:"
bash run_tests.sh
```

## License
BSD

## Author Information
Author: Stephan Hochhaus [stephan@yauh.de](mailto:stephan@yauh.de)

Contributor: Juan Carlos Castillo Cano
[jccastillocano@gmail.com](mailto:jccastillocano@gmail.com)

[yauh.de](http://yauh.de)
