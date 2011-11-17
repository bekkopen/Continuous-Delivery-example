Vagrant::Config.run do |config|
  # All Vagrant configuration is done here. The most common configuration
  # options are documented and commented below. For a complete reference,
  # please see the online documentation at vagrantup.com.

  # Every Vagrant virtual environment requires a box to build off of.
  #config.vm.define :ci do |ci_config|
  #  ci_config.vm.box = "oneiric32"
  #  ci_config.vm.forward_port "ssh", 22, 2202
  
  #  ci_config.vm.network("33.33.33.12")
    
    
  #  ci_config.vm.provision :puppet do |puppet|
  #   puppet.manifests_path = "puppet/manifests"
  #   puppet.manifest_file  = "ci.pp"
  #   puppet.module_path = "puppet/modules"
     #puppet.options = "--trace --debug"
  #  end
  #end
  
  
  
  config.vm.define :db do |db_config|
    db_config.vm.box_url = "https://s3-eu-west-1.amazonaws.com/bekkopen/oneiric32.box"
    db_config.vm.box = "oneiric32"
    db_config.vm.forward_port "ssh", 22, 2222
    db_config.vm.forward_port "mysql", 3306, 3306
    
    db_config.vm.network("33.33.33.10")
    
    
    #db_config.vm.boot_mode = :gui
    
    db_config.vm.provision :puppet do |puppet|
     puppet.manifests_path = "puppet/manifests"
     puppet.manifest_file  = "db.pp"
     puppet.module_path = "puppet/modules"
     puppet.options = "--trace --debug"
    end
  end

  config.vm.define :web do |web_config|
    web_config.vm.box_url = "https://s3-eu-west-1.amazonaws.com/bekkopen/oneiric32.box"
    web_config.vm.box = "oneiric32"
    web_config.vm.forward_port "webapp", 9090, 9090
    web_config.vm.forward_port "ssh", 22, 2200
    
    web_config.vm.network("33.33.33.11")
    
    web_config.vm.share_folder "m2_repo", "/home/vagrant/.m2", "~/.m2"
    
    web_config.vm.provision :puppet do |puppet|
     puppet.manifests_path = "puppet/manifests"
     puppet.manifest_file  = "web.pp"
     puppet.module_path = "puppet/modules"
     puppet.options = "--trace --debug"
    end
  end

  # The url from where the 'config.vm.box' box will be fetched if it
  # doesn't already exist on the user's system.
  # config.vm.box_url = "http://domain.com/path/to/above.box"

  # Boot with a GUI so you can see the screen. (Default is headless)
  # config.vm.boot_mode = :gui

  # Assign this VM to a host only network IP, allowing you to access it
  # via the IP.
  # config.vm.network "33.33.33.10"

  # Forward a port from the guest to the host, which allows for outside
  # computers to access the VM, whereas host only networking does not.
  # config.vm.forward_port "http", 80, 8080

  # Share an additional folder to the guest VM. The first argument is
  # an identifier, the second is the path on the guest to mount the
  # folder, and the third is the path on the host to the actual folder.
  # config.vm.share_folder "v-data", "/vagrant_data", "../data"

  # Enable provisioning with Puppet stand alone.  Puppet manifests
  # are contained in a directory path relative to this Vagrantfile.
  # You will need to create the manifests directory and a manifest in
  # the file oneiric32.pp in the manifests_path directory.
  #
  # An example Puppet manifest to provision the message of the day:
  #
  # # group { "puppet":
  # #   ensure => "present",
  # # }
  # #
  # # File { owner => 0, group => 0, mode => 0644 }
  # #
  # # file { '/etc/motd':
  # #   content => "Welcome to your Vagrant-built virtual machine!
  # #               Managed by Puppet.\n"
  # # }
  #
  # config.vm.provision :puppet do |puppet|
  #   puppet.manifests_path = "manifests"
  #   puppet.manifest_file  = "oneiric32.pp"
  # end

  # Enable provisioning with chef solo, specifying a cookbooks path (relative
  # to this Vagrantfile), and adding some recipes and/or roles.
  #
  # config.vm.provision :chef_solo do |chef|
  #   chef.cookbooks_path = "cookbooks"
  #   chef.add_recipe "mysql"
  #   chef.add_role "web"
  #
  #   # You may also specify custom JSON attributes:
  #   chef.json = { :mysql_password => "foo" }
  # end

  # Enable provisioning with chef server, specifying the chef server URL,
  # and the path to the validation key (relative to this Vagrantfile).
  #
  # The Opscode Platform uses HTTPS. Substitute your organization for
  # ORGNAME in the URL and validation key.
  #
  # If you have your own Chef Server, use the appropriate URL, which may be
  # HTTP instead of HTTPS depending on your configuration. Also change the
  # validation key to validation.pem.
  #
  # config.vm.provision :chef_client do |chef|
  #   chef.chef_server_url = "https://api.opscode.com/organizations/ORGNAME"
  #   chef.validation_key_path = "ORGNAME-validator.pem"
  # end
  #
  # If you're using the Opscode platform, your validator client is
  # ORGNAME-validator, replacing ORGNAME with your organization name.
  #
  # IF you have your own Chef Server, the default validation client name is
  # chef-validator, unless you changed the configuration.
  #
  #   chef.validation_client_name = "ORGNAME-validator"
end
