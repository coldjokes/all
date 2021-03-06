package com.dosth.common.db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

/**
 * 查询条件容器
 * 
 * @author guozhidong
 *
 * @param <T>
 */
public class Criteria<T> implements Specification<T> {

	private List<Criterion> criterions = new ArrayList<>();

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (!criterions.isEmpty()) {
			List<Predicate> predicates = new ArrayList<>();
			for (Criterion c : criterions) {
				predicates.add(c.toPredicate(root, query, builder));
			}
			// 将所有条件用and联合起来
			if (predicates.size() > 0) {
				return builder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}
		return null;
	}

	/**
	 * 添加简单条件表达式
	 * 
	 * @param criterion
	 */
	public void add(Criterion criterion) {
		if (criterion != null) {
			criterions.add(criterion);
		}
	}
}