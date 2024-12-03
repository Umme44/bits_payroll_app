INSERT INTO jhi_user (id, login, password_hash, first_name, last_name, email, image_url, activated, lang_key, activation_key, reset_key, created_by, created_date, reset_date, last_modified_by, last_modified_date) VALUES (3, 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Administrator', 'Administrator', 'admin@localhost', '', true, 'en', null, null, 'system', null, null, 'system', null);
INSERT INTO jhi_user_authority (user_id, authority_name) VALUES (3, 'ROLE_ADMIN');
INSERT INTO jhi_user_authority (user_id, authority_name) VALUES (3, 'ROLE_USER');
