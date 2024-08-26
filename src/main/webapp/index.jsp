<%@page import="java.sql.Connection"%>
<%@page import="com.ajaxjsp.dao.DBConnection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
		let empData = null;
		let jobsData = null;
		let departmentsData = null;
		
	$(document).ready(function(){
		getAllEmployees("noOrder");
		getJobsData();
		getDepartmentsData();
		
		// 검색 + 정렬
		let searchName = $("#findEmpName").val();
		let orderValue = 'empNo'; // 디폴트 정렬방식
		
		//사원 이름으로 검색
		$("#findEmpName").keyup(function(){
			searchName = $(this).val();
			if(searchName.length > 1){ //검색어가 두글자 이상인 경우
				getAllEmp(searchName, orderValue);
			}
		});
		
		//정렬 방식이 변할 때
		$(".orderMethod").change(function(){
			orderValue = $(".orderMethod:checked").val();
			getAllEmp(searchName, orderValue);
		});
 		
		
		
		//직원추가 버튼 클릭시
		$(".writeIcon").click(function(){
			$("#writeJobId").html(makeJobSelection());
			$("#writeManager").html(makeManagerSelection());
			$("#writeDepartment").html(makeDepartmentSelection());
			$("#writeModal").show();
		});
		
		
		//직급 정보 입력시 (사원추가)
		$("#writeJobId").change(function(){
			console.log($(this).val());
			
			if($(this).val() != ''){
				let selectedJobId = $("#writeJobId option:selected").val();
// 				console.log(selectedJobId);
				
				makeSalInput(selectedJobId,"write");

			}
		});
		
		//직급 정보 입력시 (사원수정 시)
		$("#modifyJobId").change(function(){
			console.log($(this).val());
			
			if($(this).val() != ''){
				let selectedJobId = $("#modifyJobId option:selected").val();
//					console.log(selectedJobId);
				makeSalInput(selectedJobId,"modify");
			}
		});
		
		//사원 저장 버튼 클릭시
		$(".writeBtn").click(function(){
			let writeFirstName = $("#writeFirstName").val();
			let writeLastName = $("#writeLastName").val();
			let writeEmail = $("#writeEmail").val();
			let writePhoneNumber = $("#writePhoneNumber").val();
			let writeHireDate = $("#writeHireDate").val();
			let writeJobId = $("#writeJobId").val();
			let writeSalary = $("#writeSalary").val();
			let writeCommission = $("#writeCommission").val();
			let writeManager = $("#writeManager").val();
			let writeDepartment = $("#writeDepartment").val();
			
			let tmpEmp = {
				firstName :  writeFirstName,
				lastName : writeLastName,
				email : writeEmail,
				phoneNumber : writePhoneNumber,
				hireDate : writeHireDate,
				jobId : writeJobId,
				salary : writeSalary,
				commission : writeCommission,
				managerId : writeManager,
				departmentId : writeDepartment
			};
			console.log(tmpEmp);
			
			inputEmpValidate(tmpEmp);
		});
		
		//삭제 버튼 클릭시
		$(".deleteBtn").click(function(){
			let empNo = $("#delEmpNo").text();
			console.log("EMPNO : " + empNo);
			let url ="deleteEmp.do";
			$.ajax({
			    url: url, // 데이터가 송수신될 서버의 주소
			    type: "GET", // 통신 방식 (GET, POST, PUT, DELETE)
				data : {"empNo": empNo}, // 데이터 보내기
			    dataType: "json", // 수신 받을 데이터 타입 (MIME TYPE)
			    success: function (data) {
				    console.log(data);
				    if(data.status =="ERROR"){
						alert("저장 실패");
				    }else if(data.status == "success"){
						alert("삭제 성공");
				    }else if(data.status =="fail"){
						alert("삭제 실패");
				    }
				    $("#deleteModal").hide();
// 				    location.reload() //페이지 새로고침
// 				    initInputModal();
				    getAllEmployees();
				    
			    },
			    error: function () {},
			    complete: function () {},
			  });
		});
		
// 		// 이름으로 사원 검색하기 (엔터키를 치면 검색)
// 		$("#findEmpName").keyup(function(e){
// 			if(e.keyCode ==13){
// 				//엔터를 눌렀을 때
				
// 				//검색어 가져오기
// 				let searchName = $(this).val().toLowerCase();
// 				//검색 요청-응답
// 				$.ajax({
// 				    url: "findEmpByName.do", // 데이터가 송수신될 서버의 주소
// 				    type: "GET", // 통신 방식 (GET, POST, PUT, DELETE)
// 					data : {"searchName" : searchName}, // 데이터 보내기
// 				    dataType: "json", // 수신 받을 데이터 타입 (MIME TYPE)
// 				    success: function (data) {
// 					    console.log(data);
// 					    if(data.status == "success"){
// 							alert("검색 성공");
// 							outputEntireEmployees(data);
// 					    }
					    
// 				    },
// 				    error: function () {},
// 				    complete: function () {},
// 				  });
				
				
// 			}
// 		});
// 		// 정렬 방식 변경
// 		$(".orderMethod").click(function(){
// 			//선택된 정렬 방식 읽어오기
// 			let orderMethod = $(this).val();
// 			$.ajax({
// 			    url: "orderByEmp.do", // 데이터가 송수신될 서버의 주소
// 			    type: "GET", // 통신 방식 (GET, POST, PUT, DELETE)
// 				data : {"orderMethod" : orderMethod}, // 데이터 보내기
// 			    dataType: "json", // 수신 받을 데이터 타입 (MIME TYPE)
// 			    success: function (data) {
// 				    console.log(data);
// 				    if(data.status == "success"){
// 						outputEntireEmployees(data);
// 				    }
				    
// 			    },
// 			    error: function () {},
// 			    complete: function () {},
// 			  });
			
			
// // 			getAllEmployees($(this).val());
			
// 		});
		
		
		// 모달 닫기
		$(".closeModal").click(function(){
			$("#writeModal").hide();
			$("#modifyModal").hide();
		});
		

	});
	
	function getAllEmp(searchName, orderMethod){
		if(orderMethod == 'empNo'){
			orderMethod = 'employee_id';
		} else if(orderMethod == 'hireDate'){
			orderMethod = 'hire_date desc';
		} else {
			orderMethod = 'salary desc';
		}
		
		let url = "getAllEmp.do?orderMethod=" + orderMethod;
		console.log(searchName, orderMethod);
		
		if(searchName != ''){ // 검색어가 있을 때
			url += "&searchName=" + searchName.toLowerCase();
		}
		
		$.ajax({
		    url: url, // 데이터가 송수신될 서버의 주소
		    type: "GET", // 통신 방식 (GET, POST, PUT, DELETE)
		    dataType: "json", // 수신 받을 데이터 타입 (MIME TYPE)
		    success: function (data) {
		      // 통신이 성공하면 수행할 함수
		      console.log(data);
		      outputEntireEmployees(data);
		    },
		    error: function () {},
		    complete: function () {},
		  });
	}
	
	
	function inputEmpValidate(tmpEmp){
		//사원 저장시 유효성 검사
		// not null? (필수 항목)
		// 바인딩한 정보가 선택이 되어있는지?
		let isLastNameValid = checkNameValid(tmpEmp.lastName);
		let isEmailValid = checkEmailValid(tmpEmp.email);
		let isHireDateValid = checkHireDateValid(tmpEmp.hireDate);
		let isJobIdValid = checkJobIdValid(tmpEmp.jobId);
		let isManagerIdValid = checkManagerIdValid(tmpEmp.managerId);
		let isDepartmentIdValid = checkDepartmentIdValid(tmpEmp.departmentId);

		if(isLastNameValid && isEmailValid && isHireDateValid && isJobIdValid && isManagerIdValid && isDepartmentIdValid){
			let url ="saveEmp.do";
			$.ajax({
			    url: url, // 데이터가 송수신될 서버의 주소
			    type: "GET", // 통신 방식 (GET, POST, PUT, DELETE)
				data : tmpEmp, // 데이터 보내기
			    dataType: "json", // 수신 받을 데이터 타입 (MIME TYPE)
			    success: function (data) {
				    console.log(data);
				    if(data.status =="ERROR"){
						alert("저장 실패");
				    }else if(data.status == "SUCCESS"){
						alert("저장 성공");
				    }else if(data.status =="fail"){
						alert("예외 발생");
				    }
				    $("#writeModal").hide();
// 				    location.reload() //페이지 새로고침
				    initInputModal();
				    getAllEmployees();
				    
			    },
			    error: function () {},
			    complete: function () {},
			  });
		};
			
	}
	
	function initInputModal(){
		$("#writeFirstName").val("");
		$("#writeLastName").val("");
		$("#writeEmail").val("");
		$("#writePhoneNumber").val("");
		$("#writeHireDate").val("");
		$("#writeJobId").val("");
		$("#writeSalary").val("");
		$("#spanSal").text("");
		$("#writeCommission").val('0');
		$("#writeManager").val("");
	    $("#writeDepartment").val("");
	}
	function checkNameValid(lastName){
		let isNameValid = true;
		if(lastName.length < 1){
			printErrMsg('writeLastName', '성은 필수 항목 입니다.');
			isNameValid = false;
		}
// 		if(lastName!=''){
// 			return true;
// 		}else{
// 			$("#writeLastName").attr("placeholder","이름의 성 입력은 필수 입니다.");
// 			$("#writeLastName::-webkit-input-placeholder").css({'color' : 'red'});
// 		}
		return isNameValid;
	}
	
	function printErrMsg(id,msg){
		let errMsg = `<div class='errMsg'>\${msg}</div>`;
		$(errMsg).insertAfter($(`#\${id}`).parent());
		$(`#\${id}`).focus(); // 커서 이동
		$(".errMsg").hide(2000);
	}
	
	function checkEmailValid(email){

		//email :  not null & unique
		
		//not null 체크
		let isEmailNNValid = true;
		if(email == ''){
			printErrMsg('writeEmail', '이메일은 필수항목입니다.');
			isEmailNNValid = false;
		}
		
		// email 중복 검사
		let isEmailUKValid = true;
		$.each(empData.employees, function(i,e){
			if(e.EMAIL.toUpperCase() == email.toUpperCase()){
				// 중복
				printErrMsg('writeEmail', '이미 존재하는 메일입니다.');
				isEmailUKValid = false;
			}
		})
		// 유효성 검사
// 		let isEmailFormatValid = true;
// 		if(!email.match(/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i)){
// 		printErrMsg('writeEmail', '이메일 형식에 맞게 작성해주세요.')
// 		isEmailFormatValid = false;
// 		}
		if(isEmailNNValid && isEmailUKValid){
			return true;
		} else{
			return false;
		}
	}
	function checkModifyEmailValid(email, empId){

		//email :  not null & unique
		
		//not null 체크
		let isEmailNNValid = true;
		if(email == ''){
			printErrMsg('writeEmail', '이메일은 필수항목입니다.');
			isEmailNNValid = false;
		}
		
		// email 중복 검사 
		let isEmailUKValid = true;
		$.each(empData.employees, function(i,e){
			if(e.EMAIL.toUpperCase() == email.toUpperCase() && e.EMPLOYEE_ID != empId){
				// 중복
				printErrMsg('writeEmail', '이미 존재하는 메일입니다.');
				isEmailUKValid = false;
			}
		})
		// 유효성 검사
// 		let isEmailFormatValid = true;
// 		if(!email.match(/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i)){
// 		printErrMsg('writeEmail', '이메일 형식에 맞게 작성해주세요.')
// 		isEmailFormatValid = false;
// 		}
		if(isEmailNNValid && isEmailUKValid){
			return true;
		} else{
			return false;
		}
	}
	function checkHireDateValid(hireDate){
		let isHireDateValid = true;
		if(hireDate==''){
			printErrMsg('writeHireDate', '입사일은 필수 항목입니다.');
			isHireDateValid = false;
		}
		return isHireDateValid;
	}
	function checkJobIdValid(jobId){
		let isJobIdValid = true;
		if(jobId==''){
			printErrMsg('writeJobId', '직급 선택은 필수 항목입니다.');
			isJobIdValid = false;
		}
		
		return isJobIdValid;
	}
	function checkManagerIdValid(managerId){
		let isManagerIdValid = true;
		if(managerId==''){
			printErrMsg('writeManager', '매니저 선택은 필수 항목입니다.');
			isManagerIdValid = false;
		}
		
		return isManagerIdValid;
	}
	function checkDepartmentIdValid(departmentId){
		let isDepartmentIdValid = true;
		if(departmentId==''){
			printErrMsg('writeDepartment', '부서 선택은 필수 항목입니다.');
			isDepartmentIdValid = false;
		}
		
		return isDepartmentIdValid;
	}
	
	function makeSalInput(inputJobId, mode){
		//mode: "write", "modify"
		let minSal = 0;
		let maxSal = 0;
		let avgSal = 0;
		
		// 선택된 job_id의 최소급여, 최대급여
		$.each(jobsData.JOBS, function(i,e){
			if(inputJobId == e.JOB_ID){
				minSal = e.MIN_SALARY;
				maxSal = e.MAX_SALARY;
				avgSal = (minSal + maxSal) / 2;
			}
		});
		
		console.log(minSal, maxSal);
		
		$(`#\${mode}Salary`).attr("min",minSal);
		$(`#\${mode}Salary`).attr("max",maxSal);
		$(`#\${mode}Salary`).attr("step",10);
		if(mode == 'write'){
// 		$("#writeSalary").attr("min",minSal);
// 		$("#writeSalary").attr("max",maxSal);
// 		$("#writeSalary").attr("step", 10);
		$("#writeSalary").val(avgSal);
		$("#spanSal").html(avgSal);

		}
// 		else if (mmode == 'modify'){
// 			$("#modifySalary").attr("min",minSal);
// 			$("#modifySalary").attr("max",maxSal);
// 			$("#modifySalary").attr("step", 10);
// 		}
		
	}
	function changeSal(sal){
		$("#spanSal").html(sal);
	}
	
	function changeModifySal(sal){
		$("#spanModifySal").html(sal);
	}
	
	function makeDepartmentSelection(){
		let output = "<option value =''>-- 부서를 선택하세요 -- </option>";
		$.each(departmentsData.DEPARTMENTS, function(i, element){
			output += "<option value ='" + element.DEPARTMENT_ID + "'>" + element.DEPARTMENT_NAME + "</option>";
		});
		
		return output;
	}
	
	function makeManagerSelection(){
		let output = "<option value=''>-- 매니저를 선택하세요 -- </option>";
		$.each(empData.employees, function(i, element){
			output += "<option value = '" + element.EMPLOYEE_ID + "'>" + element.FIRST_NAME + ", " + element.LAST_NAME + "</option>";

		});
		
		return output;
	}
	function makeJobSelection(){
		//직급 (job_id)정보를 동적으로 select태그에 바인딩
		let output = "<option value=''>-- 직급을 선택하세요 --</option>";
		$.each(jobsData.JOBS, function(i, element){
			output += "<option value = '" + element.JOB_ID +"'>"+ element.JOB_TITLE +"</option>"; 
		});
		return output;
	}
	
	function getDepartmentsData(){
		let url ="getDepartmentsData.do";
		$.ajax({
		    url: url, // 데이터가 송수신될 서버의 주소
		    type: "GET", // 통신 방식 (GET, POST, PUT, DELETE)
		    dataType: "json", // 수신 받을 데이터 타입 (MIME TYPE)
		    success: function (data) {
			    console.log(data);
		      // 통신이 성공하면 수행할 함수
		      if(data.status == "fail"){
				alert("데이터를 불러오지 못했습니다.")
		      }else if(data.status == "success"){
			    departmentsData = data;
		      }
		    },
		    error: function () {},
		    complete: function () {},
		  });
	}
	function getJobsData(){
		let url ="getJobsData.do";
		$.ajax({
		    url: url, // 데이터가 송수신될 서버의 주소
		    type: "GET", // 통신 방식 (GET, POST, PUT, DELETE)
		    dataType: "json", // 수신 받을 데이터 타입 (MIME TYPE)
		    success: function (data) {
			    console.log(data);
		      // 통신이 성공하면 수행할 함수
		      if(data.status == "fail"){
				alert("데이터를 불러오지 못했습니다.")
		      }else if(data.status == "success"){
			    jobsData = data;
		      }
		    },
		    error: function () {},
		    complete: function () {},
		  });
	}
	
	function getAllEmployees(order){
		let url ="getAllEmployees.do";
		$.ajax({
		    url: url, // 데이터가 송수신될 서버의 주소
		    type: "GET", // 통신 방식 (GET, POST, PUT, DELETE)
			data : {"order" : order},
		    dataType: "json", // 수신 받을 데이터 타입 (MIME TYPE)
		    success: function (data) {
		      // 통신이 성공하면 수행할 함수
		      console.log(data);
		      empData = data;
		      outputEntireEmployees(data);
		    },
		    error: function () {},
		    complete: function () {},
		  });
	}
	
	function outputEntireEmployees(json){
		//모든 직원 데이터를 파싱하여 출력
		$("#outputDate").html("출력일시 : " + json.outputDate);
		$("#outputCnt").html("총 직원수 : " + json.count);
		
		let output = "<table class = 'table table-striped'><thead>";
		output += "<tr><th>사원번호</th><th>이름</th><th>이메일</th><th>전화번호</th><th>입사일</th><th>직급</th><th>급여</th><th>커미션(%)</th><th>매니저</th><th>부서명</th><th>수정</th><th>삭제</th></tr></thead>";
		output +="<tbody>";
		$.each(json.employees, function(i,item){
			output += "<tr><td>" + item.EMPLOYEE_ID + "</td>";
			output += "<td>" + item.LAST_NAME +", " + item.FIRST_NAME + "</td>";
			output += "<td>" + item.EMAIL + "</td>";
			output += "<td>" + item.PHONE_NUMBER + "</td>";
			output += "<td>" + item.HIRE_DATE + "</td>";
			output += "<td>" + item.JOB_ID + "</td>";
			output += "<td>$" + (item.SALARY).toLocaleString("en-US") + "</td>";
			output += "<td>" + (item.COMMISSION_PCT) * 100 + "</td>";
			
			
			//매니저 이름 찾기
			let managerId = item.MANAGER_ID;
			let managerName = "";
			
			$.each(empData.employees, function(i,mItem){
				if(managerId == mItem.EMPLOYEE_ID){
					managerName = mItem.LAST_NAME +", " + mItem.FIRST_NAME;
				}
			});
			output += "<td>" + managerName + "</td>";
			
			output += "<td>" + item.DEPARTMENT_NAME + "</td>";
			output += "<td>" + "<img src='img/modify.png' onclick='showModifyModal("+item.EMPLOYEE_ID+");'/>" + "</td>";
			output += "<td>" + "<img src='img/delete.png' onclick='showDeleteModal("+item.EMPLOYEE_ID+");'/>" + "</td>";
			output += "</tr>";
		});
		
		output += "</tbody></table>";
		$(".empInfo").html(output);
	}
	
	function showModifyModal(empNo){
		//사원 수정 모달창 띄우기
		$("#modifyModal").show();
		$("#modifyJobId").html(makeJobSelection());
		$("#modifyManager").html(makeManagerSelection());
		$("#modifyDepartment").html(makeDepartmentSelection());
		initInputModifyModal(empNo);
		
		//사원 정보 수정 버튼 클릭시 
		$(".modifyBtn").click(function(){
			let modifyFirstName = $("#modifyFirstName").val();
			let modifyLastName = $("#modifyLastName").val();
			let modifyEmail = $("#modifyEmail").val();
			let modifyPhoneNumber = $("#modifyPhoneNumber").val();
			let modifyHireDate = $("#modifyHireDate").val();
			let modifyJobId = $("#modifyJobId").val();
			let modifySalary = $("#modifySalary").val();
			let modifyCommission = $("#modifyCommission").val();
			let modifyManager = $("#modifyManager").val();
			let modifyDepartment = $("#modifyDepartment").val();
			
			let modifyEmp = {
				employeeId : empNo,
				firstName :  modifyFirstName,
				lastName : modifyLastName,
				email : modifyEmail,
				phoneNumber : modifyPhoneNumber,
				hireDate : modifyHireDate,
				jobId : modifyJobId,
				salary : modifySalary,
				commission : modifyCommission,
				managerId : modifyManager,
				departmentId : modifyDepartment
			};
			
			inputModifyValidate(modifyEmp, empNo);
		});
		
	}
	
	function inputModifyValidate(modifyEmp, empNo){
		let isLastNameValid = checkNameValid(modifyEmp.lastName);
		let isEmailValid = checkModifyEmailValid(modifyEmp.email, empNo);
		let isHireDateValid = checkHireDateValid(modifyEmp.hireDate);
		let isJobIdValid = checkJobIdValid(modifyEmp.jobId);
		let isManagerIdValid = checkManagerIdValid(modifyEmp.managerId);
		let isDepartmentIdValid = checkDepartmentIdValid(modifyEmp.departmentId);
		
		
		console.log("버튼 실행");
		if(isLastNameValid && isEmailValid && isHireDateValid && isJobIdValid && isManagerIdValid && isDepartmentIdValid){
			let url = "modifyEmp.do";
			$.ajax({
			    url: url, // 데이터가 송수신될 서버의 주소
			    type: "POST", // 통신 방식 (GET, POST, PUT, DELETE)
			    data : modifyEmp, // 데이터 보내기
			    dataType: "json", // 수신 받을 데이터 타입 (MIME TYPE)
			    success: function (data) {
				    console.log(data);
				    if(data.status =="ERROR"){
						alert("수정 실패");
				    }else if(data.status == "success-modify"){
						alert("수정 성공");
				    }else if(data.status =="fail-modify"){
						alert("예외 발생");
				    }
				    $("#modifyModal").hide();
// 				    location.reload() //페이지 새로고침
// 				    initInputModal();
				    getAllEmployees("noOrder");
				    
			    },
			    error: function () {},
			    complete: function () {},
			  });
		};
		
	}
	function showDeleteModal(empNo){
		$("#deleteModal").show();
		$("#delEmpNo").html(empNo);
		
	}
	
	function initInputModifyModal(empNo){

		let url ="getEmployee.do";
		$.ajax({
		    url: url, // 데이터가 송수신될 서버의 주소
		    type: "GET", // 통신 방식 (GET, POST, PUT, DELETE)
			data :{"empNo" : empNo}, // 보내는 데이터
		    dataType: "json", // 수신 받을 데이터 타입 (MIME TYPE)
		    success: function (data) {
		      // 통신이 성공하면 수행할 함수
		      console.log(data);
		      if(data.status == 'fail'){
					alert("데이터를 불러오지 못했습니다.");
		      }else if(data.status =='success')
					bindingDataModifyModal(data);
		    },
		    error: function () {},
		    complete: function () {},
		  });
		
		
		$("#modifyEmployeeId").val(empNo);
// 		$.each(empData.employees, function(i,e){
// 		if(e.EMPLOYEE_ID == empNo){
// 			$("#modifyFirstName").val(e.FIRST_NAME);
// 			$("#modifyLastName").val(e.LAST_NAME);
// 			$("#modifyEmail").val(e.EMAIL);
// 			$("#modifyPhoneNumber").val(e.PHONE_NUMBER);
// 			$("#modifyHireDate").val(e.HIRE_DATE);
// 			$("#modifyJobId").val(e.JOB_ID);
// 			$("#modifyCommission").val(e.COMMISSION_PCT);
// 			$("#modifyManager").val(e.MANAGER_ID);
// 			$("#modifyDepartment").val(e.DEPARTMENT_ID);
			
// 			initModifySalary(e.JOB_ID, e.SALARY);
// 		}
		
// 		});

	}
