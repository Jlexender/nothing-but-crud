CREATE TABLE struct (
    id CHAR(36) PRIMARY KEY DEFAULT (GEN_RANDOM_UUID()),
    struct_name VARCHAR(255) UNIQUE NOT NULL,
    struct_fields JSON NOT NULL DEFAULT '{}',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
