# Chapter 2: Persistence

Let's built upon our basic application from Chapter 1 and add some persistence layer to it. We'll be using PostgreSQL running on a docker container. We'll also be using liquibase to manage the table structures. For the implementation of the persistence layer itself, we'll go the easy route and make use of Spring Data JPA.

## Local Setup
Running things locally is something developers always need to do. Typically, a locally running database is necessary for a service to run. However, one thing I've learned through the years is that having a local database installed can be tricky especially once you start working on different projects that require different database versions. Luckily, Docker is a thing and we can just run a container of the necessary database. Additionally, `docker-compose` simplifies the setup by making the connectivities declarative so the setup becomes portable in case you lose your laptop or something.

## DB Structure Management
A common pitfall of Spring Boot newbies is keeping the Hibernate DDL on. This usually leads to surprises on database structures when running your app. Typically, this is turned off and the database table structures are delegated to a separate team and/or process. To simulate that, we'll be using `Liquibase`. To my surprise, Liquibase now has native SQL support for changelogs. We no longer need to clumsily write SQL strings in XML files such that we can't use the IDE's SQL support for our statements.

## Request Validation
I've missed implementing validations in the previous chapter so we'll do it here. It's simple enough - almost 3 lines of code. Spring Boot really is magic. Though I don't see any need to explore the `Hibernate Validation` configurations just yet. Out-of-the-box features will do for now.

