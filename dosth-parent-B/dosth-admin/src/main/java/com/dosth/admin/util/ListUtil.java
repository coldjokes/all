package com.dosth.admin.util;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * list工具类
 * 
 * @author Yifeng Wang
 */

public class ListUtil {
	/**
	 * list转page
	 * @param <T>
	 * @param list
	 * @param pageable
	 * @return
	 */
	public static <T> Page<T> listConvertToPage(List<T> list, Pageable pageable) {
		int start = (int) pageable.getOffset();
		int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
		return new PageImpl<T>(list.subList(start, end), pageable, list.size());
	}
}
