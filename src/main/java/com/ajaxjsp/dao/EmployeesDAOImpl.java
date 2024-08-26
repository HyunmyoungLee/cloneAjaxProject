package com.ajaxjsp.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.ajaxjsp.dto.EmployeeDTO;
import com.ajaxjsp.vo.DepartmentsVO;
import com.ajaxjsp.vo.EmployeeVO;
import com.ajaxjsp.vo.JobsVO;

public class EmployeesDAOImpl implements EmployeesDAO {

	private static EmployeesDAOImpl instance;
	
	private EmployeesDAOImpl() {}
	
	public static EmployeesDAOImpl getInstance() {
		if(instance == null) {
			instance = new EmployeesDAOImpl();
		}
		
		return instance;
	}
	
	@Override
	public List<EmployeeVO> selectAllEmp(String order) throws NamingException, SQLException {
		
		System.out.println("DAO단 호출");
		
		List<EmployeeVO> list = new ArrayList<>();
		Connection con = DBConnection.dbConnect(); 
if (con != null) {
	//		System.out.println(con.toString());
	String query = null;
	if(order.equals("empNo")) {
		query = "select e.*, d.department_name from employees e inner join departments d on e.department_id = d.department_id where quit_date is null order by employee_id";
	}else if(order.equals("hireDate")) {
		query = "select e.*, d.department_name from employees e inner join departments d on e.department_id = d.department_id where quit_date is null order by hire_date desc";
	}else if(order.equals("salary")) {
		query = "select e.*, d.department_name from employees e inner join departments d on e.department_id = d.department_id where quit_date is null order by salary desc";
	}else {
		query = "select e.*, d.department_name from employees e inner join departments d on e.department_id = d.department_id where quit_date is null";
	}
	PreparedStatement pstmt = con.prepareStatement(query);
	ResultSet rs = pstmt.executeQuery();
	while (rs.next()) {
		list.add(new EmployeeVO(rs.getInt("EMPLOYEE_ID"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"),
				rs.getString("EMAIL"), rs.getString("PHONE_NUMBER"), rs.getDate("HIRE_DATE"), rs.getString("JOB_ID"),
				rs.getFloat("SALARY"), rs.getFloat("COMMISSION_PCT"), rs.getInt("MANAGER_ID"),
				rs.getInt("DEPARTMENT_ID"), rs.getString("DEPARTMENT_NAME")));
	}
	DBConnection.dbClose(rs, pstmt, con);
}
		return list;
	}

	@Override
	public List<JobsVO> selectAllJobs() throws NamingException, SQLException {
		
		Connection con = DBConnection.dbConnect();
		List<JobsVO> list = new ArrayList<>();
		
		if(con != null) {
			String query = "select * from jobs";
			
			PreparedStatement pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				list.add(new JobsVO(rs.getString("JOB_ID"),rs.getString("JOB_TITLE"), rs.getInt("MIN_SALARY"), rs.getInt("MAX_SALARY")));
			}
			DBConnection.dbClose(rs,pstmt, con);
		}
		return list;
	}

