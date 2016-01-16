package com.example.sorm.core;

import java.util.List;

/**
 * �����ѯ�������ṩ����ĺ����ࣩ
 * @author ���ɵĺ���
 * 
 */
@SuppressWarnings("all")
public interface Query {

	/**
	 * ֱ��ִ��һ��DML���
	 * @param sql sql���
	 * @param params ����
	 * @return ִ��sql����Ӱ���¼������
	 */
	public int executeDML(String sql,Object[] params);
	
	/**
	 * ��һ������洢�����ݿ���
	 * �Ѷ����в�Ϊnull�����������ݿ��д洢���������Ϊnull���0.
	 * @param obj Ҫ�洢�Ķ���
	 */
	public void insert(Object obj);
	
	/**
	 * ɾ��clazz��ʾ���Ӧ�ı��еļ�¼(ָ������ֵid�ļ�¼)
	 * @param clazz �����Ӧ�����Class����
	 * @param id ������ֵ
	 */
	public void delete(Class clazz,Object id);   // delete from User where id=2;
	/**
	 * ɾ�����������ݿ��ж�Ӧ�ļ�¼(�������ڵ����Ӧ���������������ֵ��Ӧ����¼)
	 * @param obj
	 */
	public void delete(Object obj);
	
	/**
	 * ���¶����Ӧ�ļ�¼������ֻ����ָ�����ֶε�ֵ
	 * @param obj ��Ҫ���µĶ���
	 * @param fieldNames ���µ������б�
	 * @return ִ��sql����Ӱ���¼������
	 */
	public int update(Object obj,String[] fieldNames);  //update user set uname=?,pwd=?
	
	/**
	 * ��ѯ���ض��м�¼������ÿ�м�¼��װ��clazzָ������Ķ�����
	 * @param sql ��ѯ���
	 * @param clazz ��װ���ݵ�javabean���Class����
	 * @param params sql�Ĳ���
	 * @return ��ѯ���Ľ��
	 */
	public List queryRows(String sql,Class clazz,Object[] params);
	
	/**
	 * ��ѯ����һ�м�¼�������ü�¼��װ��clazzָ������Ķ�����
	 * @param sql ��ѯ���
	 * @param clazz ��װ���ݵ�javabean���Class����
	 * @param params sql�Ĳ���
	 * @return ��ѯ���Ľ��
	 */
	public Object queryUniqueRow(String sql,Class clazz,Object[] params);
	
	/**
	 * ��ѯ����һ��ֵ(һ��һ��)��������ֵ����
	 * @param sql ��ѯ���
	 * @param params sql�Ĳ���
	 * @return ��ѯ���Ľ��
	 */
	public Object queryValue(String sql,Object[] params);
	
	/**
	 * ��ѯ����һ������(һ��һ��)��������ֵ����
	 * @param sql ��ѯ���
	 * @param params sql�Ĳ���
	 * @return ��ѯ��������
	 */
	public Number queryNumber(String sql,Object[] params);
	
	
}
