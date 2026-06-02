-- =============================================================================
-- Student Registration System — Oracle Database Schema
-- =============================================================================
-- This project uses exactly TWO tables (by design):
--
--   1. STUDENT   — Assignment table: id, name, address, phone, email, photo
--   2. APP_USER  — Login & registration with roles ADMIN and GUEST
--
-- No extra tables are required for the final project. Roles are stored on
-- APP_USER.ROLE (not a separate role table). Photos are stored as file paths
-- in STUDENT.PHOTO_PATH (uploaded by the Spring Boot API).
-- =============================================================================

-- Drop existing objects (re-run safe)
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE student CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE app_user CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

-- -----------------------------------------------------------------------------
-- Table 1: APP_USER — authentication (login / register)
-- Roles: ADMIN (full CRUD), GUEST (view + search only)
-- -----------------------------------------------------------------------------
CREATE TABLE app_user (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username    VARCHAR2(80)   NOT NULL,
    email       VARCHAR2(120)  NOT NULL,
    password    VARCHAR2(255)  NOT NULL,
    role        VARCHAR2(20)   NOT NULL,
    enabled     NUMBER(1)      DEFAULT 1 NOT NULL,
    created_at  TIMESTAMP      DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT uq_app_user_username UNIQUE (username),
    CONSTRAINT uq_app_user_email    UNIQUE (email),
    CONSTRAINT chk_app_user_role    CHECK (role IN ('ADMIN', 'GUEST')),
    CONSTRAINT chk_app_user_enabled CHECK (enabled IN (0, 1))
);

COMMENT ON TABLE app_user IS 'System users for login and registration';
COMMENT ON COLUMN app_user.role IS 'ADMIN or GUEST';

CREATE INDEX idx_app_user_role ON app_user (role);

-- -----------------------------------------------------------------------------
-- Table 2: STUDENT — student registration (project requirement)
-- -----------------------------------------------------------------------------
CREATE TABLE student (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR2(120)  NOT NULL,
    address     VARCHAR2(255)  NOT NULL,
    phone       VARCHAR2(30)   NOT NULL,
    email       VARCHAR2(120)  NOT NULL,
    photo_path  VARCHAR2(500),
    created_at  TIMESTAMP      DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at  TIMESTAMP,
    CONSTRAINT uq_student_email UNIQUE (email)
);

COMMENT ON TABLE student IS 'Registered students';
COMMENT ON COLUMN student.photo_path IS 'Uploaded photo filename (served by API)';

CREATE INDEX idx_student_name  ON student (name);
CREATE INDEX idx_student_email ON student (email);
CREATE INDEX idx_student_phone ON student (phone);

-- Optional: run seed-oracle.sql after this script for demo data
