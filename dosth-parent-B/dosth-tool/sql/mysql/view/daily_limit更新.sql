ALTER TABLE 
	tool.daily_limit 
 DROP COLUMN start_time,
 DROP COLUMN end_time,
 DROP COLUMN borrow_popedom,
 DROP COLUMN popedoms;

DELETE
FROM
	tool.daily_limit
WHERE
	id NOT IN (
		SELECT
			temp.id
		FROM
			(
				SELECT
					min(id) AS id
				FROM
					tool.daily_limit
				GROUP BY
					mat_info_id,
					account_id
				ORDER BY
					op_date DESC
			) AS temp
	)