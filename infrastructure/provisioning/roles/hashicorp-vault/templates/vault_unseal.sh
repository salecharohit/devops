#!/bin/bash
key1=$(cat {{vault_config_directory}}/unseal_key_0)
key2=$(cat {{vault_config_directory}}/unseal_key_1)
key3=$(cat {{vault_config_directory}}/unseal_key_2)
curl -X PUT -H "Content-Type: application/json" -d "{\"key\":\"$key1\"}" http://127.0.0.1:8200/v1/sys/unseal
curl -X PUT -H "Content-Type: application/json" -d "{\"key\":\"$key2\"}" http://127.0.0.1:8200/v1/sys/unseal
curl -X PUT -H "Content-Type: application/json" -d "{\"key\":\"$key3\"}" http://127.0.0.1:8200/v1/sys/unseal