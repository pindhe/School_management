-- =============================================================================
-- Demo seed data — STUDENT rows only (run after schema-oracle.sql)
--
-- APP_USER accounts: create via the application on first startup (DataSeeder)
-- or register at POST /api/auth/register — do not insert plain passwords here.
-- =============================================================================

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
