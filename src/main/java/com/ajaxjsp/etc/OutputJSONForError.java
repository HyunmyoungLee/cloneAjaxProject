package com.ajaxjsp.etc;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.simple.JSONObject;

public class OutputJSONForError {

	public static String outputJson(Exception e) {

		JSONObject json = new JSONObject();
		json.put("status", "fail");
		json.put("errorMsg", e.getMessage());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
		String outputDate = sdf.format(Calendar.getInstance().getTime());
		
		json.put("outputDate", outputDate);
		
		return json.toString();
	}

}
