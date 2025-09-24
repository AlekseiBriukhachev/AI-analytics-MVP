CREATE TABLE dataset (
     id VARCHAR(36) PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     description VARCHAR(1000),
     data_json TEXT,
     feature_names VARCHAR(255),
     record_count INTEGER DEFAULT 0,
     feature_count INTEGER DEFAULT 0,
     created_at TIMESTAMP NOT NULL,
     updated_at TIMESTAMP
);