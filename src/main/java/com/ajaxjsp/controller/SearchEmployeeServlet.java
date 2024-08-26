package com.ajaxjsp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
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
import com.ajaxjsp.etc.ResponseToJson;
import com.ajaxjsp.vo.EmployeeVO;

@WebServlet("/findEmpByName.do")
public class SearchEmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SearchEmployeeServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchName = request.getParameter("searchName");
		System.out.println("검색어 : " + searchName);
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		EmployeesDAO dao = EmployeesDAOImpl.getInstance();
		try {
			List<EmployeeVO> list = dao.selectByEmpName(searchName);
			String jsonString = ResponseToJson.makeJsonWithjsonSimple(list);
			out.print(jsonString);
			out.close();
			
		} catch (NamingException | SQLException e) {
			out.print(OutputJSONForError.outputJson(e));
		}finally {
			out.close();
		}
	}

}
