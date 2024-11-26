CREATE TABLE order_status
(
    id              BIGSERIAL PRIMARY KEY,
    order_id        BIGINT                                  NOT NULL,
    previous_status VARCHAR(50),
    new_status      VARCHAR(50)                             NOT NULL,
    updated_by      BIGINT                                  NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE             NOT NULL
);

CREATE TABLE orders
(
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT                                  NOT NULL,
    origin            VARCHAR(255)                            NOT NULL,
    destination       VARCHAR(255)                            NOT NULL,
    type              VARCHAR(255)                            NOT NULL,
    estimated_weight  DOUBLE PRECISION                        NOT NULL,
    additional_detail VARCHAR(255),
    created_at        TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    updated_at        TIMESTAMP WITHOUT TIME ZONE             NOT NULL
);