SDK 21
Language level 21

Если база еще не создана, перед запуском выполнить в консоли команду:
CREATE DATABASE task_manager;

Пользователи и задачи для теста

-- Пользователи
INSERT INTO users (username, password) VALUES
('test_1', 'password123'),
('test_2',    'qwerty');

-- Задачи
INSERT INTO tasks (description, task_status, author_id, executor_id, created_at, is_deleted) VALUES
('Редизайн главной',  'CREATED',     1, NULL,    NOW(), false),
('Правки по мп',  'IN_PROGRESS', 2, 1,    NOW(), false),
('Правки по лк',          'DONE',        2, 2,    NOW(), false);

UPDATE tasks
SET completed_at = NOW()
WHERE id = 3;

Ручки для пользователя:
POST http://localhost:8080/api/v1/users - создание пользователя
GET http://localhost:8080/api/v1/users - получить список всех пользователей (id, username, задачи где он автор и задачи где он исполнитель)
GET http://localhost:8080/api/v1/users/{id} - получить конкретного пользователя по id

Ручки для задач:
GET http://localhost:8080/api/v1/tasks/{id} - получить задачу по id
GET http://localhost:8080/api/v1/tasks - получить список всех задач
POST http://localhost:8080/api/v1/tasks - создать новую задачу (при создании задаем только authorId и description)
PUT http://localhost:8080/api/v1/tasks/{id} - изменить задачу. Правила: у созданной нет исполнителя, нельзя задачу из созданной перевести сразу в готовую, у задачи в работе и в готово должен быть исполнитель
DELETE http://localhost:8080/api/v1/tasks/{id} - удалить задачу (просто скрывает из результатов)


