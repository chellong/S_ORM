package com.example.sorm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.sorm.bean.ColumnInfo;
import com.example.sorm.bean.TableInfo;
import com.example.sorm.util.JDBCUtils;
import com.example.sorm.util.ReflectUtils;

/**
 * �����ѯ�������ṩ����ĺ����ࣩ
 * 
 * @author ���ɵĺ���
 * 
 */
@SuppressWarnings("all")
public abstract class Query implements Cloneable {

	/**
	 * ����ģ�巽��ģʽ��JDBC������װ��ģ�壬��������
	 * 
	 * @param sql
	 *            sql���
	 * @param params
	 *            sql�Ĳ���
	 * @param clazz
	 *            ��¼Ҫ��װ����java��
	 * @param back
	 *            CallBack��ʵ���࣬ʵ�ֻص�
	 * @return
	 */
	public Object executeQueryTemplate(String sql, Object[] params, Class clazz, CallBack back) {
		Connection conn = DBManager.getConn();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			// ��sql���
			JDBCUtils.handleParams(ps, params);
			System.out.println(ps);
			rs = ps.executeQuery();

			return back.doExecute(conn, ps, rs);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DBManager.close(ps, conn);
		}
	}

	/**
	 * ֱ��ִ��һ��DML���
	 * 
	 * @param sql
	 *            sql���
	 * @param params
	 *            ����
	 * @return ִ��sql����Ӱ���¼������
	 */
	public int executeDML(String sql, Object[] params) {
		Connection conn = DBManager.getConn();
		int count = 0;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			// ��sql���
			JDBCUtils.handleParams(ps, params);
			System.out.println(ps);

			count = ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(ps, conn);
		}

		return count;

	}

	/**
	 * ��һ������洢�����ݿ��� �Ѷ����в�Ϊnull�����������ݿ��д洢���������Ϊnull���0.
	 * 
	 * @param obj
	 *            Ҫ�洢�Ķ���
	 */
	public void insert(Object obj) {

		// obj-->���С� insert into ���� (id,uname,pwd) values (?,?,?)
		Class c = obj.getClass();
		List<Object> params = new ArrayList<Object>(); // �洢sql�Ĳ�������
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		StringBuilder sql = new StringBuilder("insert into " + tableInfo.getTname() + " (");
		int countNotNullField = 0; // ���㲻Ϊnull������ֵ
		Field[] fs = c.getDeclaredFields();
		for (Field f : fs) {
			String fieldName = f.getName();
			Object fieldValue = ReflectUtils.invokeGet(obj, fieldName);

			if (fieldValue != null) {
				countNotNullField++;
				sql.append(fieldName + ",");
				params.add(fieldValue);
			}
		}

		sql.setCharAt(sql.length() - 1, ')');
		sql.append(" values (");
		for (int i = 0; i < countNotNullField; i++) {
			sql.append("?,");
		}
		sql.setCharAt(sql.length() - 1, ')');

		executeDML(sql.toString(), params.toArray());

	}

	/**
	 * ɾ��clazz��ʾ���Ӧ�ı��еļ�¼(ָ������ֵid�ļ�¼)
	 * 
	 * @param clazz
	 *            ������Ӧ�����Class����
	 * @param id
	 *            ������ֵ
	 */
	public void delete(Class clazz, Object id) {
		// Emp.class,2-->delete from emp where id=2
		// ͨ��Class������TableInfo
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		// �������
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();

		String sql = "delete from " + tableInfo.getTname() + " where " + onlyPriKey.getName() + "=? ";

		executeDML(sql, new Object[] { id });

	}

	/**
	 * ɾ�����������ݿ��ж�Ӧ�ļ�¼(�������ڵ����Ӧ�����������������ֵ��Ӧ����¼)
	 * 
	 * @param obj
	 */
	public void delete(Object obj) {
		Class c = obj.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey(); // ����

		// ͨ��������ƣ��������Զ�Ӧ��get������set����
		Object priKeyValue = ReflectUtils.invokeGet(obj, onlyPriKey.getName());

		delete(c, priKeyValue);

	}

	/**
	 * ���¶����Ӧ�ļ�¼������ֻ����ָ�����ֶε�ֵ
	 * 
	 * @param obj
	 *            ��Ҫ���µĶ���
	 * @param fieldNames
	 *            ���µ������б�
	 * @return ִ��sql����Ӱ���¼������
	 */
	public int update(Object obj, String[] fieldNames) {

		// obj{"uanme","pwd"}-->update ���� set uname=?,pwd=? where id=?
		Class c = obj.getClass();
		List<Object> params = new ArrayList<Object>(); // �洢sql�Ĳ�������
		TableInfo tableInfo = TableContext.poClassTableMap.get(c);
		ColumnInfo priKey = tableInfo.getOnlyPriKey(); // ���Ψһ������
		StringBuilder sql = new StringBuilder("update " + tableInfo.getTname() + " set ");

		for (String fname : fieldNames) {
			Object fvalue = ReflectUtils.invokeGet(obj, fname);
			params.add(fvalue);
			sql.append(fname + "=?,");
		}
		sql.setCharAt(sql.length() - 1, ' ');
		sql.append(" where ");
		sql.append(priKey.getName() + "=? ");

		params.add(ReflectUtils.invokeGet(obj, priKey.getName())); // ������ֵ

		return executeDML(sql.toString(), params.toArray());

	}

	/**
	 * ��ѯ���ض��м�¼������ÿ�м�¼��װ��clazzָ������Ķ�����
	 * 
	 * @param sql
	 *            ��ѯ���
	 * @param clazz
	 *            ��װ���ݵ�javabean���Class����
	 * @param params
	 *            sql�Ĳ���
	 * @return ��ѯ���Ľ��
	 */
	public List queryRows(final String sql, final Class clazz, final Object[] params) {

		return (List) executeQueryTemplate(sql, params, clazz, new CallBack() {

			@Override
			public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
				List list = null;
				try {
					ResultSetMetaData metaData = rs.getMetaData();
					// ����
					while (rs.next()) {
						if (list == null) {
							list = new ArrayList();
						}

						Object rowObj = clazz.newInstance(); // ����javabean���޲ι�����

						// ���� select username ,pwd,age from user where id>? and
						// age>18
						for (int i = 0; i < metaData.getColumnCount(); i++) {
							String columnName = metaData.getColumnLabel(i + 1); // username
							Object columnValue = rs.getObject(i + 1);

							// ����rowObj�����setUsername(String
							// uname)��������columnValue��ֵ���ý�ȥ
							ReflectUtils.invokeSet(rowObj, columnName, columnValue);
						}

						list.add(rowObj);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return list;
			}
		});

	}

	/**
	 * ��ѯ����һ�м�¼�������ü�¼��װ��clazzָ������Ķ�����
	 * 
	 * @param sql
	 *            ��ѯ���
	 * @param clazz
	 *            ��װ���ݵ�javabean���Class����
	 * @param params
	 *            sql�Ĳ���
	 * @return ��ѯ���Ľ��
	 */
	public Object queryUniqueRow(String sql, Class clazz, Object[] params) {
		List list = queryRows(sql, clazz, params);
		return (list != null && list.size() > 0) ? list.get(0) : null;
	}

	/**
	 * ����������ֱֵ�Ӳ��Ҷ�Ӧ�Ķ���
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	public Object queryById(Class clazz, Object id) {
		// select * from emp where id=? //delete from emp where id=?
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		// �������
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		String sql = "select * from " + tableInfo.getTname() + " where " + onlyPriKey.getName() + "=? ";
		return queryUniqueRow(sql, clazz, new Object[] { id });
	}

	/**
	 * ��ѯ����һ��ֵ(һ��һ��)��������ֵ����
	 * 
	 * @param sql
	 *            ��ѯ���
	 * @param params
	 *            sql�Ĳ���
	 * @return ��ѯ���Ľ��
	 */
	public Object queryValue(String sql, Object[] params) {
		return executeQueryTemplate(sql, params, null, new CallBack() {
			@Override
			public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
				Object value = null;
				try {
					while (rs.next()) {
						value = rs.getObject(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return value;
			}
		});
	}

	/**
	 * ��ѯ����һ������(һ��һ��)��������ֵ����
	 * 
	 * @param sql
	 *            ��ѯ���
	 * @param params
	 *            sql�Ĳ���
	 * @return ��ѯ��������
	 */
	public Number queryNumber(String sql, Object[] params) {
		return (Number) queryValue(sql, params);
	}

	/**
	 * ��ҳ��ѯ
	 * 
	 * @param pageNum
	 *            �ڼ�ҳ����
	 * @param size
	 *            ÿҳ��ʾ���ټ�¼
	 * @return
	 */
	public abstract Object queryPagenate(int pageNum, int size);

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}