echo ">>> Running gradle tests..."
gradle test 

echo ">>> Running gradle build..."
if [ $? == 0 ];
then
    echo ">>> Building container..."
    gradle build -x test
else
    echo "Error executing tests. Unable to build the application."
fi

if [ $? == 0 ];
then
    echo ">>> Building container..."
    docker build -t tempo/services/orchestrator .
else
    echo "Error building application."
fi

if [ $? == 0 ];
then
    echo ">>> Running containers..."
    docker-compose up
else
    echo "Error building docker image."
fi

