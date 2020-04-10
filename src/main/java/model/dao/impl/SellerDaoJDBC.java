package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{

	private Connection conn;
	public  SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {			
			st = conn.prepareStatement("INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId)  "
					+ "VALUES  "
					+ "(?, ?, ?, ?, ?) ",Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());

			Integer rowsAffcted = st.executeUpdate();
			
			if(rowsAffcted > 0 ) {
				System.out.println("\n\n\t Insert success");
				rs = st.getGeneratedKeys();
				while(rs.next()) {
					System.out.println("ID NOVO: "+rs.getInt(1));
				}
			}else{System.out.println("\n\n\t Insert failed");}

		}catch(SQLException e) {			
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

	@Override
	public void update(Seller obj) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {			
			st = conn.prepareStatement("UPDATE seller  SET "
											+ "Name = ?, "
											+ "Email = ?, "
											+ "BirthDate = ?, "
											+ "BaseSalary = ?, "
											+ "DepartmentId = ?  "
										+ "WHERE "
											+ "Id = ? ");
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			
			Integer rowsAffcted = st.executeUpdate();
			
			if(rowsAffcted > 0 ) {System.out.println("\n\n\t update success");}else{System.out.println("\n\n\t update failed");}

		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}
		
	

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE FROM seller  WHERE Id = ? ");
			st.setInt(1, id);
			
			int rowsAffcted =  st.executeUpdate();
			if(rowsAffcted > 0 ) {System.out.println("\n\n\t Delete Success ID: "+id);}else{System.out.println("\n\n\t Delete Failed ID: "+id);}
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		} 
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("SELECT "
										+"seller.*,department.Name as DepName  "
										+"FROM "
										+ "seller INNER JOIN department  "
										+ "ON seller.DepartmentId = department.Id  "
										+ "WHERE "
										+ "seller.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				
				Department dep = instatiationDepartment(rs);
				Seller obj = instatiationSeller(rs,dep);

				return obj;
				
			}
			
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return null;
	}

	private Seller instatiationSeller(ResultSet rs, Department dep) throws SQLException {
		
		Seller obj = new Seller();
		
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(new java.util.Date(rs.getTimestamp("BirthDate").getTime()));
		//obj.setBirthDate(rs.getDate("BirthDate"));		
		obj.setEmail(rs.getString("Email"));
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instatiationDepartment(ResultSet rs) throws SQLException {

		Department dep = new Department();
		
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {

		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("SELECT "
										+ "seller.*,department.Name as DepName  "
										+ "FROM "
										+ "seller "
										+ "INNER JOIN department  ON seller.DepartmentId = department.Id "
										+ "ORDER BY Name");
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> mapDeartment = new HashMap<Integer, Department>();
			
			while(rs.next()) {
				
				Department dep = mapDeartment.get(rs.getInt("DepartmentId"));
				if(dep == null) {					
					dep = instatiationDepartment(rs);
					mapDeartment.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instatiationSeller(rs,dep);
				list.add(obj);
			}
			
			return list;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("SELECT "
										+ "seller.*,department.Name as DepName  "
										+ "FROM "
										+ "seller "
										+ "INNER JOIN department  ON seller.DepartmentId = department.Id "
										+ "WHERE "
										+ "DepartmentId = ? "
										+ "ORDER BY Name");
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> mapDeartment = new HashMap<Integer, Department>();
			
			while(rs.next()) {
				
				Department dep = mapDeartment.get(rs.getInt("DepartmentId"));
				if(dep == null) {					
					dep = instatiationDepartment(rs);
					mapDeartment.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instatiationSeller(rs,dep);
				list.add(obj);
			}
			
			return list;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	

}

