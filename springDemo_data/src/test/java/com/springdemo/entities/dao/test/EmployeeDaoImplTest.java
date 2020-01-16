package com.springdemo.entities.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.springdemo.db.DbConnectionManagerTest;
import com.springdemo.entities.Employee;
import com.springdemo.entities.dao.EmployeeDao;


@ContextConfiguration(locations="classpath:/springDemo-data-context.xml")
@RunWith(SpringRunner.class)
public class EmployeeDaoImplTest {

	private Logger logger = Logger.getLogger(EmployeeDaoImplTest.class.getName());
	
	
	@Autowired
	private EmployeeDao employeeDaoImpl;
	
	@Autowired
	private ComboPooledDataSource dataSource;
	
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	
	@Before
	public void setUp() throws Exception {
		
		String username = "springdemouser";
		
		String password = "spring_Demo1";
		
		String jdbcDriver = "com.mysql.cj.jdbc.Driver";
		
		String DBUrl = "jdbc:mysql://localhost:3306/springDemoDB?useSSL=false&serverTimezone=UTC"
				+ "";
		
		Connection conn = null;
		
		QueryRunner queryRunner = new QueryRunner(dataSource);
		
		try {
			
		
			DbUtils.loadDriver(jdbcDriver);
			
			conn = DriverManager.getConnection(DBUrl, username, password);
			
			queryRunner.update("drop database springdemodb");
			
			queryRunner.update("create database springdemodb");
			
	//		queryRunner.update("drop table employee");
			queryRunner.update("use springdemodb");
			
			queryRunner.update("create table `employee`(\n" + 
					"	\n" + 
					"	`id` int(11) not null auto_increment,\n" + 
					"	`first_name` varchar(45) default null,\n" + 
					"	`last_name` varchar(45) default null,\n" + 
					"	`email` varchar(45) default null,\n" + 
					"	\n" + 
					"	 primary key(`id`)\n" + 
					"	\n" + 
					")ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;");
			
			
			
		}
		catch(Exception e) {
			
			logger.throwing(EmployeeDaoImplTest.class.getName(), "setUp() method",e.getCause());
			
			e.printStackTrace();
			
		}
		finally {
			
			DbUtils.close(conn);
		}
				
		
	}
	
	@Test
	public void dbManagerClassesInitializedTest() {
		
		assertNotNull(employeeDaoImpl);
		assertNotNull(dataSource);
//		assertNotNull(sessionFactory);
	}
	
	@Test
	public void addEmployeeToDatabaseTest() {
		
	try {	
			logger.info("Creating new employee object");
			 logger.info("Creating new employee object");
			Employee tempEmployee = new Employee("John", "Paulina", "john@gmail.com");
			
			logger.info("Storing employee to the database");
			employeeDaoImpl.addEmployee(tempEmployee);
			
			logger.info("Successfully saving employee to the databsase");
			
	}
	catch(Exception e) {
	
		e.printStackTrace();
		logger.warning("Error saving employee to the dataabse");
	}
	
		
		
	}
	
	@Test
	public void getEmployeeListTest() {
		
		List<Employee> theEmployees = createEmployeeAndSave();
		
	}
	
	@Test
	public void getEmployeeByIdTest() {
		
		List<Employee> theEmployees = createEmployeeAndSave();
			
			
			int employeeId = theEmployees.get(0).getId();
			
			Employee tempEmployee4  = employeeDaoImpl.getEmployeeById(employeeId);
			
			String firstName = tempEmployee4.getFirstName();
			String lastName = tempEmployee4.getLastName();
			String email = tempEmployee4.getEmail();
			
			assertNotNull(tempEmployee4);
			
			System.out.println("Employee found from the Database: ==>> "+tempEmployee4);
			
			assertEquals(firstName, tempEmployee4.getFirstName());
			assertEquals(lastName, tempEmployee4.getLastName());
			assertEquals(email, tempEmployee4.getEmail());
					
	}
	
	@Test
	public void updateEmployee() {
		
		//get an employee from the database
		
		List<Employee> theEmployees = createEmployeeAndSave();
		
		
		Employee tempEmployee4 = theEmployees.get(0);
		
		//update the employee
		displayEmployee(tempEmployee4);
		
		tempEmployee4.setLastName("Johnson");
		
		//call daoimpl to update employee
		employeeDaoImpl.updateEmployee(tempEmployee4);
		
		
		
	}

	private void displayEmployee(Employee tempEmployee4) {
		
		System.out.println(tempEmployee4);
	}

	private List<Employee> createEmployeeAndSave() {
		//create employee
		Employee tempEmployee = new Employee("John", "Paulina", "john@gmail.com");
		Employee tempEmployee2 = new Employee("James", "Brown", "james@gmail.com");
		Employee tempEmployee3 = new Employee("Micheal", "Blake", "micheal@gmail.com");
	
	
		//save empoloyees to the database
		employeeDaoImpl.addEmployee(tempEmployee);
		employeeDaoImpl.addEmployee(tempEmployee2);
		employeeDaoImpl.addEmployee(tempEmployee3);
		
		
		//get employees from the database
		List<Employee> theEmployees = employeeDaoImpl.getEmployees();
		
		assertNotNull(theEmployees.get(0));
		assertNotNull(theEmployees.get(1));
		assertNotNull(theEmployees.get(2));
		return theEmployees;
	}
	
	@Test 
	public void deleteEmployeeTest() {
		
		List<Employee> theEmployees = createEmployeeAndSave();
		
		//get an employee from the list of employees
		Employee tempEmployee = theEmployees.get(0);
		
		//display the employee
		displayEmployee(tempEmployee);
		
		assertNotNull(tempEmployee);
		
		employeeDaoImpl.deleteEmployee(tempEmployee.getId());
		
	}
	
	
	
	

}