// 	function initModifySalary(jobId, salary){
// 		$.each(jobsData.JOBS, function(i,e){
// 			if(e.JOB_ID == jobId){
// 				$("#modifySalary").attr("min", e.MIN_SALARY);
// 				$("#modifySalary").attr("max", e.MAX_SALARY);
// 				$("#modifySalary").val(salary);
// 				$("#spanModifySal").html(salary);
// 			}
// 		});
// 	}
	
	//사원 정보 바인딩
	function bindingDataModifyModal(data){
		$("#modifyFirstName").val(data.employee.FIRST_NAME);
		$("#modifyLastName").val(data.employee.LAST_NAME);
		$("#modifyEmail").val(data.employee.EMAIL);
		$("#modifyPhoneNumber").val(data.employee.PHONE_NUMBER);
		$("#modifyHireDate").val(data.employee.HIRE_DATE);
		$("#modifyJobId").val(data.employee.JOB_ID);
		$("#spanModifySal").html((data.employee.SALARY).toLocaleString("en-US",{style:'currency',currency:'USD'}));

		makeSalInput(data.employee.JOB_ID,"modify");
		$("#modifySalary").val(data.employee.SALARY);
		$("#modifyCommission").val(data.employee.COMMISSION_PCT);
		$("#modifyManager").val(data.employee.MANAGER_ID);
		$("#modifyDepartment").val(data.employee.DEPARTMENT_ID);
	}
	
	
	
