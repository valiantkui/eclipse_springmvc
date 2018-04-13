package daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import dao.AdminDao;
import model.Admin;

/**
 * �־ò�ʵ��
 * @author KUIKUI
 *
 */

//ʹ��@Repository����ע�־ò�
@Repository("adminDao")
public class AdminDaoImpl implements AdminDao{

	@Resource(name="dataSource")
	private DataSource ds;
	
	@Resource(name="jdbcTemplate")
	private JdbcTemplate template;

	/**
	 * �������ݿ⣬δʹ��jdbcTemplate
	 */
	public Admin findByAdminCode(String adminCode) {
		System.out.println("findByAdminCode()");
		Admin admin = null;
		Connection con = null;
		
		try {
			con =  ds.getConnection();
			String sql = "select * from user where id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1,adminCode);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				admin = new Admin();
				admin.setAdminCode(rs.getString("id"));
				admin.setPassword(rs.getString("password"));
			}
			return admin;
		}catch(SQLException e) {
			//����־
			e.printStackTrace();
			/*
			 * ���쳣�ܷ�ָ�������ָܻ���
			 * �������ָ���
			 * ������ָܻ���������ϵͳ�쳣���������ݿ�ֹͣ���񣩣�����ʾ�û��Ժ�����
			 */
			throw new RuntimeException(e);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			//�ر�����
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				
			}
		}
		return null;
	}

	/**
	 * ʹ��jdbcTemplate�������ݿ�
	 */
	public Admin findByAdminCode2(String adminCode) {
		
		System.out.println("findByAdminCode2()");
		String sql = "select * from user where id = ?";
		Object[] params = new Object[] {adminCode};
		Admin admin = template.queryForObject(sql,params, new RowMapper<Admin>() {
			public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
				Admin admin = new Admin();
				admin.setAdminCode(rs.getString(1));
				admin.setPassword(rs.getString(2));
				return admin;
			}
		});
		return admin;
	}
	public Admin findByAdminCode3(String adminCode) {
		
		System.out.println("findByAdminCode2()");
		String sql = "select * from user where id = ?";
		Object[] params = new Object[] {adminCode};
		List<Admin> adminList = template.query(sql,params, new RowMapper<Admin>() {
			public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
				Admin admin = new Admin();
				admin.setAdminCode(rs.getString(1));
				admin.setPassword(rs.getString(2));
				return admin;
			}
		});
		Admin admin = null;
		if(adminList != null && adminList.size()>0) {
			admin = adminList.get(0); 
		}
		return admin;
	}
	
	public void save(Admin admin) {
		System.out.println("save()");
		
		String sql = "INSERT INTO user VALUES(?,?)";
		Object[] params = new Object[] {
				admin.getAdminCode(),admin.getPassword()};
		
		int rows =	template.update(sql, params);
		System.out.println("��Ӱ����У�"+rows);
		
	}
	
	public List<Admin> findAll() {
		List<Admin> adminList = new ArrayList<Admin>();
		String sql = "select * from user";
		 
		//����jdbcTemplate��ν�ResultSet�е�һ����¼ת����ʵ����� 
		adminList = template.query(sql, new AdminRowMapper());
		return adminList;
	}
	
	/*
	 * �޸�ĳ����¼
	 */
	public void modify(Admin admin) {
		String sql = "update user set password=? where id=?";
		Object[] args = new Object[] {
				admin.getPassword(),admin.getAdminCode()};
		int affectRows = template.update(sql,args);
		System.out.println("��Ӱ����У�"+affectRows);
		
	}
	
	public void delete(String id) {
		String sql = "delete from user where id = ?";
		Object[] args = new Object[] {id};
		int affectRows = template.update(sql,args);
		System.out.println(affectRows);
		
	}
	
	/**
	 *����jdbcTemplate��ν�ResultSet�е�һ����¼ת����ʵ����� 
	 */
	class AdminRowMapper implements RowMapper<Admin>{
		/**
		 * rs:Ҫ����Ľ����
		 * rowNum:��ǰ���ڴ���ļ�¼���±�(��0��ʼ)
		 */
		public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
			Admin admin = new Admin();
			admin.setAdminCode(rs.getString(1));
			admin.setPassword(rs.getString(2));
			return admin;
		}
	}
}
