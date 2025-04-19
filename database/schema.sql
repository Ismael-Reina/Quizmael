-- ----------------------------
-- Database Creation
-- ----------------------------
CREATE DATABASE quizmael_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE quizmael_db;

-- ----------------------------
-- Tables Creation
-- ----------------------------

CREATE TABLE Users (
    user_id         INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(20) NOT NULL,
    email           VARCHAR(40) NULL UNIQUE,
    password        VARCHAR(20) NULL, -- Encrypted
    password_hint   VARCHAR(100) NULL,
    secret_question VARCHAR(100) NULL,
    secret_answer   VARCHAR(20) NULL, -- Encrypted
    role ENUM('ANONYMOUS', 'REGISTERED', 'MODERATOR', 'ADMINISTRATOR') NOT NULL
);
-- Email must be unique.
-- Password is NULL if the user is anonymous.

CREATE TABLE Tests (
    test_id     INT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(100) NOT NULL,
    creator_id  INT NOT NULL,
    state       ENUM('PENDING', 'APPROVED', 'REFUSED') NOT NULL,
    FOREIGN KEY (creator_id) REFERENCES Users(user_id) ON DELETE SET NULL
);
-- If a user is deleted, their created tests remain (creator_id set to NULL).

CREATE TABLE Questions (
    question_id     INT AUTO_INCREMENT PRIMARY KEY,
    test_id         INT NOT NULL,
    text            VARCHAR(255) NOT NULL,
    feedback        VARCHAR(500) NULL,
    FOREIGN KEY (test_id) REFERENCES Tests(test_id) ON DELETE CASCADE
);
-- If a test is deleted, its questions are also deleted.

CREATE TABLE Answers (
    answer_id   INT AUTO_INCREMENT PRIMARY KEY,
    text        VARCHAR(255) NOT NULL,
    is_correct  BOOLEAN NOT NULL,
    question_id INT NOT NULL,
    FOREIGN KEY (question_id) REFERENCES Questions(question_id) ON DELETE CASCADE
);

CREATE TABLE Games (
    game_id     INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT NULL,
    played_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE SET NULL
);
-- If a user is deleted, their played games are preserved.

CREATE TABLE Game_Questions (
    game_id      INT NOT NULL,
    question_id  INT NOT NULL,
    is_correct   BOOLEAN NOT NULL,
    PRIMARY KEY (game_id, question_id),
    FOREIGN KEY (game_id) REFERENCES Games(game_id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES Questions(question_id) ON DELETE CASCADE
);
-- Tracks which questions appeared in a game and if the user answered correctly.

CREATE TABLE Moderations (
    moderation_id     INT AUTO_INCREMENT PRIMARY KEY,
    test_id           INT NOT NULL,
    moderator_id      INT NOT NULL,
    assigned_state    ENUM('PENDING', 'APPROVED', 'REFUSED') NOT NULL,
    moderated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (test_id) REFERENCES Tests(test_id) ON DELETE CASCADE,
    FOREIGN KEY (moderator_id) REFERENCES Users(user_id) ON DELETE CASCADE
);
-- A test can be moderated multiple times.
-- Only users with roles MODERATOR or ADMINISTRATOR should appear here.