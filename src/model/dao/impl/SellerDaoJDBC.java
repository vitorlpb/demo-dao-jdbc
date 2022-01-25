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
import model.entities.Departments;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
		
	}

	@Override
	public void insert(Seller obj) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("insert into seller (ID, NAME, EMAIL, BIRTHDATE, BASESALARY, DEPARTMENTID)"
									 + "values (?,?,?,?,?,?)",
									 Statement.RETURN_GENERATED_KEYS);
			
			st.setInt(1, obj.getId());
			st.setString(2, obj.getName());
			st.setString(3, obj.getEmail());
			st.setDate(4, new java.sql.Date(obj.getBirthdate().getTime()));
			st.setInt(5, obj.getBaseSalary());
			st.setInt(6, obj.getDepartments().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				
				DB.closeResultSet(rs);
				
			}else {
				throw new DbException("Erro - nenhuma linha afetada");
			}
			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("update seller "
					 				 + "set NAME= ?, EMAIL= ?, BIRTHDATE= ?, BASESALARY= ?,DEPARTMENTID= ? "
					 				 + "WHERE ID = ?");
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthdate().getTime()));
			st.setInt(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartments().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		

		
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
		obj.setBaseSalary(rs.getInt("BASESALARY"));
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
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("select seller.*,department.NAME as DepName "
									 + "from seller inner join department on seller.DEPARTMENTID = department.ID "
									 + "order by seller.NAME");
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
