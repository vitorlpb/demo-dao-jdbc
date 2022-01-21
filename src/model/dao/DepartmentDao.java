package model.dao;

import java.util.List;

import model.entities.Departments;

public interface DepartmentDao {

	void insert(Departments obj);
	void update(Departments obj);
	void deleteById(Integer id);
	Departments findById(Integer id);
	List<Departments> findAll();
}
