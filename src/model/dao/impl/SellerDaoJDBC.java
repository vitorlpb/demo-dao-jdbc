package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Departments;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
		
	}

	@Override
	public void insert(Seller obj) {
		
	}

	@Override
	public void update(Seller obj) {
		
	}

	@Override
	public void deleteById(Integer id) {
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("select seller.*, department.name as DepName "
									 + "from seller "
									 + "inner join department on seller.DEPARTMENTID = department.id "
									 + "where seller.id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Departments dep = new Departments();
				dep.setId(rs.getInt("DEPARTMENTID"));
				dep.setName(rs.getString("DepName"));
				Seller obj = new Seller();
				obj.setId(rs.getInt("ID"));
				obj.setName(rs.getString("NAME"));
				obj.setEmail(rs.getString("EMAIL"));
				obj.setBaseSalary(rs.getDouble("BASESALARY"));
				obj.setBirthdate(rs.getDate("BIRTHDATE"));
				obj.setDepartments(dep);
				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new  DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

	@Override
	public List<Seller> findAll() {
		return null;
	}
	

}
