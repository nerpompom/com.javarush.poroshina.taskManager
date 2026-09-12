SDK 21
Language level 21

Если база еще не создана, перезапуском выполнить в консоли команду:
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
('Правки по лк',          'DONE',        2, 2,    NOW(), false),
('Поменять иконки в каталоге',                     'DELETED',     1, NULL, NOW(), true);

UPDATE tasks
SET completed_at = NOW()
WHERE id = 3;

