# Practical DevOps - The Lab
I've created this DevOps lab to explain the following terms through separate blogposts

1. [Infrastructure As Code using Vagrant Ansible & Docker](https://www.rohitsalecha.com/post/practical_devops_infrastructure_as_code_vagrant_ansible_docker/)
2. [Continous Integration with Git Vault Talisman](https://www.rohitsalecha.com/post/practical_devops_continous_integration_git_vault_talisman/)
3. [Continous Delivery using Jenkins](https://www.rohitsalecha.com/post/practical_devops_continous_delivery_jenkins/)
4. [Continous Monitoring using Elasticsearch Logstash Kibana Filebeat](https://www.rohitsalecha.com/post/practical_devops_continous_monitoring_elasticsearch_logstash_kibana_filebeat/)

# Setup Instructions
[Set up Instructions](https://www.rohitsalecha.com/project/practical_devops/)

## Ubuntu 18.04 distro environment on Windows
https://kiazhi.github.io/blog/The-easy-way-to-get-Ubuntu-18.04-distro-environment-on-Windows/

# For Testing 
vagrant destroy vault.devops -f
vagrant up vault.devops

VAULT_TOKEN_MYSQL = MYSQL_ROOT_POLICY_TOKEN
TASK [Spooling MYSQL_ROOT_POLICY_TOKEN Vault Tokens] ***************************
ok: [vault.devops] => {"ansible_facts": {"MYSQL_ROOT_POLICY_TOKEN": "s.BrFHZoIpodIvalzYFuyTxzh6"}, "changed": false}

TASK [Spooling MYSQL_DB_POLICY_TOKEN Vault Tokens] *****************************
ok: [vault.devops] => {"ansible_facts": {"MYSQL_DB_POLICY_TOKEN": "s.fq8SdoWAzDxZfQ87IPDdjxFk"}, "changed": false}