# Initial cluster
sudo docker run -d --name node1 -h node1 progrium/consul -server -bootstrap-expect 3
JOIN_IP="$(sudo docker inspect -f '{{.NetworkSettings.IPAddress}}' node1)"

sudo docker run -d --name node2 -h node2 progrium/consul -server -join $JOIN_IP
sudo docker run -d --name node3 -h node3 progrium/consul -server -join $JOIN_IP

# local agent
sudo docker run -d -p 8400:8400 -p 8500:8500 -p 8600:53/udp --name node4 -h node4 progrium/consul -join $JOIN_IP

http://localhost:8500/ui/
-> service registered
-> key value edition

dig @0.0.0.0 -p 8600 consul.service.consul
dig @0.0.0.0 -p 8600 MyDeadService.service.consul
dig @0.0.0.0 -p 8600 MyLivingService.service.consul