-- Insert Users
INSERT INTO Users (name, email, password, role) VALUES
('Admin1', 'admin1@email.com', 'pass123', 'ADMINISTRATOR'),
('Mod1', 'mod1@email.com', 'pass456', 'MODERATOR'),
('User1', 'user1@email.com', 'pass789', 'REGISTERED'),
('User2', 'user2@email.com', 'pass321', 'REGISTERED'),
('Anonymous', NULL, NULL, 'ANONYMOUS');
