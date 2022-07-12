ALTER TABLE profile DROP FOREIGN KEY profile_ibfk_1;
ALTER TABLE profile MODIFY COLUMN image_id varchar(45) DEFAULT '1oRfzcWiifuIhZOh4h5eqVU2REr1G_EQ-';
UPDATE profile set image_id = '1oRfzcWiifuIhZOh4h5eqVU2REr1G_EQ-';
DROP TABLE image;