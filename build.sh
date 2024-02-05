mvn clean
mvn compile quarkus:build
docker build -f src/main/docker/Dockerfile.jvm -t api-srv:latest .