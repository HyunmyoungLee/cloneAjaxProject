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
import com.ajaxjsp.dto.EmployeeDTO;
import com.ajaxjsp.etc.OutputJSONForError;

/**
 * Servlet implementation class ModiServlet
 */
@WebServlet("/modifyEmp.do")
public class ModiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModiServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("수정할 직원 서블릿 요청");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		int employeeId = Integer.parseInt(request.getParameter("employeeId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String phoneNumber = request.getParameter("phoneNumber");
		Date hireDate = Date.valueOf(request.getParameter("hireDate"));
		String jobId = request.getParameter("jobId");
		float salary = Float.parseFloat(request.getParameter("salary")); 
		float commission = Float.parseFloat(request.getParameter("commission"));
		int managerId = Integer.parseInt(request.getParameter("managerId"));
		int departmentId = Integer.parseInt(request.getParameter("departmentId"));
		EmployeeDTO empDTO = new EmployeeDTO(employeeId, firstName, lastName, email, phoneNumber, hireDate, jobId, salary, commission, managerId, departmentId);
		
		EmployeesDAO dao = EmployeesDAOImpl.getInstance();
		
		try {
			int result = dao.updateEmp(empDTO);
			if(result == 1) { // 사원정보 수정 성공
				JSONObject json = new JSONObject();
				json.put("status", "success-modify");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
				String outputDate = sdf.format(Calendar.getInstance().getTime());
				json.put("outputDate", outputDate);
				
				out.print(json.toJSONString());
			}else{
				JSONObject json = new JSONObject();
				json.put("status", "fail-modify");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
				String outputDate = sdf.format(Calendar.getInstance().getTime());
				json.put("outputDate", outputDate);
				out.print(json.toJSONString());
			}
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
			out.print(OutputJSONForError.outputJson(e));
		} finally {
			out.flush();
			out.close();
		}
	}

}
