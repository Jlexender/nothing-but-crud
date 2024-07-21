CREATE TABLE struct
(
    id            UUID PRIMARY KEY             DEFAULT (GEN_RANDOM_UUID()),
    struct_name   VARCHAR(255) UNIQUE NOT NULL,
    struct_fields TEXT                NOT NULL DEFAULT '{}',
    created_at    TIMESTAMP                    DEFAULT CURRENT_TIMESTAMP
);
