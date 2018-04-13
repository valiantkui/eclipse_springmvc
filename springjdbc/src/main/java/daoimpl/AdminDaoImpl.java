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
 * 持久层实现
 * @author KUIKUI
 *
 */

//使用@Repository来标注持久层
@Repository("adminDao")
public class AdminDaoImpl implements AdminDao{

	@Resource(name="dataSource")
	private DataSource ds;
	
	@Resource(name="jdbcTemplate")
	private JdbcTemplate template;

	/**
	 * 访问数据库，未使用jdbcTemplate
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
			//记日志
			e.printStackTrace();
			/*
			 * 看异常能否恢复，如果能恢复，
			 * 则立即恢复。
			 * 如果不能恢复（发生了系统异常，比如数据库停止服务），则提示用户稍后重试
			 */
			throw new RuntimeException(e);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			//关闭连接
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
	 * 使用jdbcTemplate访问数据库
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
		System.out.println("受影响的行："+rows);
		
	}
	
	public List<Admin> findAll() {
		List<Admin> adminList = new ArrayList<Admin>();
		String sql = "select * from user";
		 
		//告诉jdbcTemplate如何将ResultSet中的一条记录转换成实体对象。 
		adminList = template.query(sql, new AdminRowMapper());
		return adminList;
	}
	
	/*
	 * 修改某条记录
	 */
	public void modify(Admin admin) {
		String sql = "update user set password=? where id=?";
		Object[] args = new Object[] {
				admin.getPassword(),admin.getAdminCode()};
		int affectRows = template.update(sql,args);
		System.out.println("受影响的行："+affectRows);
		
	}
	
	public void delete(String id) {
		String sql = "delete from user where id = ?";
		Object[] args = new Object[] {id};
		int affectRows = template.update(sql,args);
		System.out.println(affectRows);
		
	}
	
	/**
	 *告诉jdbcTemplate如何将ResultSet中的一条记录转换成实体对象。 
	 */
	class AdminRowMapper implements RowMapper<Admin>{
		/**
		 * rs:要处理的结果集
		 * rowNum:当前正在处理的记录的下标(从0开始)
		 */
		public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
			Admin admin = new Admin();
			admin.setAdminCode(rs.getString(1));
			admin.setPassword(rs.getString(2));
			return admin;
		}
	}
}
