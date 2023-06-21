# Ekan Avaliacao Back-end

## Descrição do Projeto

API para leitura, criação, atualização e remoção de beneficiários e seus documentos.

## Tecnologias

O projeto foi desenvolvido utilizando as seguintes tecnologias:

* Java 17
* Spring Boot (v3.0.7)
* Spring Security 
* Spring Data JPA
* Spring Test
* Spring Validation
* Java JWT
* Lombok
* H2 (banco de dados)
* IntelliJ (IDE)

## Build

Para realizar o build do projeto, é necessário ter o Java 17 instalado, assim como o maven.

Na pasta do projeto, executar o seguinte comando:

    mvn package

Será gerado o jar **ekan-avaliacao-backend-0.0.1-SNAPSHOT.jar** na pasta _target_.

Para executar a aplicação, basta acessar a pasta _target_ no terminal e executar o comando

    java -jar ekan-avaliacao-backend-0.0.1-SNAPSHOT.jar