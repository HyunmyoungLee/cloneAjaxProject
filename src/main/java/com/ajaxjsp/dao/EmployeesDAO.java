package com.ajaxjsp.dao;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import com.ajaxjsp.dto.EmployeeDTO;
import com.ajaxjsp.vo.DepartmentsVO;
import com.ajaxjsp.vo.EmployeeVO;
import com.ajaxjsp.vo.JobsVO;

public interface EmployeesDAO {
	//모든 직원 정보를 얻어오는 메서드
	public abstract List<EmployeeVO> selectAllEmp(String order) throws NamingException, SQLException;
	
	// 모든 jobs 정보를 얻어오는 메서드
	public abstract List<JobsVO> selectAllJobs() throws NamingException, SQLException;
	

	// 모든 부서명 얻어오는 메서드
	public abstract List<DepartmentsVO> selectAllDepartments() throws NamingException, SQLException;
	
	//사원 저장 (저장프로시저 이용)
	public abstract String insertEmp(EmployeeDTO emp) throws NamingException, SQLException;
	
	//사원 수정
	public abstract int updateEmp(EmployeeDTO emp) throws NamingException, SQLException;
	
	//수정할 사원 정보 가져오기
	public abstract EmployeeVO selectEmployeeByEmpNo(int empNo) throws NamingException, SQLException;

	// 사원 삭제 (quit_date)
	public abstract int deleterEmployee(int empNo, Date now) throws NamingException, SQLException;

	// 이름으로 검색
	public abstract List<EmployeeVO> selectByEmpName(String searchName) throws NamingException, SQLException;

	// 전체 사원 정렬
	public abstract List<EmployeeVO> orderEmp(String orderMethod) throws NamingException, SQLException;

	public abstract List<EmployeeVO> orderSelectByEmpName(String searchName, String orderMethod) throws NamingException, SQLException;

	//검색 + 정렬
	public abstract List<EmployeeVO> selectAllEmpSearchOrder(String searchName, String orderMethod)throws NamingException, SQLException;

	
}