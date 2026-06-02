-- Oracle schema for Student Registration System (assignment requirement)

CREATE TABLE app_user (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username    VARCHAR2(80)  NOT NULL UNIQUE,
    email       VARCHAR2(120) NOT NULL UNIQUE,
    password    VARCHAR2(255) NOT NULL,
    role        VARCHAR2(20)  NOT NULL CHECK (role IN ('ADMIN', 'GUEST')),
    enabled     NUMBER(1)     DEFAULT 1 NOT NULL,
    created_at  TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL
);

CREATE TABLE student (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR2(120) NOT NULL,
    address     VARCHAR2(255) NOT NULL,
    phone       VARCHAR2(30)  NOT NULL,
    email       VARCHAR2(120) NOT NULL UNIQUE,
    photo_path  VARCHAR2(500),
    created_at  TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at  TIMESTAMP
);

CREATE INDEX idx_student_name ON student (name);
CREATE INDEX idx_student_email ON student (email);
