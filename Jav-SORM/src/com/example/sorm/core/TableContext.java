package com.example.sorm.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.sorm.bean.ColumnInfo;
import com.example.sorm.bean.TableInfo;
import com.example.sorm.util.JavaFileUtils;
import com.example.sorm.util.StringUtils;


/**
 * �����ȡ�������ݿ����б��ṹ����ṹ�Ĺ�ϵ�������Ը��ݱ��ṹ������ṹ��
 *
 */
public class TableContext {

	/**
	 * ����Ϊkey������Ϣ����Ϊvalue
	 */
	public static  Map<String,TableInfo>  tables = new HashMap<String,TableInfo>();
	
	/**
	 * ��po��class����ͱ���Ϣ��������������������ã�
	 */
	public static  Map<Class,TableInfo>  poClassTableMap = new HashMap<Class,TableInfo>();
	
	private TableContext(){}
	
	static {
		try {
			//��ʼ����ñ�����Ϣ
			Connection con = DBManager.getConn();
			DatabaseMetaData dbmd = con.getMetaData(); 
			
			ResultSet tableRet = dbmd.getTables(null, "%","%",new String[]{"TABLE"}); 
			
			while(tableRet.next()){
				String tableName = (String) tableRet.getObject("TABLE_NAME");
				
				TableInfo ti = new TableInfo(tableName, new ArrayList<ColumnInfo>()
						,new HashMap<String, ColumnInfo>());
				tables.put(tableName, ti);
				
				ResultSet set = dbmd.getColumns(null, "%", tableName, "%");  //��ѯ���е������ֶ�
				while(set.next()){
					ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"), 
							set.getString("TYPE_NAME"), 0);
					ti.getColumns().put(set.getString("COLUMN_NAME"), ci);
				}
				
				ResultSet set2 = dbmd.getPrimaryKeys(null, "%", tableName);  //��ѯt_user���е�����
				while(set2.next()){
					ColumnInfo ci2 = (ColumnInfo) ti.getColumns().get(set2.getObject("COLUMN_NAME"));
					ci2.setKeyType(1);  //����Ϊ��������
					ti.getPriKeys().add(ci2);
				}
				
				if(ti.getPriKeys().size()>0){  //ȡΨһ������������ʹ�á������������������Ϊ�գ�
					ti.setOnlyPriKey(ti.getPriKeys().get(0));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//������ṹ
		updateJavaPOFile();
		
		//����po���������е��࣬�������ã����Ч�ʣ�
		loadPOTables();
	}
	
	/**
	 * ���ݱ��ṹ���������õ�po�������java��
	 * ʵ���˴ӱ��ṹת������ṹ
	 */
	public static void updateJavaPOFile(){
		Map<String,TableInfo> map = TableContext.tables;
		for(TableInfo t:map.values()){
			JavaFileUtils.createJavaPOFile(t,new MySqlTypeConvertor());
		}	
	}
	
	/**
	 * ����po���������
	 */
	public static void loadPOTables(){
		
		for(TableInfo tableInfo:tables.values()){
			try {
				Class c = Class.forName(DBManager.getConf().getPoPackage()
						+"."+StringUtils.firstChar2UpperCase(tableInfo.getTname()));
				poClassTableMap.put(c, tableInfo);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public static void main(String[] args) {
		 Map<String,TableInfo>  tables = TableContext.tables;
		 System.out.println(tables);
	}

}