</script>
<style>
	.writeIcon{
		width: 50px;
		height: 50px;
		position: fixed;
		right : 50px;
		top: 20px;
	}
	.input-group-text{
		width : 120px;
	}
	
</style>
</head>
<body>
<%-- <c:set var = "a" value="aaa"></c:set> --%>
<%-- <div>${a }</div> --%>
<!-- <script type="text/javascript"> -->
<!-- // 	let b = 'bbb'; -->
<%-- // 	console.log(`\${b}`); --%>
<%-- // 	console.log(`${a}`); --%>
<!-- </script> -->
	<div class="container">
		<h1>Employees  with AJAX </h1>
		<div>
			<div id="outputDate" class="genInfo"></div>
			<div id="outputCnt" class="genInfo"></div>
		</div>
		<div>
		 <input type="text" class="form-control" placeholder="찾을 사원의 이름을 입력하세요..." id="findEmpName">
		</div>
		
<!-- 		정렬방식 선택 -->
		<div class="form-check">
  <input type="radio" class="form-check-input orderMethod" id="radio1" name="optradio" value="empNo" checked>사번순 (오름차순)
  <label class="form-check-label" for="radio1"></label>
</div>
<div class="form-check">
  <input type="radio" class="form-check-input orderMethod" id="radio2" name="optradio" value="hireDate">입사일순 (내림차순)
  <label class="form-check-label" for="radio2"></label>
