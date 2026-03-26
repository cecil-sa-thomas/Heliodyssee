#!/bin/bash

set -e  # stop the script if any command fails

echo -e "\n🚀 Starting the Heliodyssee DB initialization script\n"
echo -e "\n📋 Listing existing Docker containers...\n"

docker ps -a || { echo "❌ Error while executing 'docker ps -a'"; exit 1; }

echo -e "\n"
read -p "Do you want to remove existing containers? (y/n): " answer
echo -e "\n"

if [[ "$answer" =~ ^[yY]$ ]]; then
    echo -e "\n🛑 Running 'docker-compose down'...\n"
    docker-compose -f docker-compose.yml down || { echo "❌ Error while executing 'docker-compose down'"; exit 1; }
    docker ps -a
    echo -e "\n✅ Containers stopped.\n"
fi

read -p "Do you want to delete the volume 'docker-heliodyssee_mysql_data'? (y/n): " answer2
echo -e "\n"

if [[ "$answer2" =~ ^[yY]$ ]]; then
    echo -e "\n🧽 Removing volume 'docker-heliodyssee_mysql_data'...\n"
    docker volume rm heliodyssee_mysql_data || { echo "❌ Error while removing the volume"; exit 1; }
    echo -e "\n✅ Volume removed.\n"
fi

read -p "Do you want to continue and run 'docker-compose up'? (y/n): " answer3
echo

if [[ "$answer3" =~ ^[yY]$ ]]; then
    echo -e "\n📦 Running 'docker-compose up -d'...\n"
    docker-compose -f docker-compose.yml up -d || { echo "❌ Error while running 'docker-compose up -d'"; exit 1; }
    echo -e "\n✅ Containers started.\n"

    echo -e "\n⏳ Waiting for MySQL to be ready...\n"

    # Détection automatique du conteneur MySQL (au cas où le nom change)
    MYSQL_CONTAINER=$(docker ps --format '{{.Names}}' | grep 'mysql' | head -n 1)

    for i in {1..30}; do
        if docker exec "$MYSQL_CONTAINER" mysqladmin ping -h"localhost" --silent; then
            echo "✅ MySQL is ready!"
            break
        fi
        echo "⌛ MySQL not ready yet ($i)..."
        sleep 1
    done

    if ! docker exec "$MYSQL_CONTAINER" mysqladmin ping -h"localhost" --silent; then
        echo "❌ MySQL did not become ready in time."
        exit 1
    fi
fi

read -p "Do you want to continue and run 'mvn liquibase:update -P mysql'? (y/n): " answer4
echo -e "\n"

if [[ "$answer4" =~ ^[yY]$ ]]; then
    echo -e "\n🛠️  Running Liquibase...\n"
    mvn compile && mvn liquibase:update -P mysql || { echo "❌ Error while running Liquibase"; exit 1; }
else
    echo -e "\n⏭️  Skipping Liquibase update.\n"
fi

read -p "Do you want to continue and run 'mvn spring-boot:run'? (y/n): " answer5
echo -e "\n"

if [[ "$answer5" =~ ^[yY]$ ]]; then
    echo -e "\n🚀  Running Spring Boot...\n"
    mvn spring-boot:run || { echo "❌ Error while running Spring Boot"; exit 1; }
else
    echo -e "\n⛔ Script aborted before launching Spring Boot.\n"
    exit 1
fi