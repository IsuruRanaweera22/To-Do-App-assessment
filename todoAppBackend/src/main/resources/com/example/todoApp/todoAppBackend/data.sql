ALTER TABLE tasks ALTER COLUMN task_id RESTART WITH 1;

INSERT INTO tasks (created_at, description, due_date, is_completed, title)
VALUES
    ('2024-10-24', 'Complete project documentation', '2024-10-14', FALSE, 'Task 1'),
    ('2024-10-25', 'Fix database connection issue', '2024-10-14', FALSE, 'Task 2'),
    ('2024-10-26', 'Deploy application to server', '2024-10-14', FALSE, 'Task 3');
