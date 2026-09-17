Проект сделан на SDK 21 (language level 21)


> [!important] Перед первым запуском
> Если база еще не создана, перед запуском выполнить в консоли команду: CREATE DATABASE task_manager;


> [!example] Данные чтобы заполнить базу первоначально
> -- Пользователи INSERT INTO users (username, password) VALUES ('test_1', 'password123'), ('test_2', 'qwerty');
> -- Задачи INSERT INTO tasks (description, task_status, author_id, executor_id, created_at, is_deleted) VALUES ('Редизайн главной', 'CREATED', 1, NULL, NOW(), false), ('Правки по мп', 'IN_PROGRESS', 2, 1, NOW(), false), ('Правки по лк', 'DONE', 2, 2, NOW(), false);
> --UPDATE tasks SET completed_at = NOW() WHERE id = 3;

### Ручки для пользователей

| №   | Метод | Адрес                                   | Комментарий                                                                                        |
| --- | ----- | --------------------------------------- | -------------------------------------------------------------------------------------------------- |
| 1   | POST  | http://localhost:8080/api/v1/users      | создание пользователя                                                                              |
| 2   | GET   | http://localhost:8080/api/v1/users      | получить список всех пользователей (id, username, задачи где он автор и задачи где он исполнитель) |
| 3   | GET   | http://localhost:8080/api/v1/users/{id} | получить конкретного пользователя по id                                                            |
### Ручки для фильтрации пользователей

| №   | Метод | Адрес                                                                                                                                             | Комментарий                                           |
| --- | ----- | ------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------- |
| 1   | GET   | http://localhost:8080/api/v1/users/with-authored-tasks                                                                                            | Пользователи, у которых есть задачи, где они авторы   |
| 2   | GET   | http://localhost:8080/api/v1/users/by-authored-task/{taskId}                                                                                      | Автор конкретной задачи                               |
| 3   | GET   | http://localhost:8080/api/v1/users/without-authored-tasks                                                                                         | Пользователи не являющиеся авторами задач             |
| 4   | GET   | http://localhost:8080/api/v1/users/with-executed-tasks                                                                                            | Пользователи исполнители задач                        |
| 5   | GET   | http://localhost:8080/api/v1/users/by-executed-task/{taskId}                                                                                      | Исполнитель конкретной задачи                         |
| 6   | GET   | http://localhost:8080/api/v1/users/without-executed-tasks                                                                                         | Пользователи, у которых нет задач где они исполнители |
| 7   | GET   | http://localhost:8080/api/v1/users/with-tasks-by-status?status=IN_PROGRESS<br>http://localhost:8080/api/v1/users/with-tasks-by-status?status=DONE | Исполнители, у которых задачи в определенном статусе  |
| 8   | GET   | http://localhost:8080/api/v1/users/search?username=test_3                                                                                         | Поиск по username                                     |


### Ручки для задач


| №   | Метод  | Адрес                                   | Комментарий                                                                                                                                                       |
| --- | ------ | --------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | GET    | http://localhost:8080/api/v1/tasks/{id} | получить задачу по id                                                                                                                                             |
| 2   | GET    | http://localhost:8080/api/v1/tasks      | получить список всех задач                                                                                                                                        |
| 3   | POST   | http://localhost:8080/api/v1/tasks      | создать новую задачу (при создании задаем только authorId и description)                                                                                          |
| 4   | PUT    | http://localhost:8080/api/v1/tasks/{id} | изменить задачу. Правила: у созданной нет исполнителя, нельзя задачу из созданной перевести сразу в готовую, у задачи в работе и в готово должен быть исполнитель |
| 5   | DELETE | http://localhost:8080/api/v1/tasks/{id} | удалить задачу (просто скрывает из результатов)                                                                                                                   |
### Ручки для фильтрации задач

| №   | Метод | Адрес                                                                | Комментарий                          |
| --- | ----- | -------------------------------------------------------------------- | ------------------------------------ |
| 1   | GET   | http://localhost:8080/api/v1/tasks/by-author/{authorId}              | Найти задачи конкретного автора      |
| 2   | GET   | http://localhost:8080/api/v1/tasks/by-executor/{executorId}          | Найти задачи конкретного исполнителя |
| 3   | GET   | http://localhost:8080/api/v1/tasks/without-executor                  | Найти задачи без исполнителя         |
| 4   | GET   | http://localhost:8080/api/v1/tasks/by-status/{status}                | Найти задачи по статусу              |
| 5   | GET   | http://localhost:8080/api/v1/tasks/by-created-date?date=2026-09-15   | Найти задачи по дате создания        |
| 6   | GET   | http://localhost:8080/api/v1/tasks/by-updated-date?date=2026-09-17   | Найти задачи по дате изменения       |
| 7   | GET   | http://localhost:8080/api/v1/tasks/by-completed-date?date=2026-09-17 | Найти задачи по дате завершения      |
| 8   | GET   | http://localhost:8080/api/v1/tasks/search?description=правки         | Найти задачи по описанию             |
