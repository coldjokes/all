UPDATE mat_use_record r SET r.equ_setting_name = (SELECT e.equ_setting_name FROM equ_setting e WHERE e.id = '297e5a8667960b070167969f51620013') WHERE r.equ_setting_name is NULL;

UPDATE mat_return_back b SET b.equ_setting_name = (SELECT e.equ_setting_name FROM equ_setting e WHERE e.id = '297e5a8667960b070167969f51620013') WHERE b.equ_setting_name is NULL;