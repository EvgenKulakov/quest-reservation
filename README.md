# Система бронирования квестов: Web Application (alpha)

## Особенности приложения:
+ Разные роли с разным доступом для редактирования 
+ Создание схемы квеста для роли Admin
+ В БД хранятся только бронирования, слоты - only runtime
+ Отменённые бронирования не удаляются из БД

## Stack:
```
Java 17, MySQL, Spring Boot 3, Spring Data JPA, Spring Security, 
Spring MVC, Hibernate Validator, Thymeleaf, BootStrap 5, Docker
```
***

### Сделанная часть: 
Взаимодействие с БД, основная логика программы, часть view


### В разработке: 
View, security, refactoring
***

## Схема Database:

![Схема БД](projectinfo/database-diagram.png)
***

### Сервер:
>__Сборка:__
MySQL и Spring Boot в двух отдельных Docker контейнерах на общей сети network
> 
>__Ссылка на сервер:__ 
[quest reservations](http://31.129.99.231:8080/slot-list)
> 
>__Логин/пароль от тестового аккаунта:__ _admin_