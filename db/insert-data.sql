INSERT INTO tags (content) VALUES
                               ('Technology'),
                               ('Travel'),
                               ('Food'),
                               ('Fashion'),
                               ('Sports'),
                               ('Music'),
                               ('Science'),
                               ('Movies'),
                               ('Health'),
                               ('Books');

-- Insert sample data for the "users" table
INSERT INTO users (first_name, last_name, email, username, password) VALUES
                                                                         ('John', 'Doe', 'john@example.com', 'johndoe', 'password1'),
                                                                         ('Jane', 'Smith', 'jane@example.com', 'janesmith', 'password2'),
                                                                         ('Robert', 'Johnson', 'robert@example.com', 'robertjohnson', 'password3'),
                                                                         ('Emily', 'Davis', 'emily@example.com', 'emilydavis', 'password4'),
                                                                         ('Michael', 'Brown', 'michael@example.com', 'michaelbrown', 'password5'),
                                                                         ('Sarah', 'Williams', 'sarah@example.com', 'sarahwilliams', 'password6'),
                                                                         ('David', 'Jones', 'david@example.com', 'davidjones', 'password7'),
                                                                         ('Olivia', 'Miller', 'olivia@example.com', 'oliviamiller', 'password8'),
                                                                         ('William', 'Wilson', 'william@example.com', 'williamwilson', 'password9'),
                                                                         ('Linda', 'Lee', 'linda@example.com', 'lindalee', 'password10');

-- Insert sample data for the "phone_numbers" table
INSERT INTO phone_numbers (user_id, number) VALUES
                                                (1, '123-456-7890'),
                                                (2, '987-654-3210'),
                                                (3, '555-123-4567'),
                                                (4, '123-555-7890'),
                                                (5, '987-123-3210'),
                                                (6, '555-123-5555'),
                                                (7, '123-555-1234'),
                                                (8, '987-123-9876'),
                                                (9, '555-987-5555'),
                                                (10, '123-123-1234');

-- Insert sample data for the "posts" table
INSERT INTO posts (user_id, title, content, creation_date) VALUES
                                                               (1, 'Introduction to Java Programming', 'This is a Java programming tutorial.', NOW()),
                                                               (2, 'Traveling to Paris', 'My recent trip to Paris was amazing!', NOW()),
                                                               (3, 'Delicious Italian Recipes', 'Learn how to make authentic Italian dishes.', NOW()),
                                                               (4, 'Fashion Trends for 2023', 'Discover the latest fashion trends.', NOW()),
                                                               (5, 'Top 10 Sports Moments of the Year', 'Recap of the most exciting sports events.', NOW()),
                                                               (6, 'Music Concert Highlights', 'Relive the best moments from the music concert.', NOW()),
                                                               (7, 'Latest Scientific Discoveries', 'Explore recent breakthroughs in science.', NOW()),
                                                               (8, 'Must-Watch Movies of the Year', 'Check out the top films of 2023.', NOW()),
                                                               (9, 'Healthy Living Tips', 'Tips for maintaining a healthy lifestyle.', NOW()),
                                                               (10, 'Best Books of the Year', 'A list of recommended books to read.', NOW());

-- Insert sample data for the "likes" table
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

-- Insert sample data for the "posts_tags" table
INSERT INTO posts_tags (post_id, tag_id) VALUES
                                             (1, 1),
                                             (2, 2),
                                             (3, 3),
                                             (4, 4),
                                             (5, 5),
                                             (6, 6),
                                             (7, 7),
                                             (8, 8),
                                             (9, 9),
                                             (10, 10);

-- Insert sample data for the "replies" table
INSERT INTO replies (post_id, user_id, content) VALUES
                                                    (1, 2, 'Great tutorial!'),
                                                    (2, 3, 'Paris is amazing, indeed!'),
                                                    (3, 4, 'I love Italian food.'),
                                                    (4, 5, 'Fashion is my passion.'),
                                                    (5, 6, 'What a year for sports!'),
                                                    (6, 7, 'The concert was fantastic.'),
                                                    (7, 8, 'Science never ceases to amaze me.'),
                                                    (8, 9, 'Movies are a great escape.'),
                                                    (9, 10, 'Health is wealth.'),
                                                    (10, 1, 'I love reading books.');