CREATE TABLE IF NOT EXISTS gender (
    id    INT PRIMARY KEY AUTO_INCREMENT,
    code  CHAR(1)     NOT NULL UNIQUE,
    label VARCHAR(20) NOT NULL
) DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS patient (
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    birth_date DATE         NOT NULL,
    gender_id  INT          NOT NULL,
    address    VARCHAR(255),
    phone      VARCHAR(20),
    FOREIGN KEY (gender_id) REFERENCES gender (id),
    CONSTRAINT uk_patient_identity UNIQUE (first_name, last_name, birth_date)
) DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
