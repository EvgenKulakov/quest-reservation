# Система бронирования квестов: Web Application (alpha)

## Особенности приложения:
+ Разный доступ для редактирования 
+ Создание схемы квеста для роли Admin
+ Управление пользователями для роли Admin
+ Отменённые бронирования не удаляются из БД

## Stack:
```
Java 17, MySQL, Spring Boot 3, Spring Data JPA, Spring Security 6, 
Spring MVC, Hibernate Validator, Thymeleaf, BootStrap 5, Docker
```
***

### Сделанная часть: 
Взаимодействие с БД, часть view, валидация, security


### В разработке: 
View, refactoring, внешний виджет
***

## Схема Database:

![Схема БД](projectinfo/database-diagram.png)
***

### Сервер:
>__Сборка:__
MySQL и Spring Boot в двух отдельных Docker контейнерах на общей сети network
> 
>__Ссылка на сервер:__ 
> <a href="http://31.129.99.231:8080/" target="_blank">quest-reservations</a>
> 
>__Логин/пароль от тестового аккаунта:__ _admin_