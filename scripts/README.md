Example scripts
=====

* deploy.sh: Example deploy script for deploying your artifact from local file or from a nexus-repository. Requires deploy.config (example in ../config).
* startup.sh: Example init-script. Place it in /etc/init.d, install it with chkconfig and start/stop/restart with service.
* deploy_to_servers.sh: Example script for deploying your app to multiple nodes (push deployment).
* server_monitor.sh: Example script for light-weight monitoring of your app. Install as cron job or similar.
