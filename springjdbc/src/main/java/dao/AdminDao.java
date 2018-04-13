package dao;

import model.Admin;

public interface AdminDao {

	public Admin findByAdminCode(String adminCode);
}