</div>
<div class="form-check">
  <input type="radio" class="form-check-input orderMethod" id="radio3" name="optradio" value ="salary">급여순(내림차순)
  <label class="form-check-label"></label>
</div>
		
		<div class="empInfo">
			
		</div>
		<div class="writeIcon"><img src="img/addPerson.png" alt="insertEmp"/></div>
		<!-- 사원 입력을 위한 모달창 -->
<div class="modal" id="writeModal">
  <div class="modal-dialog">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">사원 입력</h4>
        <button type="button" class="btn-close closeModal" data-bs-dismiss="modal"></button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
        <div class="input-group mb-3">
        	<span class="input-group-text">이름</span>
        	<input type ="text" class="form-control" placeholder="first name..." id="writeFirstName"/>
        </div>
        <div class="input-group mb-3">
        	<span class="input-group-text">성</span>
        	<input type ="text" class="form-control" placeholder="last name..." id ="writeLastName"/>
        </div>
        <div class="input-group mb-3">
        	<span class="input-group-text">이메일</span>
        	<input type ="text" class="form-control" placeholder="email..." id="writeEmail"/>
        </div>
        <div class="input-group mb-3">
        	<span class="input-group-text">전화번호</span>
        	<input type ="text" class="form-control" placeholder="phone number..." id="writePhoneNumber"/>
        </div>
        <div class="input-group mb-3">
        	<span class="input-group-text">입사일</span>
        	<input type ="date" class="form-control" placeholder="hire date..." id="writeHireDate"/>
        </div>
        <div class="input-group mb-3">
        	<span class="input-group-text">직급</span>
        	<select class="form-select" id="writeJobId">
        		
        	</select>
        </div>
        <div class="input-group mb-3">
        	<span class="input-group-text">급여</span>
        	<span id="spanSal"></span>
        	<input type ="range" class="form-range"  id="writeSalary" onchange="changeSal(this.value);"/>
        </div>
        <div class="input-group mb-3">
        	<span class="input-group-text">커미션</span>
        	<input type ="number" class="form-control" value = "0" min = "0.01" max = "0.99" step ="0.01" id="writeCommission"/>
        </div>
        
        <div class="input-group mb-3">
        	<span class="input-group-text">매니저</span>
        	<select class="form-select" id="writeManager">
        		
        	</select>
        </div>
        <div class="input-group mb-3">
        	<span class="input-group-text">부서명</span>
        	<select class="form-select" id="writeDepartment">
        		
        	</select>
        </div>
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
        <button type="button" class="btn btn-primary writeBtn" data-bs-dismiss="modal">Save</button>
        <button type="button" class="btn btn-danger closeModal" data-bs-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>

  <!-- 사원 수정을 위한 모달창 -->
