ALTER TABLE `santri_data` ADD COLUMN `tmp_name` VARCHAR(256) NOT NULL DEFAULT 'TMP';
ALTER TABLE `santri_data` ADD COLUMN `tmp_slug` VARCHAR(256) NOT NULL DEFAULT 'TMP';

UPDATE `santri_data` SET santri_data.`tmp_name` = `name`;
UPDATE `santri_data` SET santri_data.`tmp_slug` = `slug`;

ALTER TABLE `santri_data` DROP COLUMN `name`;
ALTER TABLE `santri_data` DROP COLUMN `slug`;

ALTER TABLE `santri_data` ADD COLUMN `name` VARCHAR(256) NOT NULL;
ALTER TABLE `santri_data` ADD COLUMN `slug` VARCHAR(256) NOT NULL;

UPDATE `santri_data` SET santri_data.`name` = santri_data.`tmp_name`;
UPDATE `santri_data` SET santri_data.`slug` = santri_data.`tmp_slug`;

ALTER TABLE `santri_data` DROP COLUMN `tmp_name`;
ALTER TABLE `santri_data` DROP COLUMN `tmp_slug`;

create index santri_data_name_index
    on santri_data (name);

create index santri_data_slug_index
    on santri_data (slug);
