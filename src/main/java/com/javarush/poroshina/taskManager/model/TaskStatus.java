package com.javarush.poroshina.taskManager.model;

//CREATED - все поля обязательные, кроме executor_id
//IN_PROGRESS - executor_id = user.id (того кто метод выполнил)
//DELETED - is_deleted = true
public enum TaskStatus {
    CREATED,
    IN_PROGRESS,
    DONE,
}
