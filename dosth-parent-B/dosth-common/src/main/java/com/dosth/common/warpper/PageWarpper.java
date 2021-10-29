package com.dosth.common.warpper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.data.domain.Page;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.IEnumState;
import com.dosth.common.constant.TableSelectType;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.common.util.DateUtil;
import com.dosth.common.warpper.bootstraptable.Columns;

/**
 * 分页查询结果的包装类基类
 * 
 * @author guozhidong
 *
 * @param <T>
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class PageWarpper<T> {

	public final Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
			.getActualTypeArguments()[0];

	private Page<T> page;

	public PageWarpper(Page<T> page) {
		this.page = page;
	}
	
	/**
	 * 创建表头
	 * 
	 * @param type 表格选中方式
	 * @param tableParams 表格参数, tableParams[0] id是否隐藏 
	 * 
	 * @return
	 */
	public List<Columns> createColumns(TableSelectType type, Boolean... tableParams) {
		List<Columns> columns = new LinkedList<>();
		if (tableParams == null || tableParams.length == 0 || (tableParams.length > 0 && !tableParams[0])) {
			columns.add(new Columns(type == null ? "radio" : type.getType(), "", true));
		}
		Field[] fields;
		PageTableTitle title;
		Class clazz = entityClass.getSuperclass();
		while (true) {
			fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				title = field.getAnnotation(PageTableTitle.class);
				if (title != null) {
					columns.add(new Columns(field.getName(), title.value(), title.isVisible()));
				}
			}
			if (clazz.getName().equals(BasePojo.class.getName())) {
				break;
			}
			clazz = clazz.getSuperclass();
		}
		
		fields = entityClass.getDeclaredFields();
		for (Field field : fields) {
			title = field.getAnnotation(PageTableTitle.class);
			if (title != null) {
				if ("id".equals(title.value()) && tableParams != null && tableParams.length != 0 && tableParams.length > 0 && tableParams[0]) {
					continue;
				}
				columns.add(new Columns(field.getName(), title.value(), title.isVisible()));
			}
		}
		return columns;
	}

	/**
	 * 反射对象属性为json
	 * 
	 * @param obj
	 *            实体对象
	 * @param map
	 * @return
	 */
	public Map<String, Object> invokeObjToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("total", this.page.getTotalElements());
		map.put("rows", invokePageContent(this.page.getContent()));
		return map;
	}

	/**
	 * 处理分页数据中列表数据
	 * 
	 * @param content
	 * @return
	 */
	private List<Map<String, Object>> invokePageContent(List<T> content) {
		List<Map<String, Object>> list = new ArrayList<>();
		PageTableTitle pageTableTitle;
		Object fieldObj = null;
		Map<String, Object> map;
		IEnumState enumState;
		Field[] fields;
		for (T obj : content) {
			map = new HashMap<>();
			fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				try {
					pageTableTitle = field.getAnnotation(PageTableTitle.class);
					if (pageTableTitle != null) {
						field.setAccessible(true);
						fieldObj = field.get(obj);
						map.put(field.getName(), fieldObj == null ? "" : fieldObj);
						if (fieldObj == null) {
							continue;
						} 
						if (pageTableTitle.isForeign()) {
							warpTheMap(map, field.getName(), fieldObj);
							continue;
						}
						if (field.getAnnotation(Temporal.class) != null) {
							map.put(field.getName(), DateUtil.getTime((Date) fieldObj));
							if (field.getAnnotation(Temporal.class).value().equals(TemporalType.DATE)) {
								map.put(field.getName(), DateUtil.getDay((Date) fieldObj));
							}
							continue;
						} 
						if (field.getAnnotation(Enumerated.class) != null) {
							// 非持久化的枚举类,原型输入个性化处理
							if (field.getAnnotation(Transient.class) != null) {
								map.put(field.getName(), fieldObj);
								warpTheMap(map, field.getName(), fieldObj);
							} else {
								if (pageTableTitle.isEnum()) {
									enumState = (IEnumState) fieldObj;
									map.put(field.getName(), enumState.getMessage());
								} else {
									warpTheMap(map, field.getName(), fieldObj);
								}
							}
							continue;
						}
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			Class clazz = entityClass.getSuperclass();
			while (true) {
				fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					try {
						pageTableTitle = field.getAnnotation(PageTableTitle.class);
						if (pageTableTitle != null) {
							field.setAccessible(true);
							fieldObj = field.get(obj);
							map.put(field.getName(), fieldObj == null ? "" : fieldObj);
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				if (clazz.getName().equals(BasePojo.class.getName())) {
					break;
				}
				clazz = clazz.getSuperclass();
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 设置分页列表数据
	 * 
	 * @param map
	 *            分页数据
	 * @param key
	 *            列表键值
	 * @param obj
	 *            值
	 */
	protected abstract void warpTheMap(Map<String, Object> map, String key, Object obj);
}