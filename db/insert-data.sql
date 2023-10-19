INSERT INTO tags (content) VALUES
                               ('Tag1'),
                               ('Tag2'),
                               ('Tag3'),
                               ('Tag4'),
                               ('Tag5'),
                               ('Tag6'),
                               ('Tag7'),
                               ('Tag8'),
                               ('Tag9'),
                               ('Tag10');

INSERT INTO users (first_name, last_name, email, username, password) VALUES
    ('John', 'Doe', 'john.doe@example.com', 'johndoe', 'password1'),
    ('Jane', 'Smith', 'jane.smith@example.com', 'janesmith', 'password2'),
    ('Alice', 'Johnson', 'alice.johnson@example.com', 'alicej', 'password3'),
    ('Bob', 'Brown', 'bob.brown@example.com', 'bobbrown', 'password4'),
    ('Ella', 'White', 'ella.white@example.com', 'ellaw', 'password5'),
    ('Charlie', 'Lee', 'charlie.lee@example.com', 'charliel', 'password6'),
    ('Grace', 'Wilson', 'grace.wilson@example.com', 'gracew', 'password7'),
    ('David', 'Martin', 'david.martin@example.com', 'davidm', 'password8'),
    ('Sophia', 'Harris', 'sophia.harris@example.com', 'sophiah', 'password9'),
    ('William', 'Clark', 'william.clark@example.com', 'williamc', 'password10');

INSERT INTO roles (name) VALUES
                             ('user'),
                             ('admin');

INSERT INTO posts (user_id, title, content) VALUES
    (1, 'Sample Post 1', 'This is the content of sample post 1.'),
    (2, 'Sample Post 2', 'This is the content of sample post 2.'),
    (3, 'Sample Post 3', 'This is the content of sample post 3.'),
    (4, 'Sample Post 4', 'This is the content of sample post 4.'),
    (5, 'Sample Post 5', 'This is the content of sample post 5.'),
    (6, 'Sample Post 6', 'This is the content of sample post 6.'),
    (7, 'Sample Post 7', 'This is the content of sample post 7.'),
    (8, 'Sample Post 8', 'This is the content of sample post 8.'),
    (9, 'Sample Post 9', 'This is the content of sample post 9.'),
    (10, 'Sample Post 10', 'This is the content of sample post 10');

INSERT INTO likes (post_id, user_id) VALUES
    (1, 2),
    (2, 3),
    (3, 4),
    (4, 5),
    (5, 6),
    (6, 7),
    (7, 8),
    (8, 9),
    (9, 10),
    (10, 1);

INSERT INTO posts_tags (post_id, tag_id) VALUES
    (1, 1),
    (1, 2),
    (2, 3),
    (2, 4),
    (3, 5),
    (3, 6),
    (4, 7),
    (4, 8),
    (5, 9),
    (5, 10);

INSERT INTO replies (post_id, user_id, content) VALUES
    (1, 2, 'Reply 1 to post 1'),
    (2, 3, 'Reply 1 to post 2'),
    (3, 4, 'Reply 1 to post 3'),
    (4, 5, 'Reply 1 to post 4'),
    (5, 6, 'Reply 1 to post 5'),
    (6, 7, 'Reply 1 to post 6'),
    (7, 8, 'Reply 1 to post 7'),
    (8, 9, 'Reply 1 to post 8'),
    (9, 10, 'Reply 1 to post 9'),
    (10, 1, 'Reply 1 to post 10');

INSERT INTO phone_numbers (number_id, user_id, number)  VALUES
    (1, 2 , 0988779978),
    (2, 6, 0858499534),
    (3, 10, 097987878);

INSERT INTO roles_users (role_id, user_id) VALUES
(1, 1),   -- User 1 with role "user"
(1, 2),   -- User 2 with role "user"
(1, 3),   -- User 3 with role "user"
(1, 4),   -- User 4 with role "user"
(1, 5),   -- User 5 with role "user"
(1, 6),   -- User 6 with role "user"
(1, 7),   -- User 7 with role "user"
(1, 8),   -- User 8 with role "user"
(2, 9),   -- User 9 with role "admin"
(2, 10);  -- User 10 with role "admin"





