# idtrust-prova

Prova IDTrust Desenvolvedor Sênior

## Requerimentos

Para construir e rodar a aplicação você vai precisar:

- [JDK 14](https://www.oracle.com/java/technologies/javase/jdk14-archive-downloads.html)
- [Gradle](https://gradle.org/install/)

## Rodando a aplicação localmente

Basta importar o projeto numa IDE como projeto Gradle e executar a classe IdTrustApplication.java

Ou alternativamente executar:
```shell
 ./gradlew bootRun
```

## Docker

- [Docker](https://www.docker.com/products/docker-desktop)

Para rodar a aplicação via docker executar os seguintes comandos na raiz do projeto

```shell
 ./gradlew bootJar
 docker build -t idtrust-prova-0.0.1.jar .
 docker run idtrust-prova-0.0.1.jar
```

## Banco de dados Postgres
Criar um volume chamado 'postgresql-volume'

```shell
 docker volume create postgresql-volume
``` 
- [docker-compose.yml ](https://drive.google.com/file/d/1BzVluDULWZUnVFoG67OR6slpyJhv6vAy/view?usp=sharing)
Executar o arquivo docker-compose.yml

```shell
docker-compose up
``` 
JDBC URL: jdbc:postgresql://localhost:54320/idtrust

username: postgres

password: postgres

## Testando a API com Postman

Requisição GET:
http://localhost:8080/cotacao-cepea/COTTON_D/12-03-2020
 
## Copyright

Released under the Apache License 2.0. See the [LICENSE](https://github.com/codecentric/springboot-sample-app/blob/master/LICENSE) file.
