
-- Author: Ismael Reina Muñoz
-- Version: 1.0
-- Description: Full test dataset for Quizmael

-- Insert additional users
INSERT INTO Users (name, email, password, password_hint, secret_question, secret_answer, birth_date, role) VALUES
('alice', 'alice@example.com', '1234', 'My favorite animal', 'Favorite animal?', 'cat', '1990-05-01', 'REGISTERED'),
('bob', 'bob@example.com', 'abcd', 'My first car', 'First car?', 'fiat', '1985-09-13', 'MODERATOR'),
('charlie', 'charlie@example.com', 'pass', 'My hometown', 'Hometown?', 'madrid', '1992-11-23', 'REGISTERED'),
('diana', 'diana@example.com', 'pass123', 'My pet''s name', 'Pet name?', 'fluffy', '1994-07-16', 'REGISTERED');

-- Insert topics
INSERT INTO Topics (name) VALUES
('Science'), ('History'), ('Geography'), ('Literature');

-- Insert tests
INSERT INTO Tests (title, creator_id, language, state, options_count, time_limit, description, difficulty, image, moderated_by_id) VALUES
('Basic Science Test', 10, 'EN', 'PUBLISHED', 4, 300, 'A basic science test.', 3, NULL, 11),
('European Capitals', 10, 'EN', 'PUBLISHED', 4, 300, 'Test your knowledge of European capitals.', 3, NULL, 11),
('Famous Books', 11, 'ES', 'PENDING', 4, 600, 'Identify books by famous authors.', 3, NULL, NULL),
('World General Knowledge', 10, 'EN', 'PUBLISHED', 4, 600, 'A broad test of general world knowledge.', 4, NULL, 11),
('Famous People and Inventions', 11, 'EN', 'PUBLISHED', 4, 600, 'Test your knowledge of inventors and iconic figures.', 3, NULL, 11);

-- Insert test topics
INSERT INTO Test_Topics (test_id, topic_id) VALUES
(1, 1), -- Science
(2, 3), -- Geography
(3, 4), -- Literature
(4, 1), (4, 2), (4, 3), -- Science, History, Geography
(5, 2); -- History

-- Insert questions
INSERT INTO Questions (test_id, text, feedback) VALUES
(1, 'What is the chemical symbol for water?', 'H2O is water.'),
(1, 'Which planet is closest to the sun?', 'Mercury is the closest.'),
(2, 'What is the capital of France?', 'Paris is the capital.'),
(2, 'What is the capital of Germany?', 'Berlin is correct.'),
(3, 'Who wrote Don Quixote?', 'Miguel de Cervantes wrote it.'),
(4, 'What is the largest ocean on Earth?', 'The Pacific Ocean is the largest.'),
(4, 'In which year did World War II end?', 'WWII ended in 1945.'),
(4, 'What is the boiling point of water in Celsius?', '100°C is the standard boiling point.'),
(4, 'Which country has the most population?', 'China has the highest population.'),
(4, 'Who discovered gravity?', 'Isaac Newton discovered gravity.'),
(4, 'Which continent is the Sahara Desert located in?', 'Africa.'),
(4, 'How many legs does a spider have?', 'Spiders have 8 legs.'),
(4, 'What gas do plants absorb from the atmosphere?', 'Carbon dioxide.'),
(4, 'Who was the first man on the Moon?', 'Neil Armstrong.'),
(4, 'What is the capital of Canada?', 'Ottawa.'),
(4, 'Which is the longest river in the world?', 'The Nile.'),
(4, 'What is the square root of 64?', 'It is 8.'),
(5, 'Who invented the telephone?', 'Alexander Graham Bell invented it.'),
(5, 'Who developed the theory of relativity?', 'Einstein.'),
(5, 'Who painted the Mona Lisa?', 'Leonardo da Vinci.'),
(5, 'Who was the first President of the USA?', 'George Washington.'),
(5, 'Who discovered penicillin?', 'Alexander Fleming.'),
(5, 'Who was the first female Prime Minister of the UK?', 'Margaret Thatcher.'),
(5, 'What did Thomas Edison invent?', 'The light bulb (among others).'),
(5, 'Who led India to independence with non-violent protest?', 'Mahatma Gandhi.'),
(5, 'Who is known for the law of motion?', 'Isaac Newton.'),
(5, 'Who composed the Fifth Symphony?', 'Beethoven.'),
(5, 'Who created the polio vaccine?', 'Jonas Salk.'),
(5, 'Who founded Microsoft?', 'Bill Gates.');

-- Insert answers
INSERT INTO Answers (text, is_correct, question_id) VALUES
('H2O', TRUE, 1),
('O2', FALSE, 1),
('CO2', FALSE, 1),
('NaCl', FALSE, 1),

('Mercury', TRUE, 2),
('Venus', FALSE, 2),
('Earth', FALSE, 2),
('Mars', FALSE, 2),

