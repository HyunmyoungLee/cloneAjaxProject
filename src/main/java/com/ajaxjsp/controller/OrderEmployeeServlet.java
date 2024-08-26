package com.ajaxjsp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjsp.dao.EmployeesDAO;
import com.ajaxjsp.dao.EmployeesDAOImpl;
import com.ajaxjsp.dto.EmployeeDTO;
import com.ajaxjsp.etc.OutputJSONForError;
import com.ajaxjsp.etc.ResponseToJson;
import com.ajaxjsp.vo.EmployeeVO;

@WebServlet("/orderByEmp.do")
public class OrderEmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public OrderEmployeeServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String orderMethod = request.getParameter("orderMethod");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		EmployeesDAO dao = EmployeesDAOImpl.getInstance();
		
		
		try {
			List<EmployeeVO> list = dao.orderEmp(orderMethod);
			System.out.println(ResponseToJson.makeJsonWithjsonSimple(list));
			out.print(ResponseToJson.makeJsonWithjsonSimple(list));
			
			
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
			OutputJSONForError.outputJson(e);
		}finally {
			out.flush();
			out.close();
			
		}
	}

	

}
