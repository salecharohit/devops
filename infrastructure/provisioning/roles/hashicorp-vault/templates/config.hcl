storage "file" {
  path = "{{vault_data_directory}}"
}
listener "tcp" {
  address = "0.0.0.0:8200"
  tls_disable = 1
#  tls_cert_file = "./src/test/vault-config/localhost.cert"
#  tls_key_file = "./src/test/vault-config/localhost.key"
}
max_lease_ttl = "10h"
default_lease_ttl = "10h"
ui = true
api_addr = "http://{{vault_api_domain}}:8200"