package com.example.sorm.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.example.pool.DBConnPool;
import com.example.sorm.bean.Configuration;

/**
 * ����������Ϣ��ά�����Ӷ���Ĺ���(�������ӳع���)
 * 
 * @author ���ɵĺ���
 * 
 */
public class DBManager {
	/**
	 * ������Ϣ
	 */
	private static Configuration conf;
	/**
	 * ���ӳض���
	 */
	private static DBConnPool pool;

	static { // ��̬�����
		Properties pros = new Properties();
		try {
			pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		conf = new Configuration();
		conf.setDriver(pros.getProperty("driver"));
		conf.setPoPackage(pros.getProperty("poPackage"));
		conf.setPwd(pros.getProperty("pwd"));
		conf.setSrcPath(pros.getProperty("srcPath"));
		conf.setUrl(pros.getProperty("url"));
		conf.setUser(pros.getProperty("user"));
		conf.setUsingDB(pros.getProperty("usingDB"));
		conf.setQueryClass(pros.getProperty("queryClass"));
		conf.setPoolMaxSize(Integer.parseInt(pros.getProperty("poolMaxSize")));
		conf.setPoolMinSize(Integer.parseInt(pros.getProperty("poolMinSize")));

	}

	/**
	 * ���Connection����
	 * 
	 * @return
	 */
	public static Connection getConn() {
		if (pool == null) {
			pool = new DBConnPool();
		}
		return pool.getConnection();
	}

	/**
	 * �����µ�Connection����
	 * 
	 * @return
	 */
	public static Connection createConn() {
		try {
			Class.forName(conf.getDriver());
			return DriverManager.getConnection(conf.getUrl(), conf.getUser(), conf.getPwd()); // ֱ�ӽ������ӣ������������ӳش��������Ч�ʣ�����
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �رմ����ResultSet��Statement��Connection����
	 * 
	 * @param rs
	 * @param ps
	 * @param conn
	 */
	public static void close(ResultSet rs, Statement ps, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pool.close(conn);

	}

	/**
	 * �رմ����Statement��Connection����
	 * 
	 * @param ps
	 * @param conn
	 */
	public static void close(Statement ps, Connection conn) {
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pool.close(conn);

	}

	public static void close(Connection conn) {
		pool.close(conn);

	}

	/**
	 * ����Configuration����
	 * 
	 * @return
	 */
	public static Configuration getConf() {
		return conf;
	}

}