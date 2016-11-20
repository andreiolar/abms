CREATE OR REPLACE VIEW
	CONTACT_INFO
AS SELECT
	last_name AS family_name,
	concat(first_name, ' ', last_name) AS contact_person,
	apartment_number,
	email,
	mobile_number
FROM user_info;