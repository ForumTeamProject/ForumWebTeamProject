INSERT INTO roles (name) VALUES ('blockedUser'), ('admin'), ('user');

-- Sample records for 'tags'
INSERT INTO tags (content) VALUES
                               ('Tag 1'), ('Tag 2'), ('Tag 3'), ('Tag 4'), ('Tag 5'),
                               ('Tag 6'), ('Tag 7'), ('Tag 8'), ('Tag 9'), ('Tag 10');

-- Sample records for 'users'
INSERT INTO users (first_name, last_name, email, username, password) VALUES
                                                                         ('John', 'Doe', 'john@example.com', 'johndoe', 'password1'),
                                                                         ('Jane', 'Smith', 'jane@example.com', 'janesmith', 'password2'),
                                                                         ('Mike', 'Johnson', 'mike@example.com', 'mikejohnson', 'password3'),
                                                                         ('Emily', 'Brown', 'emily@example.com', 'emilybrown', 'password4'),
                                                                         ('Chris', 'Lee', 'chris@example.com', 'chrislee', 'password5'),
                                                                         ('Ava', 'Garcia', 'ava@example.com', 'avagarcia', 'password6'),
                                                                         ('David', 'Martinez', 'david@example.com', 'davidmartinez', 'password7'),
                                                                         ('Sophia', 'Lopez', 'sophia@example.com', 'sophialopez', 'password8'),
                                                                         ('Michael', 'Hill', 'michael@example.com', 'michaelhill', 'password9'),
                                                                         ('Emma', 'Adams', 'emma@example.com', 'emmaadams', 'password10');

-- Sample records for 'phone_numbers'
INSERT INTO phone_numbers (user_id, number) VALUES
                                                (1, '1234567890'), (2, '2345678901'), (3, '3456789012'), (4, '4567890123'),
                                                (5, '5678901234'), (6, '6789012345'), (7, '7890123456'), (8, '8901234567'),
                                                (9, '9012345678'), (10, '0123456789');

-- Sample records for 'posts' (with photo_url set to NULL)
INSERT INTO posts (user_id, title, content, creation_date, photo_url) VALUES
                                                                          (1, 'Post 1', 'Content for Post 1', CURRENT_TIMESTAMP, NULL),
                                                                          (2, 'Post 2', 'Content for Post 2', CURRENT_TIMESTAMP, NULL),
                                                                          (3, 'Post 3', 'Content for Post 3', CURRENT_TIMESTAMP, NULL),
                                                                          (4, 'Post 4', 'Content for Post 4', CURRENT_TIMESTAMP, NULL),
                                                                          (5, 'Post 5', 'Content for Post 5', CURRENT_TIMESTAMP, NULL),
                                                                          (6, 'Post 6', 'Content for Post 6', CURRENT_TIMESTAMP, NULL),
                                                                          (7, 'Post 7', 'Content for Post 7', CURRENT_TIMESTAMP, NULL),
                                                                          (8, 'Post 8', 'Content for Post 8', CURRENT_TIMESTAMP, NULL),
                                                                          (9, 'Post 9', 'Content for Post 9', CURRENT_TIMESTAMP, NULL),
                                                                          (10, 'Post 10', 'Content for Post 10', CURRENT_TIMESTAMP, NULL);

-- Sample records for 'likes'
INSERT INTO likes (post_id, user_id) VALUES
                                         (1, 2), (1, 4), (2, 3), (3, 1), (4, 5),
                                         (5, 7), (6, 6), (6, 9), (7, 8), (8, 10);

-- Sample records for 'posts_tags'
INSERT INTO posts_tags (post_id, tag_id) VALUES
                                             (1, 2), (1, 4), (2, 3), (3, 5), (4, 1),
                                             (5, 6), (6, 8), (6, 10), (7, 7), (8, 9);

-- Sample records for 'replies'
INSERT INTO replies (post_id, user_id, content) VALUES
                                                    (1, 3, 'Reply to Post 1'), (2, 5, 'Reply to Post 2'), (3, 1, 'Reply to Post 3'),
                                                    (4, 6, 'Reply to Post 4'), (5, 8, 'Reply to Post 5'), (6, 2, 'Reply to Post 6'),
                                                    (7, 4, 'Reply to Post 7'), (8, 7, 'Reply to Post 8'), (9, 10, 'Reply to Post 9'),
                                                    (10, 9, 'Reply to Post 10');

-- Sample records for 'roles_users'
INSERT INTO roles_users (role_id, user_id) VALUES
                                               (1, 3), (1, 5), (1, 7), (1, 9), (1, 2),
                                               (2, 1), (2, 4), (2, 6), (2, 8), (3, 10);