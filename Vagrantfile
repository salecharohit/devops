# -*- mode: ruby -*-
# vi: set ft=ruby :

VAGRANTFILE_API_VERSION = "2"
#loading hosts from a file https://bertvv.github.io/notes-to-self/2015/10/05/one-vagrantfile-to-rule-them-all/
hosts = YAML.load_file('hosts.yml')
UBUNTU_BASE_BOX = 'ubuntu/bionic64'
ALPINE_BASE_BOX = 'maier/alpine-3.8-x86_64'

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  
# Check for missing plugins
  required_plugins = %w(vagrant-disksize vagrant-hostmanager vagrant-vbguest vagrant-clean)
  plugin_installed = false
  required_plugins.each do |plugin|
    unless Vagrant.has_plugin?(plugin)
          system "vagrant plugin install #{plugin}"  
      plugin_installed = true
    end
  end

  # If new plugins installed, restart Vagrant process
  if plugin_installed === true
    exec "vagrant #{ARGV.join' '}"
  end

  config.hostmanager.enabled = true
  config.hostmanager.manage_host = true
  config.hostmanager.manage_guest = true
  config.hostmanager.include_offline= true
  config.hostmanager.ignore_private_ip = false

  config.vm.box_check_update = false
  config.vbguest.auto_update = false

  hosts.each do |host|
    config.vm.define host['name'] do |node|
      node.vm.hostname = host['name']
      node.vm.network :private_network, ip: host['ip']
      node.disksize.size = host['size']
# ------------------- Box Section -------------------------------      
      if host['os'] == "alpine"
        node.vm.box = ALPINE_BASE_BOX
        if host['docker'] == "required"
          node.vm.provision "shell",privileged: true, path: 'infrastructure/provisioning/alpine/with_docker.sh'
        else
          node.vm.provision "shell",privileged: true, path: 'infrastructure/provisioning/alpine/without_docker.sh'
        end
      else
        node.vm.box = UBUNTU_BASE_BOX
      end
# ------------------- Provisioning Section -------------------------------
      if host['provisioner'] == 'shell'
        node.vm.provision "shell",privileged: true, path: host['playbook']
      else
        node.vm.provision "ansible_local" do |ansible|
          ansible.playbook = host['playbook']
          ansible.verbose = "False"
        end
      end
      node.vm.provider :virtualbox do |vb|
        vb.gui = false
        vb.name = host['name']
        vb.customize ["modifyvm", :id, "--memory", host['mem'], "--cpus", host['cpus'], "--hwvirtex", "on","--groups", "/DevOps-Lab",
        "--natdnshostresolver1", "on", "--cableconnected1", "on"]
      end      
    end
  end
end