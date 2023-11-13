INSERT INTO tags (content) VALUES
                               ('Tag Sample 1'),
                               ('Tag Sample 2'),
                               ('Tag Sample 3'),
                               ('Tag Sample 4'),
                               ('Tag Sample 5'),
                               ('Tag Sample 6'),
                               ('Tag Sample 7'),
                               ('Tag Sample 8'),
                               ('Tag Sample 9'),
                               ('Tag Sample 10');

INSERT INTO users (first_name, last_name, email, username, password, photo_url) VALUES
                                                                                    ('John', 'Doe', 'john@example.com', 'johndoe', 'password', null),
                                                                                    ('Alice', 'Smith', 'alice@example.com', 'alicesmith', 'password', null),
                                                                                    ('Bob', 'Johnson', 'bob@example.com', 'bobjohnson', 'password', null),
                                                                                    ('Emily', 'Brown', 'emily@example.com', 'emilybrown', 'password', null),
                                                                                    ('David', 'Lee', 'david@example.com', 'davidlee', 'password', null),
                                                                                    ('Sophia', 'Garcia', 'sophia@example.com', 'sophiagarcia', 'password', null),
                                                                                    ('Oliver', 'Martinez', 'oliver@example.com', 'olivermartinez', 'password', null),
                                                                                    ('Emma', 'Lopez', 'emma@example.com', 'emmalopez', 'password', null),
                                                                                    ('Mia', 'Perez', 'mia@example.com', 'miaperez', 'password', null),
                                                                                    ('Ethan', 'Gonzalez', 'ethan@example.com', 'ethangonzalez', 'password',null);

-- Assuming each user has a phone number
INSERT INTO phone_numbers (user_id, number) VALUES
                                                (1, '1234567890'),
                                                (2, '2345678901'),
                                                (3, '3456789012'),
                                                (4, '4567890123'),
                                                (5, '5678901234'),
                                                (6, '6789012345'),
                                                (7, '7890123456'),
                                                (8, '8901234567'),
                                                (9, '9012345678'),
                                                (10, '0123456789');

-- Assuming each user has a post
INSERT INTO posts (user_id, title, content, creation_date, photo_url) VALUES
                                                                          (1, 'Cat Shedding', 'Hi guys, do you know which are the best products for persian cat shedding? I have persian cat and I would like to buy her relevant products.', NOW(), null),
                                                                          (2, 'Golden Retrievers - eating habits', 'Hello, is there anyone who can suggest a proper eating routine for golden retrievers. I got a new one just now and I am wondering how often I should feed him.', NOW(), null),
                                                                          (3, 'Parrots - which is the best vet in Sofia?', 'Hello again, my parrot has some issues with the right leg. Can you please give me contact to the best vet in Sofia? Thank you in advance!', NOW(), null),
                                                                          (4, 'Domestic Rabbit', 'Hi everybody, I am searching for a rabbit to buy. I think they are really cute and calm. They do not cause any mess when you keep them at home. Do you know where I can buy?', NOW(),null ),
                                                                          (5, 'Dog missing!', 'Hi, I lost my dog yesterday. He is responding to the name Lucas. We lost him in the South Park in Sofia. If you happen to find him, please contact us on: 0954546344', NOW(), null),
                                                                          (6, 'Best shampoo for dogs', 'I know a lot of you asked recently about the best shampoo I have used for my puppy. That is why I am going to post the name here: Dog Cleaner 3000 :). This shampoo is only 10 pounds and you can find it in H&M xD', NOW(), null),
                                                                          (7, 'What do you think about dog tattoos? ', 'Hi, I am considering tattooing my dog. Has anyone does this before? Does it hurt the dog?', NOW(), null),
                                                                          (8, 'Dog and cat as roomates?', 'Hi, I am currently living with my dog Emma. I would like to buy a British Shorthair cat, because I love how they look and they are so cute. I am wondering if anybody can tell me whether they have a dog and a cat at home. Do they beat each other. Is it worth ?', NOW(), null),
                                                                          (9, 'Searching for 100x100 aquarium', 'Hi, I am searching for an aquarium with dimensions 100x100. I need it as part of my new office. If anybody can help please DM me.', NOW(), null),
                                                                          (10, 'Dogs for sale', 'My dog Gale has 5 puppies. I am selling 4 of them. If you are interested DM me and I will send you some photos and we can negotiate for a price.', NOW(), null);

-- Assuming users like posts
INSERT INTO likes (post_id, user_id) VALUES
                                         (1, 2), -- User 2 likes Post 1
                                         (2, 3), -- User 3 likes Post 2
                                         (3, 4), -- User 4 likes Post 3
                                         (4, 5), -- User 5 likes Post 4
                                         (5, 6), -- User 6 likes Post 5
                                         (6, 7), -- User 7 likes Post 6
                                         (7, 8), -- User 8 likes Post 7
                                         (8, 9), -- User 9 likes Post 8
                                         (9, 10), -- User 10 likes Post 9
                                         (10, 1); -- User 1 likes Post 10

-- Assuming posts have multiple tags
INSERT INTO posts_tags (post_id, tag_id) VALUES
                                             (1, 2), -- Post 1 is tagged with Tag 2
                                             (2, 3), -- Post 2 is tagged with Tag 3
                                             (3, 4), -- Post 3 is tagged with Tag 4
                                             (4, 5), -- Post 4 is tagged with Tag 5
                                             (5, 6), -- Post 5 is tagged with Tag 6
                                             (6, 7), -- Post 6 is tagged with Tag 7
                                             (7, 8), -- Post 7 is tagged with Tag 8
                                             (8, 9), -- Post 8 is tagged with Tag 9
                                             (9, 10), -- Post 9 is tagged with Tag 10
                                             (10, 1); -- Post 10 is tagged with Tag 1

-- Assuming users reply to posts
INSERT INTO replies (post_id, user_id, content) VALUES
                                                    (1, 3, 'Yes, you can take the clean cat 500. It is a very powerful and the fur is extremely squishy.'),
                                                    (2, 4, 'You can feed them 3 times per day with large quantity as they need more energy.'),
                                                    (3, 5, 'Mr.Winston is working every day from Monday to Friday. He is located at ParrotStreet 31'),
                                                    (4, 6, 'You can DM me. I have 3 rabbits for sale.'),
                                                    (5, 7, 'I think I found him. I send you a DM with my number.'),
                                                    (6, 8, 'Thank you for sharing. Just bought it from the store <3.'),
                                                    (7, 9, 'It is not recommended. The skin can infect and the dog will be in life danger.'),
                                                    (8, 10, 'I think it depends whether they will match. I have two in my house and they are understanding great. They sleep and play together.'),
                                                    (9, 1, 'DM me. I can offer something.'),
                                                    (10, 2, 'I am interested!');

INSERT INTO roles (role_id, name) VALUES
                             (1, 'admin'),
                             (2, 'blockedUser'),
                             (3, 'user');

-- Assuming users have roles
INSERT INTO roles_users (role_id, user_id) VALUES
                                               (1, 1), -- User 1 has the role of 'admin'
                                               (2, 2), -- User 2 has the role of 'user'
                                               (3, 3), -- User 3 has the role of 'blockedUser'
                                               (2, 4), -- User 4 has the role of 'user'
                                               (2, 5), -- User 5 has the role of 'user'
                                               (2, 6), -- User 6 has the role of 'user'
                                               (2, 7), -- User 7 has the role of 'user'
                                               (2, 8), -- User 8 has the role of 'user'
                                               (2, 9), -- User 9 has the role of 'user'
                                               (2, 10); -- User 10 has the role of 'user'