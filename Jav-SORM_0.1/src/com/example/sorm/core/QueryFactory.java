package com.example.sorm.core;

/**
 * ����Query����Ĺ�����
 * 
 * @author ���ɵĺ���
 * 
 */

@SuppressWarnings("all")
public class QueryFactory {

	private static Query prototypeObj; // ԭ�Ͷ���

	static {

		try {
			Class c = Class.forName(DBManager.getConf().getQueryClass()); //// ����ָ����query��
			prototypeObj = (Query) c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ����po���������е��࣬�������ã����Ч�ʣ�
		TableContext.loadPOTables();

	}

	private QueryFactory() { // ˽�й�����
	}

	public static Query createQuery() {
		try {
			return (Query) prototypeObj.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

}