package com.example.sorm.core;

/**
 * mysql数据类型和java数据类型的转换
 * 
 * @author 北飞的候鸟
 */
public class MySqlTypeConvertor implements TypeConvertor {

	@Override
	public String databaseType2JavaType(String columnType) {

		if ("varchar".equalsIgnoreCase(columnType)) {
			return "String";
		} else if ("char".equalsIgnoreCase(columnType)) {
			return "String";
		} else if ("tinyint".equalsIgnoreCase(columnType)) {
			return "Integer";
		} else if ("smallint".equalsIgnoreCase(columnType)) {
			return "Integer";
		} else if ("integer".equalsIgnoreCase(columnType)) {
			return "Integer";
		} else if ("int".equalsIgnoreCase(columnType)) {
			return "Integer";
		} else if ("bigint".equalsIgnoreCase(columnType)) {
			return "Long";
		} else if ("double".equalsIgnoreCase(columnType)) {
			return "Double";
		} else if ("float".equalsIgnoreCase(columnType)) {
			return "Double";
		} else if ("clob".equalsIgnoreCase(columnType)) {
			return "java.sql.CLob";
		} else if ("blob".equalsIgnoreCase(columnType)) {
			return "java.sql.BLob";
		} else if ("date".equalsIgnoreCase(columnType)) {
			return "java.sql.Date";
		} else if ("datetime".equalsIgnoreCase(columnType)) {
			return "java.sql.Date";
		} else if ("time".equalsIgnoreCase(columnType)) {
			return "java.sql.Time";
		} else if ("timestamp".equalsIgnoreCase(columnType)) {
			return "java.sql.Timestamp";
		}else if ("INT UNSIGNED".equalsIgnoreCase(columnType)) {
			return "Integer";
		}
		/**
		 * 
		 * 如有其它未申明的类型终止程序运行
		 *
		 */
		try {
			throw new Error("mysql数据转换成java类型错误，"
					+"已终止程序运行\n"
					+ "请检查是否有未申明的mysql数据类型");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String javaType2DatabaseType(String javaDataType) {
		return null;
	}

}
