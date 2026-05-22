
-- Author: Ismael Reina Muñoz
-- Version: 1.0
-- Description: Full test dataset for Quizmael

-- Insert more users
INSERT INTO Users (name, email, password, password_hint, secret_question, secret_answer, birth_date, role) VALUES
                                                                                                               ('diana2', 'diana@example.com', 'd123', 'My favorite color', 'Color?', 'blue', '1991-08-12', 'REGISTERED'),
                                                                                                               ('edgar', 'edgar@example.com', 'e456', 'My pet’s name', 'Pet?', 'luna', '1988-07-22', 'MODERATOR'),
                                                                                                               ('frida', 'frida@example.com', 'f789', 'My dream job', 'Dream job?', 'doctor', '1994-04-03', 'REGISTERED'),
                                                                                                               ('greg', 'greg@example.com', 'g000', 'My best friend', 'Friend?', 'sam', '1990-12-11', 'ADMINISTRATOR');

-- Insert more tests
INSERT INTO Tests (title, creator_id, language, state, options_count, time_limit, description, difficulty, image, moderated_by_id) VALUES
                                                                                                                                       ('World History Basics', 12, 'EN', 'PUBLISHED', 4, 400, 'Test about major historical events.', 2, NULL, 13),
                                                                                                                                       ('Spanish Literature', 14, 'ES', 'PENDING', 4, 500, 'Preguntas sobre literatura española.', 3, NULL, NULL),
                                                                                                                                       ('Physics Fundamentals', 10, 'EN', 'PUBLISHED', 4, 300, 'Basic physics questions.', 2, NULL, 13);

-- Insert more topics
INSERT INTO Topics (name) VALUES
                              ('Mathematics'), ('Physics');

-- Link new tests with topics
INSERT INTO Test_Topics (test_id, topic_id) VALUES
                                                (4, 2), -- World History Basics -> History
                                                (5, 4), -- Spanish Literature -> Literature
                                                (6, 6); -- Physics Fundamentals -> Physics

-- Insert new questions
INSERT INTO Questions (test_id, text, feedback) VALUES
                                                    (4, 'When did World War II end?', 'It ended in 1945.'),
                                                    (4, 'Who was Napoleon Bonaparte?', 'A French military leader.'),
                                                    (5, '¿Quién escribió La Regenta?', 'Leopoldo Alas "Clarín".'),
                                                    (6, 'What is Newton’s second law?', 'F = m * a.'),
                                                    (6, 'What unit measures force?', 'The Newton.');

-- Insert corresponding answers
INSERT INTO Answers (text, is_correct, question_id) VALUES
                                                        ('1945', TRUE, 6),
                                                        ('1939', FALSE, 6),
                                                        ('1918', FALSE, 6),
                                                        ('1950', FALSE, 6),

                                                        ('French emperor', TRUE, 7),
                                                        ('German king', FALSE, 7),
                                                        ('Spanish poet', FALSE, 7),
                                                        ('Italian painter', FALSE, 7),

                                                        ('Clarín', TRUE, 8),
                                                        ('Bécquer', FALSE, 8),
                                                        ('Cervantes', FALSE, 8),
                                                        ('Lorca', FALSE, 8),

                                                        ('F = m * a', TRUE, 9),
                                                        ('E = m * c^2', FALSE, 9),
                                                        ('V = I * R', FALSE, 9),
                                                        ('a^2 + b^2 = c^2', FALSE, 9),

                                                        ('Newton', TRUE, 10),
                                                        ('Watt', FALSE, 10),
                                                        ('Pascal', FALSE, 10),
                                                        ('Joule', FALSE, 10);

-- Insert new games
INSERT INTO Games (user_id, test_id, questionCount, correctAnswers, score) VALUES
                                                                               (10, 4, 2, 1, 5.00),
                                                                               (12, 6, 2, 2, 10.00),
                                                                               (14, 5, 1, 0, 0.00);

-- Insert new game questions
INSERT INTO Game_Questions (game_id, question_id, is_correct) VALUES
                                                                  (4, 6, FALSE),
                                                                  (4, 7, TRUE),
                                                                  (5, 9, TRUE),
                                                                  (5, 10, TRUE),
                                                                  (6, 8, FALSE);

-- Insert more moderations
INSERT INTO Moderations (test_id, moderator_id, assigned_state) VALUES
                                                                    (4, 13, 'APPROVED'),
                                                                    (6, 13, 'APPROVED');

