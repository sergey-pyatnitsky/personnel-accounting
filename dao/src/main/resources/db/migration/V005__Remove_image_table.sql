ALTER TABLE profile DROP FOREIGN KEY profile_ibfk_1;
ALTER TABLE profile MODIFY COLUMN image_id varchar(45) DEFAULT '1ohOOokl9RpEE27HIrtsxj6dXdxnPQnmo';
UPDATE profile set image_id = '1ohOOokl9RpEE27HIrtsxj6dXdxnPQnmo';
DROP TABLE image;