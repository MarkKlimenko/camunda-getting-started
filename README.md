# c8-getting-started-microservice-orchestration-lab

## Start postgres for application
```shell
docker run --name camunda-app-postgres -p 5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres:15.2
docker start camunda-app-postgres
docker stop camunda-app-postgres
docker rm camunda-app-postgres
```
