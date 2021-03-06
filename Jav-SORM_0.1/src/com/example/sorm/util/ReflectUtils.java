package com.example.sorm.util;

import java.lang.reflect.Method;

/**
 * 封装了反射常用的操作
 *
 * @author 北飞的候鸟
 */
@SuppressWarnings("all")
public class ReflectUtils {

	/**
	 * 调用obj对象对应属性fieldName的get方法
	 * 
	 * @param fieldName
	 * @param obj
	 * @return
	 */
	public static Object invokeGet(Object obj, String fieldName) {
		try {
			Class c = obj.getClass();
			Method m = c.getDeclaredMethod("get" + StringUtils.firstChar2UpperCase(fieldName), null);
			return m.invoke(obj, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 调用obj对象对应属性fieldName的set方法
	 * 
	 * @param fieldName
	 * @param obj
	 * @return
	 */
	public static void invokeSet(Object obj, String columnName, Object columnValue) {
		try {
			if(columnName != null){
				Method m = obj.getClass().getDeclaredMethod("set" + StringUtils.firstChar2UpperCase(columnName),
						columnValue.getClass());
				m.invoke(obj, columnValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
