select e.*, d.department_name from employees e inner join departments d
on e.department_id = d.department_id;

-- 모든 직급 가져오기
select * from jobs;

    -- 사원 저장하기 저장 프로시저
    create or replace PROCEDURE proc_saveEmp
    (
     pFIRST_NAME IN EMPLOYEES.FIRST_NAME%TYPE,
     pLAST_NAME IN EMPLOYEES.LAST_NAME%TYPE,
     pEMAIL IN EMPLOYEES.EMAIL%TYPE,
     pPHONE_NUMBER IN EMPLOYEES.PHONE_NUMBER%TYPE,
     pHIRE_DATE IN EMPLOYEES.HIRE_DATE%TYPE,
     pJOB_ID IN EMPLOYEES.JOB_ID%TYPE,
     pSALARY IN EMPLOYEES.SALARY%TYPE,
     pCOMMISSION_PCT IN EMPLOYEES.COMMISSION_PCT%TYPE,
     pMANAGER_ID IN EMPLOYEES.MANAGER_ID%TYPE,
     pDEPARTMENT_ID IN EMPLOYEES.DEPARTMENT_ID%TYPE,
     RESULT OUT VARCHAR2 
    )
    AS
    tmp_empId EMPLOYEES.EMPLOYEE_ID%TYPE;
    BEGIN
    SELECT MAX(EMPLOYEE_ID) + 1 INTO TMP_EMPID FROM EMPLOYEES;
    INSERT INTO EMPLOYEES 
    (EMPLOYEE_ID, FIRST_NAME, LAST_NAME,EMAIL,PHONE_NUMBER,  
    HIRE_DATE,JOB_ID, SALARY,COMMISSION_PCT,MANAGER_ID,DEPARTMENT_ID)
    VALUES
    (TMP_EMPID, INITCAP(pFIRST_NAME), INITCAP(pLAST_NAME),UPPER(pEMAIL),pPHONE_NUMBER,  pHIRE_DATE,pJOB_ID
    ,pSALARY,pCOMMISSION_PCT,pMANAGER_ID,pDEPARTMENT_ID);
    
    RESULT := 'SUCCESS';
    COMMIT;
    
    EXCEPTION --BEGIN 부분에 있는 DML문장을 실행할 때 예외가 나면 처리될 문장
        WHEN OTHERS THEN -- 모든 예외에 대해서... 다음의 문장을 실행
        RESULT := 'ERROR';
        ROLLBACK;
    END;