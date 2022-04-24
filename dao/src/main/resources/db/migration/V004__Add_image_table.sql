CREATE TABLE image
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    fileName VARCHAR(255)          NULL,
    fileType VARCHAR(255)          NULL,
    data     BLOB                  NULL,
    CONSTRAINT pk_image PRIMARY KEY (id)
);