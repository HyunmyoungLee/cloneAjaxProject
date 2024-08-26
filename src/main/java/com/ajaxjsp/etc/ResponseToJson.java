package com.ajaxjsp.etc;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ajaxjsp.vo.EmployeeVO;

public class ResponseToJson {

	public static String makeJsonWithjsonSimple(List<EmployeeVO> list) {
		
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
				employee.put("DEPARTMENT_ID", e.getDepartment_id());
				employee.put("DEPARTMENT_NAME", e.getDepartment_name());
				
				employees.add(employee);
			}
			
			json.put("employees", employees);
		}
		return json.toJSONString();
	}
	
}
