CREATE TABLE payments
(
    id          VARCHAR(100) PRIMARY KEY,
    external_id VARCHAR(100),

    status      VARCHAR(200) NOT NULL,

    version     INTEGER      NOT NULL
);