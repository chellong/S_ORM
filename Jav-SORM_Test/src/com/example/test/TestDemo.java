package com.example.test;

import java.sql.Date;
import java.util.List;

import com.example.po.Emp;
import com.example.sorm.core.Query;
import com.example.sorm.core.QueryFactory;
import com.example.sorm.core.TableContext;
import com.example.vo.EmpVO;

/**
 * 測試
 * @author 北飞的候鸟
 *
 */
@SuppressWarnings("all")
public class TestDemo {
	
	/**
	 * 通过这个方法可以生成po类！
	 */
	public static void init() {
		TableContext.updateJavaPOFile();
	}
	
	public static void add(){
		Emp e = new Emp();
		e.setId(5);
		e.setAge(18);
		e.setEmname("name");
		e.setBirthday(new Date(System.currentTimeMillis()));
		e.setSalary(2000.0);
		e.setDeptId(1);
		Query q  = QueryFactory.createQuery();
		q.insert(e);
	}
	public static void delete(){
		Emp e = new Emp();
		e.setAge(18);
		e.setEmname("name");
		e.setSalary(2000.0);
		e.setId(3);
		Query q  = QueryFactory.createQuery();
		q.delete(e);
	}
	public static void update(){
		Emp e = new Emp();
		e.setAge(180);
		e.setEmname("name");
		e.setSalary(200.0);
		e.setId(1);
		Query q  = QueryFactory.createQuery();
		q.update(e, new String[]{"age","salary"});
	}
	
	public static void select01(){
		Query q  = QueryFactory.createQuery();
		Number n = q.queryNumber("select count(*) from emp where salary>?",new Object[]{100});
		System.out.println(n);
	}
	public static void select02(){
		Query q  = QueryFactory.createQuery();
		Emp e =  (Emp)q.queryUniqueRow("select * from emp where id=?", Emp.class, new Object[]{40});
		System.out.println(e);
		System.out.println(e.getEmname());
	}
	public static void select03(){
		Query q  = QueryFactory.createQuery();
		List<Emp> list =  (List)q.queryRows("select * from emp where id>?", Emp.class, new Object[]{41});
		System.out.println(list);
		for (Emp emp : list) {
			System.out.println(emp.getEmname()+"--"+emp.getBirthday());
		}
	}
	
	public static void select04(){
		Query q  = QueryFactory.createQuery();
		String sql = "select e.id,e.empname,e.age,d.dname 'deptName',d.address 'deptAddr' from emp e join dept d on e.deptId=d.id ";
		List<EmpVO> list =  (List)q.queryRows(sql, 
				EmpVO.class, new Object[]{});
		System.out.println(list);
		for (EmpVO emp : list) {
			System.out.println(emp.getEmpname()+"--"+emp.getDeptAddr()+"--"+emp.getDeptName());
		}
	}
	public static void test05(){
		Query q  = QueryFactory.createQuery();
		Emp e = (Emp) q.queryById(Emp.class, 43);
		System.out.println(e.getEmname());
	}
	
	public static void main(String[] args) {
		//init();
		add();
		//delete();
		//update();
		//select01();
		//select02();
		//select03();
		//select04();
		//test05();
	}
}
