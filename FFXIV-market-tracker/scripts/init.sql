CREATE TABLE IF NOT EXISTS items (
    item_id     BIGINT PRIMARY KEY,
    item_name   VARCHAR(255),
    icon_url    VARCHAR(255),
    can_be_hq   BOOLEAN,
    stack_size  INTEGER,
    vendor_price INTEGER,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users (
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(255),
    email       VARCHAR(255),
    home_world  VARCHAR(255),
    password_hash VARCHAR(255),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS price_snapshots (
    id              BIGSERIAL PRIMARY KEY,
    item_id         BIGINT REFERENCES items(item_id),
    world           VARCHAR(255),
    is_hq           BOOLEAN,
    avg_price       NUMERIC,
    min_price       NUMERIC,
    max_price       NUMERIC,
    avg_price_hq    NUMERIC,
    avg_price_nq    NUMERIC,
    listing_count   INTEGER,
    volume_sold     INTEGER,
    last_upload_time BIGINT,
    captured_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS price_alerts (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT REFERENCES users(id),
    item_id         BIGINT REFERENCES items(item_id),
    world           VARCHAR(255),
    condition       VARCHAR(10),
    target_price    NUMERIC,
    is_hq           BOOLEAN,
    is_active       BOOLEAN DEFAULT TRUE,
    last_triggered_at TIMESTAMP,
    trigger_count   INTEGER DEFAULT 0,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);