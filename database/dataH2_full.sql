
-- Author: ChatGPT for Ismael
-- Version: 1.0
-- Description: Full test dataset for Quizmael

-- Insert additional users
INSERT INTO Users (name, email, password, password_hint, secret_question, secret_answer, birth_date, role) VALUES
('alice', 'alice@example.com', '1234', 'My favorite animal', 'Favorite animal?', 'cat', '1990-05-01', 'REGISTERED'),
('bob', 'bob@example.com', 'abcd', 'My first car', 'First car?', 'fiat', '1985-09-13', 'MODERATOR'),
('charlie', 'charlie@example.com', 'pass', 'My hometown', 'Hometown?', 'madrid', '1992-11-23', 'REGISTERED');

-- Insert topics
INSERT INTO Topics (name) VALUES
('Science'), ('History'), ('Geography'), ('Literature');

-- Insert tests
INSERT INTO Tests (title, creator_id, language, state, options_count, time_limit, description, image, moderated_by_id) VALUES
('Basic Science Test', 10, 'EN', 'APPROVED', 4, 300, 'A basic science test.', NULL, 11),
('European Capitals', 10, 'EN', 'APPROVED', 4, 300, 'Test your knowledge of European capitals.', NULL, 11),
('Famous Books', 11, 'ES', 'PENDING', 4, 600, 'Identify books by famous authors.', NULL, NULL);

-- Insert test topics
INSERT INTO Test_Topics (test_id, topic_id) VALUES
(1, 1), -- Science
(2, 3), -- Geography
(3, 4); -- Literature

-- Insert questions
INSERT INTO Questions (test_id, text, feedback) VALUES
(1, 'What is the chemical symbol for water?', 'H2O is water.'),
(1, 'Which planet is closest to the sun?', 'Mercury is the closest.'),
(2, 'What is the capital of France?', 'Paris is the capital.'),
(2, 'What is the capital of Germany?', 'Berlin is correct.'),
(3, 'Who wrote Don Quixote?', 'Miguel de Cervantes wrote it.');

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
('Kafka', FALSE, 5);

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
