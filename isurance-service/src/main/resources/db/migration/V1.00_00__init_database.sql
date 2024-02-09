CREATE TABLE insurances
(
    id         VARCHAR(100) PRIMARY KEY,

    status     VARCHAR(200) NOT NULL,
    user_name  VARCHAR(200) NOT NULL,
    user_age   INTEGER      NOT NULL,
    auto_brand VARCHAR(200) NOT NULL,

    version    INTEGER      NOT NULL
);