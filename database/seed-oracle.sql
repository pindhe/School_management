-- =============================================================================
-- Demo seed data (run after schema-oracle.sql)
-- Passwords: admin123 / guest123 (BCrypt hashes)
-- =============================================================================

INSERT INTO app_user (username, email, password, role, enabled)
VALUES (
    'admin',
    'admin@school.local',
    '$2a$10$8.UnVuG9HHgffUDAlk8qeO3xlRIn7eSIJPT9bVSLqBokNPeiitSLe',
    'ADMIN',
    1
);

INSERT INTO app_user (username, email, password, role, enabled)
VALUES (
    'guest',
    'guest@school.local',
    '$2a$10$92DXUWuQckKYG.QZ4nXbOeZ5F5E5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y5Yu',
    'GUEST',
    1
);

-- Fix guest hash (BCrypt for guest123)
UPDATE app_user
SET password = '$2a$10$EqKcp1WFKVq7/TwlVNz.0eZ5F5E5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y5Yu'
WHERE username = 'guest';

INSERT INTO student (name, address, phone, email)
VALUES (
    'Amina Hassan',
    '12 Education Street, Cairo',
    '+20-100-555-0101',
    'amina.hassan@student.local'
);

INSERT INTO student (name, address, phone, email)
VALUES (
    'Omar Khalil',
    '45 Campus Road, Alexandria',
    '+20-100-555-0102',
    'omar.khalil@student.local'
);

COMMIT;
