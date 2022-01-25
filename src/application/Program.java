package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Departments;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=== TEST 1: seller findById ===");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("\n=== TEST 2: seller findByDepartment ===");
		Departments department =  new Departments(2,null);
		List<Seller> list = sellerDao.findingByDepartment(department);
		for (Seller obj : list) {
			System.out.println(obj);
		}
		
		System.out.println("\n=== TEST 3: seller findAll ===");
		list = sellerDao.findAll();
		for (Seller obj : list) {
			System.out.println(obj);
		}

//		System.out.println("\n=== TEST 4: seller Insert ===");
//		Seller newSeller = new Seller(9,"Greg","Greg@gmail.com",new Date(), 4000, department);
//		sellerDao.insert(newSeller);
//		System.out.println("inserido new Id = " + newSeller.getId());
		
		System.out.println("\n=== TEST 5: seller update ===");
		seller = sellerDao.findById(9);
		//seller.setName("LorensoLorival");
		seller.setEmail("lorenso_lorival@gmail.com.br.org");
		sellerDao.update(seller);
		System.out.println("Update complete");

	}

}
