CREATE TABLE struct (
    id UUID PRIMARY KEY,
    struct_name VARCHAR(255) NOT NULL,
    struct_data BYTEA,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);