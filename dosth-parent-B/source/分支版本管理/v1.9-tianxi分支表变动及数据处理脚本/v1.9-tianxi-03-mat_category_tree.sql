DELETE FROM mat_category;
DELETE FROM mat_category_tree;

INSERT INTO mat_category_tree(id, create_date, equ_type, `level`, `name`, p_id, `status`, f_id, f_name) VALUES ('1', NOW(), 'MATTYPE', '0', '类型', '1', 'ENABLE', '1', '类型');
INSERT INTO mat_category_tree(id, create_date, equ_type, `level`, `name`, p_id, `status`, f_id, f_name) VALUES ('2', NOW(), 'REQREF', '0', '设备', '2', 'ENABLE', '2', '设备');
INSERT INTO mat_category_tree(id, create_date, equ_type, `level`, `name`, p_id, `status`, f_id, f_name) VALUES ('3', NOW(), 'PROCREF', '0', '工序', '3', 'ENABLE', '3', '工序');
INSERT INTO mat_category_tree(id, create_date, equ_type, `level`, `name`, p_id, `status`, f_id, f_name) VALUES ('4', NOW(), 'PARTS', '0', '零件', '4', 'ENABLE', '4', '零件');
INSERT INTO mat_category_tree(id, create_date, equ_type, `level`, `name`, p_id, `status`, f_id, f_name) VALUES ('5', NOW(), 'CUSTOM', '0', '自定义', '5', 'ENABLE', '5', '自定义');