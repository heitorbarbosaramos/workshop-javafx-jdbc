package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{

	private Connection conn;	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("insert into department (Id,Name) "
										+ "values "
										+ "(?,?)",Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, obj.getId());
			st.setString(2, obj.getName());
			
			Integer rowsAffcted = st.executeUpdate();
			
			if(rowsAffcted > 0) {
				
				rs = st.getGeneratedKeys();
				while(rs.next()) {
					System.out.println("ID INSERT DEPARTMENT: "+rs.getInt(1));
				}
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		
		try {
		st = conn.prepareStatement("UPDATE department SET "
									+ "Name = ? "
									+ "WHERE "
									+ "Id = ?");
		st.setString(1, obj.getName());
		st.setInt(2, obj.getId());
		
		int rowsAffcted = st.executeUpdate();
		
		if(rowsAffcted > 0 ) {System.out.println("\n\n\t update success");}else{System.out.println("\n\n\t update failed");}
		
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st;
		
		try {
			st = conn.prepareStatement("delete from department where id = ?");
			st.setInt(1, id);
			int rowsAffcted = st.executeUpdate();
			
			if(rowsAffcted > 0 ) {System.out.println("\n\n\t delete success");}else{System.out.println("\n\n\t delete failed");}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		
		
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("select * from department where id = ?");
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			while(rs.next()) {
				return new Department(rs.getInt("Id"), rs.getString("Name"));
			}
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
		return null;
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("select * from department");		
			rs = st.executeQuery();
			
			List<Department> list = new ArrayList<Department>();
			
			while(rs.next()) {
				Department department = new Department(rs.getInt("Id"), rs.getString("Name"));
				list.add(department);
			}
			
			return list;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		

	}

}
