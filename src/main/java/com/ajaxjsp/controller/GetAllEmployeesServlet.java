package com.ajaxjsp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ajaxjsp.dao.EmployeesDAOImpl;
import com.ajaxjsp.etc.OutputJSONForError;
import com.ajaxjsp.vo.EmployeeVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/getAllEmployees.do")
public class GetAllEmployeesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetAllEmployeesServlet() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("서블릿 호출됨");
		String order = request.getParameter("order");
		System.out.println(order);
		
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		EmployeesDAOImpl dao = EmployeesDAOImpl.getInstance();
		
		try {
			List<EmployeeVO> list = dao.selectAllEmp(order);
			
//			for(EmployeeVO e : list) {
//				System.out.println(e.toString());
//			}
			
			//json으로 변환해서 응답
			//1)json-simple 이용
			JSONObject json = new JSONObject();
			json.put("status", "success");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
			String outputDate = sdf.format(Calendar.getInstance().getTime());
			json.put("outputDate", outputDate);
			json.put("count", list.size());
			
			
			if(list.size() > 0) {
				JSONArray employees = new JSONArray();
				
				for(EmployeeVO e : list) {
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
					employee.put("DEPARTMENT_NAME", e.getDepartment_name());
					
					employees.add(employee);
				}
				
				json.put("employees", employees);
			}
//			String jsonString = json.toJSONString();
			//(2) GSON 라이브러리 이용 
//			String jsonString = toJsonWithGson(list);
//			out.print(jsonString);
			String jsonString = toJsonWithGsonMap(list);
			out.print(jsonString);
			out.close();
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			out.print(OutputJSONForError.outputJson(e));
		}finally {
			out.close();
		}
	}

	private String toJsonWithGson(List<EmployeeVO> list) {
		Gson gson = new Gson();
		return gson.toJson(list);
	}
	
	private String toJsonWithGsonMap(List<EmployeeVO> list) {
		Map map = new HashMap<>();
		map.put("status", "success");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
		String outputDate = sdf.format(Calendar.getInstance().getTime());
		map.put("outputDate", outputDate);
		map.put("count", list.size());
		map.put("employees", list);
		
//		Gson gson = new Gson();
//		return gson.toJson(map);
		Gson gsonDate = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		return gsonDate.toJson(map);
		
	}
}
