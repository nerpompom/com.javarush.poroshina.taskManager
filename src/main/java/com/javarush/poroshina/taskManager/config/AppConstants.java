package com.javarush.poroshina.taskManager.config;

public final class AppConstants {

    //Роли
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    //Сообщения
    public static final String TASK_SUCCESSFULLY_DELETED = "Task was deleted successfully";
    public static final String NUMBER_TASKS_CREATED = "Number of tasks created";
    public static final String NUMBER_SUCCESSFUL_USER_LOGINS = "Number of successful user logins";

    //Исключения
    public static final String INVALID_VALUE_PARAMETER = "Invalid parameter value";
    public static final String INVALID_REQUEST_BODY = "Request body is missing or contains invalid data";
    public static final String BLANK_DESCRIPTION_MASSAGE = "Description must not be blank";
    public static final String BIG_DESCRIPTION_MASSAGE = "Description must be at most 1000 characters";
    public static final String NULL_STATUS_MESSAGE = "Task status must not be null";
    public static final String BLANK_USERNAME_MESSAGE = "Username must not be blank";
    public static final String SMALL_USERNAME_MESSAGE = "Username must be at least 3 characters";
    public static final String BLANK_PASSWORD_MESSAGE = "Password must not be blank";
    public static final String SMALL_PASSWORD_MESSAGE = "Password must be at least 3 characters";
    public static final String NO_PERMISSION_MESSAGE = "You do not have permission to perform this action";
    public static final String AUTHENTICATION_REQUIRED_MESSAGE = "Authentication is required";
    public static final String INVALID_TOKEN_MESSAGE = "Invalid or expired token";
    public static final String USER_TOKEN_NOT_FOUD_MASSAGE = "User from token was not found";
    public static final String USERNAME_EXIST_MESSAGE = "Username is already taken";
    public static final String INVALID_USERNAME_OR_PASSWORD_MESSAGE = "Invalid username or password";
    public static final String TASK_NOT_FOUND_MESSAGE = "Task not found with id: ";
    public static final String EXECUTOR_ID_REQUIRED_FOR_IN_PROGRESS_MESSAGE = "Executor id is required for IN_PROGRESS status";
    public static final String EXECUTOR_ID_MUST_BE_NULL_FOR_CREATED_MESSAGE = "Executor id must be null for CREATED status";
    public static final String EXECUTOR_ID_REQUIRED_FOR_DONE_MESSAGE = "Executor id is required for DONE status";
    public static final String ONLY_IN_PROGRESS_TASK_CAN_BE_COMPLETED_MESSAGE = "Only task in progress can be completed";
    public static final String USER_NOT_FOUND_MESSAGE = "User not found with id: ";
    public static final String BLANK_DESCRIPTION_SEARCH_MESSAGE = "Description search value must not be blank";
    public static final String CURRENT_USER_NOT_AUTHENTICATED_MESSAGE = "Current user is not authenticated";
    public static final String AUTHENTICATED_USER_NOT_FOUND_MESSAGE = "Authenticated user was not found";
    public static final String AUTHOR_NOT_FOUND_FOR_TASK_MESSAGE = "Author not found for task with id: ";
    public static final String EXECUTOR_NOT_FOUND_FOR_TASK_MESSAGE = "Executor not found for task with id: ";

    //Кастомные метрики
    public static final String METRIC_TASKS_CREATED = "tasks.created";
    public static final String METRIC_USERS_LOGIN_SUCCESS = "users.login.success";
}
