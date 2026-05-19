-- Author: Ismael
-- Version: 1.0

-- ----------------------------
-- Insert Users
-- ----------------------------

-- Special users
INSERT INTO Users (user_id, name, password, role) VALUES 
(1, 'Anonymous', NULL, 'ANONYMOUS'),    -- Anonymous user created during system installation
(2, 'Quizmael', NULL, 'REGISTERED'),    -- System user for preinstalled tests
(3, 'Deleted User', NULL, 'ANONYMOUS'), -- Placeholder user for data from deleted accounts
(4, 'admin', '7315e4f471363ff0cf0b39122e7e751aa4c95ed9fb76a594ef4bac26709c2a55', 'ADMINISTRATOR'); -- Default administrator user. Password is stored in plain text (not encrypted yet)

-- Adjust the AUTO_INCREMENT counter by reserving the first IDs
ALTER TABLE Users ALTER COLUMN user_id RESTART WITH 10;
