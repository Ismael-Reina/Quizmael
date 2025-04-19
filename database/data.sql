-- ----------------------------
-- Insert Users
-- ----------------------------

-- Usuario anónimo creado al instalar el sistema
INSERT INTO Users (user_id, name, email, password, password_hint, secret_question, secret_answer, role)
VALUES (1, 'Anonymous', 'anonymous@quizmael.com', 'dummy', 'N/A', 'N/A', 'dummy', 'ANONYMOUS');


-- Usuario del sistema para los tests preinstalados
INSERT INTO Users (user_id, name, email, password, password_hint, secret_question, secret_answer, role)
VALUES (2, 'Quizmael', 'quizmael@system.com', 'systempass', 'N/A', 'N/A', 'system', 'ADMINISTRATOR');

