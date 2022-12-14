echo ">>> Running gradle build..."
gradle build -x test

if [ $? == 0 ];
then
    echo ">>> Running gradle tests..."
    gradle test 
else
    echo "Error building application."
fi

if [ $? == 0 ];
then
    echo ">>> Building container..."
    docker build --no-cache -t tempo/services/orchestrator .
else
    echo "Error executing tests."
fi

if [ $? == 0 ];
then
    echo ">>> Running containers..."
    docker-compose up --build
else
    echo "Error building docker image."
fi

