# Chapter 2: Persistence

Let's built upon our basic application from Chapter 1 and add some persistence layer to it. We'll be using PostgreSQL running on a docker container. We'll also be using liquibase to manage create the table structures. For the implementation of the persistence layer itself, we'll go the easy route and make use of Spring Data JPA.

