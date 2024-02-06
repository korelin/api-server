multipass launch -n linux-vm -d 50G
multipass shell linux-vm
sudo apt update
sudo apt install ubuntu-desktop xrdp -y -qq

sudo passwd ubuntu

sudo apt install maven -y -qq
sudo apt install openjdk-17-jdk -y -qq
sudo apt install docker.io -y -qq
sudo apt install jq -y -qq
sudo groupadd docker
sudo usermod -aG docker $USER
sudo apt install docker-compose -y -qq