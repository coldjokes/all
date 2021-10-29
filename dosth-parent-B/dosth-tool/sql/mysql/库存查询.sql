SELECT
	m.mat_equ_name AS matName,
	m.bar_code AS bar_code,
	m.spec AS spec,
	SUM(s.cur_num) AS curNum
FROM
	tool.equ_detail_sta s
LEFT JOIN tool.mat_equ_info m ON s.mat_info_id = m.id
GROUP BY
	s.mat_info_id