package com.ajaxjsp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.ajaxjsp.dao.EmployeesDAO;
import com.ajaxjsp.dao.EmployeesDAOImpl;
import com.ajaxjsp.etc.OutputJSONForError;
import com.ajaxjsp.vo.EmployeeVO;

@WebServlet("/getEmployee.do")
public class GetEmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetEmployeeServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("사원 수정 서블릿 empNO : " + request.getParameter("empNo"));
		
		int empNo = Integer.parseInt(request.getParameter("empNo"));
		EmployeesDAO dao = EmployeesDAOImpl.getInstance();
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		try {
			EmployeeVO e = dao.selectEmployeeByEmpNo(empNo);
			
			//응답 json 만들기
			JSONObject json = new JSONObject();
			json.put("status", "success");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
			String outputDate = sdf.format(Calendar.getInstance().getTime());
			json.put("outputDate", outputDate);
			
			JSONObject employee = new JSONObject();
			employee.put("EMPLOYEE_ID", e.getEmployee_id());
			employee.put("FIRST_NAME", e.getFirst_name());
			employee.put("LAST_NAME", e.getLast_name());
			employee.put("EMAIL" , e.getEmail());
			employee.put("PHONE_NUMBER", e.getPhone_number());
			
			Date tmpDate = e.getHire_date();
			SimpleDateFormat sdfHireDate = new SimpleDateFormat("yyyy-MM-dd");
			employee.put("HIRE_DATE", sdfHireDate.format(tmpDate));
			employee.put("JOB_ID", e.getJob_id());
			employee.put("SALARY", e.getSalary());
			employee.put("COMMISSION_PCT", e.getCommission_pct());
			employee.put("MANAGER_ID", e.getManager_id());
			employee.put("DEPARTMENT_ID", e.getDepartment_id());
			employee.put("DEPARTMENT_NAME", e.getDepartment_name());
			
			json.put("employee", employee);
			
			out.print(json.toJSONString());
			
		} catch (NamingException | SQLException e) {
			out.print(OutputJSONForError.outputJson(e));
		}finally {
			out.flush();
			out.close();
		}
	}

}