('Paris', TRUE, 3),
('London', FALSE, 3),
('Rome', FALSE, 3),
('Madrid', FALSE, 3),

('Berlin', TRUE, 4),
('Munich', FALSE, 4),
('Hamburg', FALSE, 4),
('Frankfurt', FALSE, 4),

('Cervantes', TRUE, 5),
('Shakespeare', FALSE, 5),
('Hemingway', FALSE, 5),
('Kafka', FALSE, 5),

('Pacific Ocean', TRUE, 6), ('Atlantic Ocean', FALSE, 6), ('Indian Ocean', FALSE, 6), ('Arctic Ocean', FALSE, 6),
('1945', TRUE, 7), ('1939', FALSE, 7), ('1940', FALSE, 7), ('1950', FALSE, 7),
('100', TRUE, 8), ('90', FALSE, 8), ('80', FALSE, 8), ('110', FALSE, 8),
('China', TRUE, 9), ('India', FALSE, 9), ('USA', FALSE, 9), ('Indonesia', FALSE, 9),
('Newton', TRUE, 10), ('Einstein', FALSE, 10), ('Galileo', FALSE, 10), ('Tesla', FALSE, 10),
('Africa', TRUE, 11), ('Asia', FALSE, 11), ('Australia', FALSE, 11), ('South America', FALSE, 11),
('8', TRUE, 12), ('6', FALSE, 12), ('4', FALSE, 12), ('10', FALSE, 12),
('Carbon Dioxide', TRUE, 13), ('Oxygen', FALSE, 13), ('Nitrogen', FALSE, 13), ('Hydrogen', FALSE, 13),
('Neil Armstrong', TRUE, 14), ('Buzz Aldrin', FALSE, 14), ('Yuri Gagarin', FALSE, 14), ('Michael Collins', FALSE, 14),
('Ottawa', TRUE, 15), ('Toronto', FALSE, 15), ('Montreal', FALSE, 15), ('Vancouver', FALSE, 15),
('Nile', TRUE, 16), ('Amazon', FALSE, 16), ('Yangtze', FALSE, 16), ('Mississippi', FALSE, 16),
('8', TRUE, 17), ('6', FALSE, 17), ('7', FALSE, 17), ('9', FALSE, 17),

('Alexander Graham Bell', TRUE, 18), ('Edison', FALSE, 18), ('Tesla', FALSE, 18), ('Newton', FALSE, 18),
('Albert Einstein', TRUE, 19), ('Stephen Hawking', FALSE, 19), ('Bohr', FALSE, 19), ('Galileo', FALSE, 19),
('Leonardo da Vinci', TRUE, 20), ('Michelangelo', FALSE, 20), ('Raphael', FALSE, 20), ('Van Gogh', FALSE, 20),
('George Washington', TRUE, 21), ('Lincoln', FALSE, 21), ('Jefferson', FALSE, 21), ('Adams', FALSE, 21),
('Alexander Fleming', TRUE, 22), ('Pasteur', FALSE, 22), ('Lister', FALSE, 22), ('Curie', FALSE, 22),
('Margaret Thatcher', TRUE, 23), ('Angela Merkel', FALSE, 23), ('Elizabeth II', FALSE, 23), ('Indira Gandhi', FALSE, 23),
('Light bulb', TRUE, 24), ('Steam engine', FALSE, 24), ('Airplane', FALSE, 24), ('Telephone', FALSE, 24),
('Mahatma Gandhi', TRUE, 25), ('Nehru', FALSE, 25), ('Mandela', FALSE, 25), ('Obama', FALSE, 25),
('Isaac Newton', TRUE, 26), ('Galileo', FALSE, 26), ('Einstein', FALSE, 26), ('Edison', FALSE, 26),
('Beethoven', TRUE, 27), ('Mozart', FALSE, 27), ('Bach', FALSE, 27), ('Tchaikovsky', FALSE, 27),
('Jonas Salk', TRUE, 28), ('Fleming', FALSE, 28), ('Sabin', FALSE, 28), ('Curie', FALSE, 28),
('Bill Gates', TRUE, 29), ('Steve Jobs', FALSE, 29), ('Zuckerberg', FALSE, 29), ('Musk', FALSE, 29);

-- Insert games
INSERT INTO Games (user_id, test_id, questionCount, correctAnswers, score) VALUES
(10, 1, 2, 2, 10.00),
(10, 2, 2, 1, 5.00),
(11, 3, 1, 1, 10.00);

-- Insert game questions
INSERT INTO Game_Questions (game_id, question_id, is_correct) VALUES
(1, 1, TRUE),
(1, 2, TRUE),
(2, 3, TRUE),
(2, 4, FALSE),
(3, 5, TRUE);

-- Insert moderations
INSERT INTO Moderations (test_id, moderator_id, assigned_state) VALUES
(1, 11, 'APPROVED'),
(2, 11, 'APPROVED');
