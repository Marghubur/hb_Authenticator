package com.hiringbell.authenticator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiringbell.authenticator.contract.ILoginService;
import com.hiringbell.authenticator.entity.*;
import com.hiringbell.authenticator.jwtconfig.JwtGateway;
import com.hiringbell.authenticator.model.ApplicationConstant;
import com.hiringbell.authenticator.model.JwtTokenModel;
import com.hiringbell.authenticator.model.LoginResponse;
import com.hiringbell.authenticator.repository.UserDetailRepository;
import com.hiringbell.authenticator.repository.UserMedicalDetailRepository;
import com.hiringbell.authenticator.repository.UserRepository;
import com.hiringbell.authenticator.repository.LoginRepository;
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
    UserRepository userRepository;

    @Autowired
    UserDetailRepository userDetailRepository;

    @Autowired
    UserMedicalDetailRepository userMedicalDetailRepository;

    public LoginResponse userAuthetication(Login user) throws Exception {
        Map<String, Object> result = jwtUtil.validateToken(user.getToken());
        if (result.isEmpty() || result.get("email") == null || result.get("email").equals(""))
            throw new Exception("Invalid email");

        String email = result.get("email").toString();
        var loginDetail = loginRepository.getLoginByEmailOrMobile("", email);
        if (loginDetail == null) {
            String name = result.get("name").toString();
            loginDetail = addUserService(name, email);
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
    private Login addUserService(String name, String email) throws Exception {
        Date utilDate = new Date();
        var currentDate = new Timestamp(utilDate.getTime());
        User user = new User();
        var lastUserId = userRepository.getLastUserId();
        if (lastUserId == null)
            user.setUserId(1L);
        else
            user.setUserId(lastUserId.getUserId()+1);

        String[] splitStr = name.split("\\s+");
        if (splitStr.length == 1)
            user.setFirstName(splitStr[0]);
        else {
            user.setFirstName(splitStr[0]);
            user.setLastName(splitStr[1]);
        }
        user.setEmail(email);
        user.setRoleId(0);
        user.setDesignationId(0);
        user.setReporteeId(0);
        user.setActive(true);
        user.setCreatedOn(currentDate);
        userRepository.save(user);

        Login loginDetail = new Login();
        var lastLoginRecord = this.loginRepository.getLastLoginRecord();
        if (lastLoginRecord == null){
            loginDetail.setLoginId(1L);
        }else {
            loginDetail.setLoginId(lastLoginRecord.getLoginId()+1);
        }
        loginDetail.setUserId(user.getUserId());
        loginDetail.setEmail(email);
        loginDetail.setPassword(ApplicationConstant.DefaultPassword);
        loginDetail.setRoleId(0);
        loginDetail.setActive(true);
        loginDetail.setCreatedBy(1L);
        loginDetail.setCreatedOn(currentDate);
        this.loginRepository.save(loginDetail);

        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(user.getUserId());
        userDetail.setJobTypeId(0);
        userDetail.setExperienceInMonths(0);
        userDetail.setLastWorkingDate(utilDate);
        userDetail.setSalary(BigDecimal.ZERO);
        userDetail.setExpectedSalary(BigDecimal.ZERO);
        userDetail.setCreatedBy(1L);
        userDetail.setCreatedOn(currentDate);
        userDetailRepository.save(userDetail);

        UserMedicalDetail userMedicalDetail = new UserMedicalDetail();
        var lastuserMedicalDetailRecord = userMedicalDetailRepository.getLastUerMedicalDetailRecord();
        if (lastuserMedicalDetailRecord == null){
            userMedicalDetail.setUserMedicalDetailId(1L);
        }else {
            userMedicalDetail.setUserMedicalDetailId(lastuserMedicalDetailRecord.getUserMedicalDetailId()+1);
        }
        userMedicalDetail.setUserId(user.getUserId());
        userMedicalDetail.setMedicalConsultancyId(0);
        userMedicalDetail.setConsultedOn(utilDate);
        userMedicalDetail.setReferenceId(0L);
        userMedicalDetail.setReportId(0);
        userMedicalDetail.setCreatedBy(1L);
        userMedicalDetail.setCreatedOn(currentDate);
        userMedicalDetailRepository.save(userMedicalDetail);

        return loginDetail;
    }

    private LoginResponse getLoginResponse(Login loginDetail) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String loginDetailJson = mapper.writeValueAsString(loginDetail);
        JwtTokenModel jwtTokenModel = new JwtTokenModel();
        jwtTokenModel.setUserDetail(loginDetailJson);
        jwtTokenModel.setUserId(loginDetail.getUserId());
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
                jwtTokenModel.setRole(ApplicationConstant.User);
        }

        JwtGateway jwtGateway = JwtGateway.getJwtGateway();
        String result = jwtGateway.generateJwtToken(jwtTokenModel);

        LoginResponse loginResponse = new LoginResponse();
        Login userDetail = new Login();
        Date oldDate = new Date(); // oldDate == current time
        final long hoursInMillis = 60L * 60L * 1000L;
        Date newDate = new Date(oldDate.getTime() + (2L * hoursInMillis)); // Adds 2 hours
        userDetail.setToken(result);
        userDetail.setTokenExpiryDuration(newDate);
        userDetail.setUserId(loginDetail.getUserId());
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
