CREATE USER IF NOT EXISTS 'debezium'@'%' IDENTIFIED BY 'dbz';
GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'debezium'@'%';
FLUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS app_write;
USE app_write;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_users_username (username)
);

CREATE TABLE IF NOT EXISTS authorities (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  role VARCHAR(64) NOT NULL,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_authorities_user_role (user_id, role),
  KEY idx_authorities_user_id (user_id),
  CONSTRAINT fk_authorities_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO users (username, password)
SELECT 'admin','$2a$10$fJ65H/8ihJW40LOI4CAzWuiqp/G.TQs1rzs8RbfiR1avAP9Ty0Tau'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users (username, password)
SELECT 'user', '$2b$10$lNnrCtsLXsPB6RgFvxHbWuCE9jmC4DwUEI3b5DrlLZ72EBPYpGNfS'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user');

INSERT INTO authorities (user_id, role)
SELECT u.id, 'ROLE_ADMIN'
FROM users u
WHERE u.username = 'admin'
  AND NOT EXISTS (
    SELECT 1 FROM authorities a
    WHERE a.user_id = u.id AND a.role = 'ROLE_ADMIN'
  );

INSERT INTO authorities (user_id, role)
SELECT u.id, 'ROLE_USER'
FROM users u
WHERE u.username = 'admin'
  AND NOT EXISTS (
    SELECT 1 FROM authorities a
    WHERE a.user_id = u.id AND a.role = 'ROLE_USER'
  );

INSERT INTO authorities (user_id, role)
SELECT u.id, 'ROLE_USER'
FROM users u
WHERE u.username = 'user'
  AND NOT EXISTS (
    SELECT 1 FROM authorities a
    WHERE a.user_id = u.id AND a.role = 'ROLE_USER'
  );

