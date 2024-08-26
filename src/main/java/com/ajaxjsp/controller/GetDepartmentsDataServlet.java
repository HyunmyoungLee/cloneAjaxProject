package com.ajaxjsp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ajaxjsp.dao.EmployeesDAO;
import com.ajaxjsp.dao.EmployeesDAOImpl;
import com.ajaxjsp.etc.OutputJSONForError;
import com.ajaxjsp.vo.DepartmentsVO;

@WebServlet("/getDepartmentsData.do")
public class GetDepartmentsDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
    public GetDepartmentsDataServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("DEPARTMENTS 서블릿 호출");
		response.setContentType("application/json; charset=utf-8");
		EmployeesDAO dao = EmployeesDAOImpl.getInstance();
		PrintWriter out = response.getWriter();
		
			try {
				List<DepartmentsVO> list = dao.selectAllDepartments();
				JSONObject json = new JSONObject();
				json.put("status", "success");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
				String outputDate = sdf.format(Calendar.getInstance().getTime());
				json.put("outputDate", outputDate);
				json.put("count", list.size());
				
				JSONArray departments = new JSONArray();
				
				if(list.size() > 0) {
					for(DepartmentsVO dp : list) {
						JSONObject department = new JSONObject();
						department.put("DEPARTMENT_ID", dp.getDepartment_id());
						department.put("DEPARTMENT_NAME", dp.getDepartment_name());
						department.put("MANAGER_ID", dp.getManager_id());
						department.put("LOCATION_ID", dp.getLocation_id());
						
						departments.add(department);
					}
					json.put("DEPARTMENTS", departments);
				}
				out.print(json.toJSONString());
			} catch (NamingException | SQLException e) {
				out.print(OutputJSONForError.outputJson(e));
			} finally {
				out.close();
			}

	}



}
