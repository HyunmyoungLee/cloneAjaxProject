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
import com.ajaxjsp.vo.JobsVO;

@WebServlet("/getJobsData.do")
public class GetJobsDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetJobsDataServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("jobs 서블릿 호출");
		response.setContentType("application/json; charset=utf-8");
		EmployeesDAO dao = EmployeesDAOImpl.getInstance();
		PrintWriter out = response.getWriter();
		try {
			List<JobsVO> list = dao.selectAllJobs();
			JSONObject json = new JSONObject();
			json.put("status", "success");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
			String outputDate = sdf.format(Calendar.getInstance().getTime());
			json.put("outputDate", outputDate);
			json.put("count", list.size());
			
			JSONArray jobs = new JSONArray();
			
			if(list.size() > 0) {
				for(JobsVO j : list) {
					JSONObject job = new JSONObject();
					job.put("JOB_ID", j.getJob_id());
					job.put("JOB_TITLE", j.getJob_title());
					job.put("MIN_SALARY", j.getMin_salary());
					job.put("MAX_SALARY", j.getMax_salary());
					
					jobs.add(job);
					}
				json.put("JOBS", jobs);
			}
			out.print(json.toJSONString());
		} catch (NamingException | SQLException e) {
			out.print(OutputJSONForError.outputJson(e));
		}finally {
			out.close();
		}
	}

}
