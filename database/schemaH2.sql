-- Author: Ismael
-- Version: 1.0


-- It is not necessary to create the database in H2

-- ----------------------------
-- Tables Creation
-- ----------------------------

CREATE TABLE Users (
    user_id         INT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(20) NOT NULL UNIQUE,
    email           VARCHAR(40),
    password        VARCHAR(20),
    password_hint   VARCHAR(100),
    secret_question VARCHAR(100),
    secret_answer   VARCHAR(20),
    birth_date DATE,
    profile_picture BLOB,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ANONYMOUS', 'REGISTERED', 'MODERATOR', 'ADMINISTRATOR'))
);
-- name must be unique.
-- Password is NULL if the user is anonymous.

CREATE TABLE Tests (
    test_id     INT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(100) NOT NULL,
    creator_id  INT NOT NULL,
    language    VARCHAR(5) NOT NULL CHECK (language IN ('ES', 'EN')),
    state       VARCHAR(10) NOT NULL CHECK (state IN ('DRAFT', 'PENDING', 'APPROVED', 'REFUSED')),
    options_count INT NOT NULL CHECK (options_count BETWEEN 2 AND 6),
    time_limit  INT NOT NULL CHECK (time_limit BETWEEN 0 AND 3600),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description TEXT,
    image       BLOB,
    times_played INT DEFAULT 0,
    average_score DECIMAL(4,2) DEFAULT 0.00,
    average_rating DECIMAL(2,1) DEFAULT 0.0,
    FOREIGN KEY (creator_id) REFERENCES Users(user_id) ON DELETE RESTRICT
);
-- Users with created tests cannot be deleted unless their tests are reassigned (e.g. to the 'Deleted User').

CREATE TABLE User_Favorite_Tests (
    user_id INT NOT NULL,
    test_id INT NOT NULL,
    PRIMARY KEY (user_id, test_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (test_id) REFERENCES Tests(test_id) ON DELETE CASCADE
);

CREATE TABLE User_Recent_Tests (
    user_id     INT NOT NULL,
    test_id     INT NOT NULL,
    played_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, test_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (test_id) REFERENCES Tests(test_id) ON DELETE CASCADE
);

CREATE TABLE Topics (
    topic_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Test_Topics (
    test_id  INT NOT NULL,
    topic_id INT NOT NULL,
    PRIMARY KEY (test_id, topic_id),
    FOREIGN KEY (test_id) REFERENCES Tests(test_id) ON DELETE CASCADE,
    FOREIGN KEY (topic_id) REFERENCES Topics(topic_id) ON DELETE CASCADE
);

CREATE TABLE Questions (
    question_id     INT PRIMARY KEY AUTO_INCREMENT,
    test_id         INT NOT NULL,
    text            VARCHAR(255) NOT NULL,
    feedback        VARCHAR(500),
    FOREIGN KEY (test_id) REFERENCES Tests(test_id) ON DELETE CASCADE
);
-- If a test is deleted, its questions are also deleted.

CREATE TABLE Answers (
    answer_id   INT PRIMARY KEY AUTO_INCREMENT,
    text        VARCHAR(255) NOT NULL,
    is_correct  BOOLEAN NOT NULL,
    question_id INT NOT NULL,
    FOREIGN KEY (question_id) REFERENCES Questions(question_id) ON DELETE CASCADE
);

CREATE TABLE Games (
    game_id         INT PRIMARY KEY AUTO_INCREMENT,
    user_id         INT NOT NULL,
    test_id         INT NOT NULL,
    questionCount   INT NOT NULL CHECK (questionCount BETWEEN 1 AND 100),
    correctAnswers  INT NOT NULL CHECK (correctAnswers BETWEEN 0 AND 100),
    score           DECIMAL(4,2) NOT NULL CHECK (score >= 0.00 AND score <= 10.00),
    start_time      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    played_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE RESTRICT
    FOREIGN KEY (test_id) REFERENCES Tests(test_id) ON DELETE RESTRICT
);
-- Users with played games cannot be deleted unless their games are reassigned (e.g. to the 'Deleted User').

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
    moderation_id     INT PRIMARY KEY AUTO_INCREMENT,
    test_id           INT NOT NULL,
    moderator_id      INT NOT NULL,
    assigned_state    VARCHAR(10) NOT NULL CHECK (assigned_state IN ('PENDING', 'APPROVED', 'REFUSED')),
    moderated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rejection_reason TEXT,
    FOREIGN KEY (test_id) REFERENCES Tests(test_id) ON DELETE CASCADE,
    FOREIGN KEY (moderator_id) REFERENCES Users(user_id) ON DELETE CASCADE
);
-- A test can be moderated multiple times.
-- Only users with roles MODERATOR or ADMINISTRATOR should appear here.