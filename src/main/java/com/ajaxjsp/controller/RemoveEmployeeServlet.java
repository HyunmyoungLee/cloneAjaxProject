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

@WebServlet("/deleteEmp.do")
public class RemoveEmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public RemoveEmployeeServlet() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		int empNo = Integer.parseInt(request.getParameter("empNo"));
		System.out.println("사원 삭제 요청 : ");
		System.out.println(empNo);
		
		EmployeesDAO dao =EmployeesDAOImpl.getInstance();
		
		Date now = new Date(System.currentTimeMillis());
//		Date now2 = new Date(Calendar.getInstance().getTimeInMillis());
		
		try {
			int result = dao.deleterEmployee(empNo, now);
			if(result == 1) { // 사원정보 수정 성공
				JSONObject json = new JSONObject();
				json.put("status", "success");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
				String outputDate = sdf.format(Calendar.getInstance().getTime());
				json.put("outputDate", outputDate);
				
				out.print(json.toJSONString());
			}else{
				JSONObject json = new JSONObject();
				json.put("status", "fail");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
				String outputDate = sdf.format(Calendar.getInstance().getTime());
				json.put("outputDate", outputDate);
				out.print(json.toJSONString());
			}
		} catch (NamingException | SQLException e) {
			OutputJSONForError.outputJson(e);
		} finally {
			out.flush();
			out.close();
		}
	}

	

}