	@Override
	public List<DepartmentsVO> selectAllDepartments() throws NamingException, SQLException {
		
		Connection con = DBConnection.dbConnect();
		List<DepartmentsVO> list = new ArrayList<>();
		if(con != null) {
			String query = "select * from departments";
			PreparedStatement pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				list.add(new DepartmentsVO(rs.getInt("DEPARTMENT_ID"), rs.getString("DEPARTMENT_NAME"), rs.getInt("MANAGER_ID"), rs.getInt("LOCATION_ID")));
			}
			DBConnection.dbClose(rs, pstmt, con);
		}
		return list;
	}

	@Override
	public String insertEmp(EmployeeDTO emp) throws NamingException, SQLException {
		System.out.println("사원저장 daoImpl호출");
		String result = null;
		
		Connection con = DBConnection.dbConnect();
		if(con != null) {
			String query = "{call PROC_SAVEEMP(?,?,?,?,?,?,?,?,?,?,?)}";
			
			CallableStatement cstmt =  con.prepareCall(query);
			cstmt.setString(1, emp.getFirst_name());
			cstmt.setString(2, emp.getLast_name());
			cstmt.setString(3, emp.getEmail());
			cstmt.setString(4, emp.getPhone_number());
			cstmt.setDate(5, emp.getHire_date());
			cstmt.setString(6, emp.getJob_id());
			cstmt.setFloat(7, emp.getSalary());
			cstmt.setFloat(8, emp.getCommission_pct());
			cstmt.setInt(9, emp.getManager_id());
			cstmt.setInt(10, emp.getDepartment_id());
			cstmt.registerOutParameter(11, java.sql.Types.VARCHAR);
			
			// 실행
			cstmt.executeUpdate();
			
			// OUT매개변수에서 반환되는 값을 가져오기.
			result = cstmt.getString(11);
			System.out.println(result);
			
			DBConnection.dbClose(cstmt, con);
		}
		
		return result;
	}

	@Override
	public int updateEmp(EmployeeDTO emp) throws NamingException, SQLException {
		System.out.println("업데이트EMP 호출");
		int result = 0;
		Connection con = DBConnection.dbConnect();
		if(con != null) {
			System.out.println("쿼리문 실행");
			System.out.println(emp.toString());
			String query = "update employees set first_name = ?, last_name = ?, email = ? ,  phone_number = ?, hire_date = ?, job_id = ?, salary = ?, commission_pct = ?, manager_id = ?, department_id = ? where employee_id = ?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, emp.getFirst_name());
			pstmt.setString(2,emp.getLast_name());
			pstmt.setString(3,emp.getEmail());
			pstmt.setString(4,emp.getPhone_number());
			pstmt.setDate(5,emp.getHire_date());
			pstmt.setString(6, emp.getJob_id());
			pstmt.setFloat(7,emp.getSalary());
			pstmt.setFloat(8,emp.getCommission_pct());
			pstmt.setInt(9, emp.getManager_id());
			pstmt.setInt(10, emp.getDepartment_id());
			pstmt.setInt(11, emp.getEmployee_id());
			
			 result = pstmt.executeUpdate();
			 System.out.println(result);
			DBConnection.dbClose(pstmt, con);
		}
		
		return result;
	}

	@Override
	public EmployeeVO selectEmployeeByEmpNo(int empNo) throws NamingException, SQLException {
		Connection con = DBConnection.dbConnect();
		EmployeeVO emp = null;
		String query = "  select e.*, d.department_name\r\n"
				+ "    from employees e inner join departments d \r\n"
				+ "    on e.department_id = d.department_id where e.employee_id = ?";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, empNo);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			emp = new EmployeeVO(rs.getInt("EMPLOYEE_ID"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"),
					rs.getString("EMAIL"), rs.getString("PHONE_NUMBER"), rs.getDate("HIRE_DATE"), rs.getString("JOB_ID"),
					rs.getFloat("SALARY"), rs.getFloat("COMMISSION_PCT"), rs.getInt("MANAGER_ID"),
					rs.getInt("DEPARTMENT_ID"), rs.getString("DEPARTMENT_NAME"));
		}
		
		DBConnection.dbClose(rs, pstmt, con);
		return emp;
	}

	@Override
	public int deleterEmployee(int empNo, Date now) throws NamingException, SQLException {
		
		int result = 0;
		Connection con = DBConnection.dbConnect();
		if(con!= null) {
			String query = " update employees set quit_date = ? where employee_id = ? ";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setDate(1, now);
			pstmt.setInt(2, empNo);
			
			result = pstmt.executeUpdate();
			DBConnection.dbClose(pstmt, con);
		}
		return result;
	}

	@Override
	public List<EmployeeVO> selectByEmpName(String searchName) throws NamingException, SQLException {
		Connection con = DBConnection.dbConnect();
		List<EmployeeVO> list = new ArrayList<>();
		
		if(con != null) {
			String query = " select e.*, d.department_name from employees e inner join departments d\r\n"
					+ "on e.department_id = d.department_id\r\n"
					+ "where (lower(first_name) like '%' || ? ||'%' or lower(last_name) like '%' || ? ||'%') and quit_date is null";
			
//			String query = " select e.*, d.department_name from employees e inner join departments d\r\n"
//					+ "on e.department_id = d.department_id\r\n"
//					+ "where (lower(first_name) like '%steven%' or lower(last_name) like '%steven%') and quit_date is null";
			
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, searchName);
			pstmt.setString(2, searchName);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				list.add(new EmployeeVO(rs.getInt("EMPLOYEE_ID"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"),
						rs.getString("EMAIL"), rs.getString("PHONE_NUMBER"), rs.getDate("HIRE_DATE"), rs.getString("JOB_ID"),
						rs.getFloat("SALARY"), rs.getFloat("COMMISSION_PCT"), rs.getInt("MANAGER_ID"),
						rs.getInt("DEPARTMENT_ID"), rs.getString("DEPARTMENT_NAME")));
			}
			DBConnection.dbClose(rs, pstmt, con);
		}
		
		return list;
	}

	@Override
	public List<EmployeeVO> orderEmp(String orderMethod) throws NamingException, SQLException {

		List<EmployeeVO> list = new ArrayList<>();
		Connection con = DBConnection.dbConnect();
		
		if(con != null) {
			//쿼리문
			String query = null;
			if(orderMethod.equals("employee_id")) {
				query = "select e.*, d.department_name from employees e inner join departments d on e.department_id = d.department_id where quit_date is null order by employee_id";
			}else if(orderMethod.equals("hire_date")) {
				query = "select e.*, d.department_name from employees e inner join departments d on e.department_id = d.department_id where quit_date is null order by hire_date desc";
			}else if(orderMethod.equals("salary")) {
				query = "select e.*, d.department_name from employees e inner join departments d on e.department_id = d.department_id where quit_date is null order by salary desc";
			}else {
				query = "select e.*, d.department_name from employees e inner join departments d on e.department_id = d.department_id where quit_date is null";
			}
		PreparedStatement pstmt = con.prepareStatement(query);

		ResultSet rs = pstmt.executeQuery();
		
		while (rs.next()) {
			list.add(new EmployeeVO(rs.getInt("EMPLOYEE_ID"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"),
					rs.getString("EMAIL"), rs.getString("PHONE_NUMBER"), rs.getDate("HIRE_DATE"), rs.getString("JOB_ID"),
					rs.getFloat("SALARY"), rs.getFloat("COMMISSION_PCT"), rs.getInt("MANAGER_ID"),
					rs.getInt("DEPARTMENT_ID"), rs.getString("DEPARTMENT_NAME")));
		}
		DBConnection.dbClose(rs, pstmt, con);
		}
		
		return list;
	}

	@Override
	public List<EmployeeVO> orderSelectByEmpName(String searchName, String orderMethod)
			throws NamingException, SQLException {
		Connection con = DBConnection.dbConnect();
		List<EmployeeVO> list = new ArrayList<>();
		
		if(con != null) {
			String query = " select e.*, d.department_name from employees e inner join departments d\r\n"
					+ "on e.department_id = d.department_id\r\n"
					+ "where (lower(first_name) like '%' || ? ||'%' or lower(last_name) like '%' || ? ||'%') and quit_date is null";
			if(orderMethod.equals("empNo")) {
				query += " order by e.employee_id";
			}else if(orderMethod.equals("hireDate")) {
				query += " order by e.hire_date desc";
			}else if(orderMethod.equals("salary")) {
				query += " order by salary desc";
			}
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, searchName);
			pstmt.setString(2, searchName);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(new EmployeeVO(rs.getInt("EMPLOYEE_ID"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"),
						rs.getString("EMAIL"), rs.getString("PHONE_NUMBER"), rs.getDate("HIRE_DATE"), rs.getString("JOB_ID"),
						rs.getFloat("SALARY"), rs.getFloat("COMMISSION_PCT"), rs.getInt("MANAGER_ID"),
						rs.getInt("DEPARTMENT_ID"), rs.getString("DEPARTMENT_NAME")));
			}
			DBConnection.dbClose(rs, pstmt, con);
			}
		return list;
	}

	@Override
	public List<EmployeeVO> selectAllEmpSearchOrder(String searchName, String orderMethod)
			throws NamingException, SQLException {
		Connection con = DBConnection.dbConnect();
		List<EmployeeVO> list = new ArrayList<>();
		if(con != null) {
			String query = " select e.*, d.department_name from employees e inner join departments d\r\n"
					+ "on e.department_id = d.department_id\r\n"
					 + "where quit_date is null";
			PreparedStatement pstmt = null;
			if(searchName == null) {//검색어가 없는 경우
				query += " order by e." + orderMethod;
				 pstmt = con.prepareStatement(query);
			}else if(searchName != null){// 검색어가 있는 경우
				query += " and (lower(first_name) like ? or lower(last_name) like ?) order by e." + orderMethod;
				 pstmt = con.prepareStatement(query);
				pstmt.setString(1, "%" + searchName + "%");
				pstmt.setString(2, "%" + searchName + "%");
			}
			
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(new EmployeeVO(rs.getInt("EMPLOYEE_ID"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"),
						rs.getString("EMAIL"), rs.getString("PHONE_NUMBER"), rs.getDate("HIRE_DATE"), rs.getString("JOB_ID"),
						rs.getFloat("SALARY"), rs.getFloat("COMMISSION_PCT"), rs.getInt("MANAGER_ID"),
						rs.getInt("DEPARTMENT_ID"), rs.getString("DEPARTMENT_NAME")));
			}
			DBConnection.dbClose(rs, pstmt, con);
			}
		return list;
	}

}
