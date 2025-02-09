# Better than Kideapp

## How to:
Create .env file in project root with your database details
```
MYSQL_USER=USERNAME
MYSQL_PASSWORD=PASSWORD
MYSQL_DATABASE=DATABASE
```

./mvnw clean package

To compile .jar and then

docker compose up --build

To start the service