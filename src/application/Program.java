package application;

import java.util.Date;

import model.entities.Departments;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		Departments obj = new Departments(1,"Books");
		
		Seller seller = new Seller(21,"Bob","bob@gmail.com",new Date(),3000.0,obj);
		
		System.out.println(seller);
	
	}

}