<div class="modal" id="modifyModal">
  <div class="modal-dialog">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">사원 수정</h4>
        <button type="button" class="btn-close closeModal" data-bs-dismiss="modal"></button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
        <div class="input-group mb-3">
        	<span class="input-group-text">사번</span>
        	<input type ="text" class="form-control"  id="modifyEmployeeId" readonly/>
        </div>
        <div class="input-group mb-3">
        	<span class="input-group-text">이름</span>
        	<input type ="text" class="form-control" placeholder="first name..." id="modifyFirstName"/>
        </div>
        <div class="input-group mb-3">
        	<span class="input-group-text">성</span>
        	<input type ="text" class="form-control" placeholder="last name..." id ="modifyLastName"/>
        </div>
        <div class="input-group mb-3">
        	<span class="input-group-text">이메일</span>
        	<input type ="text" class="form-control" placeholder="email..." id="modifyEmail"/>
        </div>
        <div class="input-group mb-3">
        	<span class="input-group-text">전화번호</span>
        	<input type ="text" class="form-control" placeholder="phone number..." id="modifyPhoneNumber"/>
        </div>
        <div class="input-group mb-3">
        	<span class="input-group-text">입사일</span>
        	<input type ="date" class="form-control" placeholder="hire date..." id="modifyHireDate"/>
        </div>
        <div class="input-group mb-3">
        	<span class="input-group-text">직급</span>
        	<select class="form-select" id="modifyJobId">
        		
        	</select>
        </div>
        <div class="input-group mb-3">
        	<span class="input-group-text">급여</span>
        	<span id="spanModifySal"></span>
        	<input type ="range" class="form-range"  id="modifySalary" onchange="changeModifySal(this.value);"/>
        </div>
        <div class="input-group mb-3">
        	<span class="input-group-text">커미션</span>
        	<input type ="number" class="form-control" value = "0" min = "0.01" max = "0.99" step ="0.01" id="modifyCommission"/>
        </div>
        
        <div class="input-group mb-3">
        	<span class="input-group-text">매니저</span>
        	<select class="form-select" id="modifyManager">
        		
        	</select>
        </div>
        <div class="input-group mb-3">
        	<span class="input-group-text">부서명</span>
        	<select class="form-select" id="modifyDepartment">
        		
        	</select>
        </div>
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
        <button type="button" class="btn btn-success modifyBtn" data-bs-dismiss="modal">Save</button>
        <button type="button" class="btn btn-danger closeModal" data-bs-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
  </div>
  <!-- 사원 삭제 모달 -->
<div class="modal" id="deleteModal">
  <div class="modal-dialog">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">사원 삭제</h4>
        <button type="button" class="btn-close closeModal" data-bs-dismiss="modal"></button>
      </div>

      <!-- Modal body -->
      <div class="modal-body">
        <div>
        	<span id ="delEmpNo"></span>번 사원을 정말 삭제하시겠습니까?
        </div>
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
      <button type="button" class="btn btn-success deleteBtn" data-bs-dismiss="modal">삭제</button>
        <button type="button" class="btn btn-danger closeModal" data-bs-dismiss="modal">닫기</button>
      </div>

    </div>
  </div>
</div>
	
</body>
</html>