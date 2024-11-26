CREATE TABLE courier_assignment
(
    id               BIGSERIAL PRIMARY KEY,
    order_id         BIGINT                                  NOT NULL,
    courier_id       BIGINT                                  NOT NULL,
    time_window_from TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    time_window_to   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE             NOT NULL
);

ALTER TABLE courier_assignment
    ADD CONSTRAINT FK_COURIER_ASSIGNMENT_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id);