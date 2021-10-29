package com.dosth.common.warpper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.data.domain.Page;

import com.dosth.common.constant.IEnumState;

/**
 * 控制器查询结果的包装类基类
 * 
 * @author guozhidong
 *
 */
public abstract class BaseControllerWarpper<T> {

	private Page<T> page;

	public BaseControllerWarpper(Page<T> page) {
		this.page = page;
	}

	@SuppressWarnings("unchecked")
	public Object warp() {
		Map<String, Object> map = new HashedMap();
		List<T> list = this.page.getContent();
		for (T t : list) {
			this.invokeObjToMap(t, map);
		}
		return map;
	}

	/**
	 * 反射对象属性为json
	 * 
	 * @param obj 实体对象
	 * @param map 
	 * @return
	 */
	private Map<String, Object> invokeObjToMap(Object obj, Map<String, Object> map) {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				if (field.getAnnotation(ManyToOne.class) != null) {
					continue;
				}
				field.setAccessible(true);
				map.put(field.getName(), field.get(obj));
				if (field.getAnnotation(Enumerated.class) != null) {
					IEnumState enumState = (IEnumState) field.get(obj);
					map.put(field.getName(), enumState.getMessage());
				}
				if (field.getAnnotation(OneToOne.class) != null || field.getAnnotation(ManyToOne.class) != null) {
					warpTheMap(map);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	protected abstract void warpTheMap(Map<String, Object> map);
}