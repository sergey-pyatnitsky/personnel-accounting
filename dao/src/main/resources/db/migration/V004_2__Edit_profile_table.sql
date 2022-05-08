alter table profile
    add column image_id BIGINT DEFAULT 1;
ALTER TABLE profile
    ADD FOREIGN KEY(image_id) REFERENCES image(id);