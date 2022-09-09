echo ">>> Running gradle build and tests..."
gradle build 

echo ">>> Building container..."
docker build -t tempo/services/orchestrator .

echo ">>> Running containers..."
docker-compose up
