package com.hiringbell.authenticator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiringbell.authenticator.contract.ILoginService;
import com.hiringbell.authenticator.entity.*;
import com.hiringbell.authenticator.jwtconfig.JwtGateway;
import com.hiringbell.authenticator.model.ApplicationConstant;
import com.hiringbell.authenticator.model.JwtTokenModel;
import com.hiringbell.authenticator.model.LoginResponse;
import com.hiringbell.authenticator.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

@Service
public class LoginService implements ILoginService {
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeDetailRepository employeeDetailRepository;

    @Autowired
    EmployeeMedicalDetailRepository employeeMedicalDetailRepository;

    public LoginResponse userAuthetication(UserEntity user) throws Exception {
        Map<String, Object> result = jwtUtil.validateToken(user.getToken());
        if (result.isEmpty() || result.get("email") == null || result.get("email").equals(""))
            throw new Exception("Invalid email");

        String email = result.get("email").toString();
        var loginDetail = loginRepository.getLoginByEmailOrMobile("", email);
        if (loginDetail == null) {
            String name = result.get("name").toString();
            loginDetail = addEmployeeService(name, email);
        }
        return getLoginResponse(loginDetail);
    }

    public LoginResponse authenticateUserService(Login login) throws Exception {
        try {
            validateLoginDetail(login);
            Login loginDetail = null;
            loginDetail = loginRepository.getLoginByEmailOrMobile(login.getMobile(), login.getEmail());

            if (loginDetail == null)
                throw new Exception("Login detail not found");

            validateCredential(loginDetail, login);
            return getLoginResponse(loginDetail);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private Login addEmployeeService(String name, String email) throws Exception {
        Date utilDate = new Date();
        var currentDate = new Timestamp(utilDate.getTime());
        Employee employee = new Employee();
        var lastEmployeeId = this.employeeRepository.getLastEmployeeId();
        if (lastEmployeeId == null)
            employee.setEmployeeId(1L);
        else
            employee.setEmployeeId(lastEmployeeId.getEmployeeId()+1);

        String[] splitStr = name.split("\\s+");
        if (splitStr.length == 1)
            employee.setFirstName(splitStr[0]);
        else {
            employee.setFirstName(splitStr[0]);
            employee.setLastName(splitStr[1]);
        }
        employee.setEmail(email);
        employee.setRoleId(0);
        employee.setDesignationId(0);
        employee.setReporteeId(0);
        employee.setActive(true);
        employee.setCreatedOn(currentDate);
        this.employeeRepository.save(employee);

        Login loginDetail = new Login();
        var lastLoginRecord = this.loginRepository.getLastLoginRecord();
        if (lastLoginRecord == null){
            loginDetail.setLoginId(1L);
        }else {
            loginDetail.setLoginId(lastLoginRecord.getLoginId()+1);
        }
        loginDetail.setEmployeeId(employee.getEmployeeId());
        loginDetail.setEmail(email);
        loginDetail.setPassword(ApplicationConstant.DefaultPassword);
        loginDetail.setRoleId(0);
        loginDetail.setActive(true);
        loginDetail.setCreatedBy(1L);
        loginDetail.setCreatedOn(currentDate);
        this.loginRepository.save(loginDetail);

        EmployeeDetail employeeDetail = new EmployeeDetail();
        employeeDetail.setEmployeeId(employee.getEmployeeId());
        employeeDetail.setJobTypeId(0);
        employeeDetail.setExperienceInMonths(0);
        employeeDetail.setLastWorkingDate(utilDate);
        employeeDetail.setSalary(BigDecimal.ZERO);
        employeeDetail.setExpectedSalary(BigDecimal.ZERO);
        employeeDetail.setCreatedBy(1L);
        employeeDetail.setCreatedOn(currentDate);
        this.employeeDetailRepository.save(employeeDetail);

        EmployeeMedicalDetail employeeMedicalDetail = new EmployeeMedicalDetail();
        var lastEmployeeMedicalDetailRecord = this.employeeMedicalDetailRepository.getLastEmployeeMedicalDetailRecord();
        if (lastEmployeeMedicalDetailRecord == null){
            employeeMedicalDetail.setEmployeeMedicalDetailId(1L);
        }else {
            employeeMedicalDetail.setEmployeeMedicalDetailId(lastEmployeeMedicalDetailRecord.getEmployeeMedicalDetailId()+1);
        }
        employeeMedicalDetail.setEmployeeId(employee.getEmployeeId());
        employeeMedicalDetail.setMedicalConsultancyId(0);
        employeeMedicalDetail.setConsultedOn(utilDate);
        employeeMedicalDetail.setReferenceId(0L);
        employeeMedicalDetail.setReportId(0);
        employeeMedicalDetail.setCreatedBy(1L);
        employeeMedicalDetail.setCreatedOn(currentDate);
        this.employeeMedicalDetailRepository.save(employeeMedicalDetail);

        return loginDetail;
    }

    private LoginResponse getLoginResponse(Login loginDetail) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String loginDetailJson = mapper.writeValueAsString(loginDetail);
        JwtTokenModel jwtTokenModel = new JwtTokenModel();
        jwtTokenModel.setUserDetail(loginDetailJson);
        jwtTokenModel.setUserId(loginDetail.getEmployeeId());
        jwtTokenModel.setEmail(loginDetail.getEmail());
        jwtTokenModel.setCompanyCode("");
        switch (loginDetail.getRoleId()){
            case 1:
                jwtTokenModel.setRole(ApplicationConstant.Admin);
                break;
            case 3:
                jwtTokenModel.setRole(ApplicationConstant.Client);
                break;
            default:
                jwtTokenModel.setRole(ApplicationConstant.Employee);
        }

        JwtGateway jwtGateway = JwtGateway.getJwtGateway();
        String result = jwtGateway.generateJwtToken(jwtTokenModel);

        LoginResponse loginResponse = new LoginResponse();
        UserEntity userDetail = new UserEntity();
        Date oldDate = new Date(); // oldDate == current time
        final long hoursInMillis = 60L * 60L * 1000L;
        Date newDate = new Date(oldDate.getTime() + (2L * hoursInMillis)); // Adds 2 hours
        userDetail.setToken(result);
        userDetail.setTokenExpiryDuration(newDate);
        userDetail.setEmployeeId(loginDetail.getEmployeeId());
        userDetail.setEmail(loginDetail.getEmail());
        userDetail.setMobile(loginDetail.getMobile());
        userDetail.setRoleId(loginDetail.getRoleId());
        loginResponse.setUserDetail(userDetail);
        return loginResponse;
    }

    private void validateCredential(Login login, Login request) throws Exception {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String password = encoder.encode(request.getPassword());
//        if(!encoder.matches(password, login.getPassword())) {
//            user = userService.getByUserMobileService(request.getMobile());
//        } else {
//            throw new Exception("Invalid username or password.");
//        }
        if (!login.getPassword().equals(request.getPassword()))
            throw new Exception("Password is not matched");
    }

    private void validateLoginDetail(Login login) throws Exception {
        if (login.getPassword() == null || login.getPassword().isEmpty())
            throw new Exception("Password is required");

        if ((login.getEmail() == null || login.getEmail().isEmpty()) && (login.getMobile() == null || login.getMobile().isEmpty()))
            throw new Exception("Email or Mobile number is required");
    }
}
