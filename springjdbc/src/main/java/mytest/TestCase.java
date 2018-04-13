package mytest;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import dao.AdminDao;
import daoimpl.AdminDaoImpl;
import model.Admin;

public class TestCase {
	
	private ClassPathXmlApplicationContext ioc;

	
	@Before
	public void init() {
		ioc = new ClassPathXmlApplicationContext("springjdbc.xml");
	}
	
	@Test
	//�������ӳ�
	public void test1() throws SQLException {
		DataSource ds = ioc.getBean("dataSource",DataSource.class);
		
		System.out.println(ds.getConnection());
		
		
	}
	
	@Test
	//���Գ־ò㣨AdminDaoImpl�ࣩ
	public void test2() {
		AdminDaoImpl ad = ioc.getBean("adminDao",AdminDaoImpl.class);
		Admin admin = ad.findByAdminCode("yuankui");
		
		System.out.println(admin);
	}

	
	@Test
	//����findByAdminCode2����
	public void test3() {
		AdminDaoImpl ad = ioc.getBean("adminDao",AdminDaoImpl.class);
		Admin admin = ad.findByAdminCode2("shaonan");
		System.out.println(admin);
	}
	@Test
	//����findByAdminCode3����
	public void test6() {
		AdminDaoImpl ad = ioc.getBean("adminDao",AdminDaoImpl.class);
		Admin admin = ad.findByAdminCode3("shaonan");
		System.out.println(admin);
	}

	@Test
	//����save����
	public void test4() {
		AdminDaoImpl ad = ioc.getBean("adminDao",AdminDaoImpl.class);
		Admin admin = new Admin();
		admin.setAdminCode("shaonan");
		admin.setPassword("123456");
		ad.save(admin);
		
	}
	
	@Test
	//��ʱfindAll����
	public void test5() {
		AdminDaoImpl ad = ioc.getBean("adminDao",AdminDaoImpl.class);
		List<Admin> adminList = ad.findAll();
		System.out.println(adminList);
	}
	
	@Test
	public void test7() {
		AdminDaoImpl ad = ioc.getBean("adminDao",AdminDaoImpl.class);
		Admin admin = new Admin();
		admin.setAdminCode("shaonan");
		admin.setPassword("666666");
		ad.modify(admin);
	}
	@Test
	//����delete����
	public void test8() {
		AdminDaoImpl ad = ioc.getBean("adminDao",AdminDaoImpl.class);
		
		ad.delete("shaonan");
	}
}
