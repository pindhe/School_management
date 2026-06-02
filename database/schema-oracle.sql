-- =============================================================================
-- Student Registration System — Oracle Database Schema
-- =============================================================================
-- Tables:
--   1. APP_USER      — Login / register (ADMIN, GUEST)
--   2. STUDENT       — Assignment: id, name, address, phone, email, photo
--   3. USER_SETTINGS — Per-user preferences (Settings page)
-- =============================================================================

-- Drop existing objects (re-run safe)
BEGIN EXECUTE IMMEDIATE 'DROP TABLE user_settings CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE student CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE app_user CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
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

-- -----------------------------------------------------------------------------
-- Table 3: USER_SETTINGS — saved preferences per user (Settings page)
-- -----------------------------------------------------------------------------
CREATE TABLE user_settings (
    id                   NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id              NUMBER NOT NULL,
    compact_tables       NUMBER(1) DEFAULT 0 NOT NULL,
    email_notifications  NUMBER(1) DEFAULT 1 NOT NULL,
    updated_at           TIMESTAMP,
    CONSTRAINT fk_user_settings_user FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE,
    CONSTRAINT uq_user_settings_user UNIQUE (user_id),
    CONSTRAINT chk_user_settings_compact CHECK (compact_tables IN (0, 1)),
    CONSTRAINT chk_user_settings_email CHECK (email_notifications IN (0, 1))
);

COMMENT ON TABLE user_settings IS 'Per-user UI preferences';

-- Optional: run seed-oracle.sql after this script for demo students
