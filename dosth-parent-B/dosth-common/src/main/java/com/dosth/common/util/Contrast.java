package com.dosth.common.util;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dosth.common.constant.IEnumState;
import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;

/**
 * 对比两个对象的变化的工具类
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("static-access")
public class Contrast {

	/**
	 * 实体包路径
	 */
	private static String packagePath;

	public Contrast(String packagePath) {
		this.packagePath = packagePath;
	}

	public static Contrast getInstance(String packagePath) {
		return new Contrast(packagePath);
	}

	/**
	 * 创建实体与参数集合对应修改值信息
	 * 
	 * @param obj
	 *            实体
	 * @param parameters
	 *            参数集合
	 * @return
	 */
	public String contrastObj(Object obj, Map<String, String> parameters) {
		Class<?> clazz = obj.getClass();
		Field[] fields;
		PageTableTitle pageTableTitle;
		StringBuffer msg = new StringBuffer();
		Object oldObj = null;
		Object tmpOld = null;
		String newObj = null;
		Field tmpField = null;
		while (true) {
			fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				pageTableTitle = field.getDeclaredAnnotation(PageTableTitle.class);
				if (pageTableTitle == null) {
					continue;
				}
				field.setAccessible(true);
				try {
					oldObj = field.get(obj);
					tmpOld = oldObj;
					newObj = parameters.get(field.getName());
					if (pageTableTitle.isEnum()) {
						tmpOld = ((IEnumState) oldObj).getMessage();
						newObj = ((IEnumState) oldObj).valueOfName(newObj);
					} else if (pageTableTitle.isForeign()) {
						try {
							tmpField = oldObj.getClass().getSuperclass().getDeclaredField("id");
							tmpField.setAccessible(true);
							tmpOld = tmpField.get(oldObj);
						} catch (Exception e) {
							tmpField = obj.getClass().getDeclaredField(field.getName() + "Id");
							tmpField.setAccessible(true);
							tmpOld = tmpField.get(obj);
						}
						newObj = parameters.get(field.getName() + "Id");
					}
					if (String.valueOf(tmpOld).equals(newObj)) {
						continue;
					}
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e1) {
					e1.printStackTrace();
				}
				msg.append("字段名称:");
				msg.append(pageTableTitle.value());
				msg.append(",旧值:");
				try {
					if (pageTableTitle.isEnum()) {
						msg.append(((IEnumState) oldObj).getMessage());
					} else if (pageTableTitle.isForeign()) {
						try {
							tmpField = oldObj.getClass().getSuperclass().getDeclaredField("id");
							tmpField.setAccessible(true);
							msg.append(tmpField.get(oldObj));
						} catch (Exception e) {
							tmpField = obj.getClass().getDeclaredField(field.getName() + "Id");
							tmpField.setAccessible(true);
							msg.append(tmpOld = tmpField.get(obj));
						}
					} else if (oldObj instanceof Date) {
						if (field.isAnnotationPresent(Temporal.class)) {
							if (field.getAnnotation(Temporal.class).value().equals(TemporalType.DATE)) {
								msg.append(DateUtil.getDay((Date) field.get(obj)));
							} else if (field.getAnnotation(Temporal.class).value().equals(TemporalType.TIMESTAMP)) {
								msg.append(DateUtil.getTime((Date) field.get(obj)));
							}
						}
					} else {
						msg.append(oldObj);
					}
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					msg.append("NULL");
					e.printStackTrace();
				}
				msg.append(",新值:");
				if (pageTableTitle.isEnum()) {
					msg.append(newObj);
				} else if (pageTableTitle.isForeign()) {
					msg.append(parameters.get(field.getName() + "Id"));
				} else {
					msg.append(parameters.get(field.getName()));
				}
				msg.append(";");
			}
			if (clazz.getName().equals(BasePojo.class.getName())) {
				break;
			}
			clazz = clazz.getSuperclass();
		}
		return msg.length() > 0 ? msg.toString() : "数据无变更信息";
	}

	/**
	 * 创建新建实体信息
	 * 
	 * @param params
	 *            修改实体集合
	 * @param parameters
	 *            参数集合
	 * @return
	 */
	public String parseMutiKey(Object[] params, Map<String, String> parameters) {
		Field[] fields;
		PageTableTitle pageTableTitle;
		Class<?> clazz;
		StringBuffer msg = new StringBuffer();
		for (Object obj : params) {
			clazz = obj.getClass();
			if (clazz.getName().indexOf(packagePath) == -1) {
				continue;
			}
			while (true) {
				fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					pageTableTitle = field.getDeclaredAnnotation(PageTableTitle.class);
					if (pageTableTitle == null) {
						continue;
					}
					field.setAccessible(true);
					msg.append("字段名称：");
					msg.append(pageTableTitle.value());
					msg.append(",新值:");

					if (pageTableTitle.isEnum()) {
						msg.append(((IEnumState) obj).valueOfName(parameters.get(field.getName())));
					} else if (pageTableTitle.isForeign()) {
						msg.append(parameters.get(field.getName() + "Id"));
					} else {
						msg.append(parameters.get(field.getName()));
					}
					msg.append(";");
				}
				if (clazz.getName().equals(BasePojo.class.getName())) {
					break;
				}
				clazz = clazz.getSuperclass();
			}
		}
		return msg.toString();
	}
}