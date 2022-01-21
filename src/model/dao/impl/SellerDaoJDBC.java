package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				Departments dep = instantiateDepartment(rs);
				Seller obj = instantiateSeller(rs, dep);
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

	private Seller instantiateSeller(ResultSet rs, Departments dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("ID"));
		obj.setName(rs.getString("NAME"));
		obj.setEmail(rs.getString("EMAIL"));
		obj.setBaseSalary(rs.getDouble("BASESALARY"));
		obj.setBirthdate(rs.getDate("BIRTHDATE"));	
		obj.setDepartments(dep);
		return obj;
	}

	private Departments instantiateDepartment(ResultSet rs) throws SQLException {
		Departments dep = new Departments();
		dep.setId(rs.getInt("DEPARTMENTID"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		return null;
	}

	@Override
	public List<Seller> findingByDepartment(Departments department) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("select seller.*,department.NAME as DepName "
									 + "from seller inner join department on seller.DEPARTMENTID = department.ID "
									 + "where DEPARTMENTID = ? "
									 + "order by seller.NAME");
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Departments> map = new HashMap<>();
			
			while (rs.next()) {
				Departments dep = map.get(rs.getInt("DEPARTMENTID"));
				
				if(dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DEPARTMENTID"), dep);
				}
				
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
			return list;
				
		} catch (SQLException e) {
			throw new  DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	

}
