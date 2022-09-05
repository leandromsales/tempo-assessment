echo ">>> Running gradle build.."
gradle build -x test

echo ">>> Building container..."
docker build -t tempo/services/orchestrator .

echo ">>> Running containers..."
docker-compose up
