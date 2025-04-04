# Система бронирования квестов: Web Application (alpha)

## Особенности приложения:
+ Приложение создаётся как B2B SaaS 
+ Возможность создания схемы квеста для роли Owner
+ Управление пользователями для ролей Owner и Admin
+ Сохранение и мониторинг клиентов
+ Подсчёт и контроль финансов
+ API для интеграции с другими сайтами

## Stack:
```
Java 17, MySQL, Spring Boot 3, Spring Data JPA, FlyWay, Spring Security 6, 
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
MySQL, Redis и Spring Boot в Docker контейнерах
> 
>__Ссылка на сервер:__ 
> <a href="http://99152dd5cacb.vps.myjino.ru" target="_blank">http://99152dd5cacb.vps.myjino.ru</a>