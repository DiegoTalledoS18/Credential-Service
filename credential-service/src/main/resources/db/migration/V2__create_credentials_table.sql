CREATE TABLE credentials (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    issuer VARCHAR(100) NOT NULL,
    license_number VARCHAR(100) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(100) NOT NULL,
    deleted_at TIMESTAMP NULL,

    CONSTRAINT fk_user_credential FOREIGN KEY (user_id) REFERENCES users(id)
);
