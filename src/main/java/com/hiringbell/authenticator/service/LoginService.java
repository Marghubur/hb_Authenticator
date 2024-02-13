package com.hiringbell.authenticator.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiringbell.authenticator.contract.ILoginService;
import com.hiringbell.authenticator.db.LowLevelExecution;
import com.hiringbell.authenticator.entity.*;
import com.hiringbell.authenticator.jwtconfig.JwtGateway;
import com.hiringbell.authenticator.model.ApplicationConstant;
import com.hiringbell.authenticator.model.DbParameters;
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
import java.sql.Types;
import java.util.*;

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

    @Autowired
    LowLevelExecution lowLevelExecution;
    @Autowired
    ObjectMapper objectMapper;

    public LoginResponse userAuthetication(User user) throws Exception {
        Map<String, Object> result = jwtUtil.validateToken(user.getToken());
        if (result.isEmpty() || result.get("email") == null || result.get("email").equals(""))
            throw new Exception("Invalid email");

        String email = result.get("email").toString();
        var data = getgUserByEmailOrMobile(email, "");
        User userdetail = null;
        if (data== null || data.get("LoginDetail") == null) {
            String name = result.get("name").toString();
            userdetail = addUserService(name, email);
        } else {
            userdetail = (User) data.get("UserDetail");
        }
        return getLoginResponse(userdetail, 0);
    }

    public LoginResponse authenticateUserService(Login login) throws Exception {
        try {
            validateLoginDetail(login);
            var data = getgUserByEmailOrMobile(login.getEmail(), login.getMobile());
            if (data == null || data.get("LoginDetail") == null)
                throw new Exception("Login detail not found");

            Login loginDetail = (Login) data.get("LoginDetail");
            if (loginDetail == null)
                throw new Exception("Login detail not found");

            validateCredential(loginDetail, login);
            User user = (User) data.get("UserDetail");
            return getLoginResponse(user, login.getRoleId());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private Map<String, Object> getgUserByEmailOrMobile(String email, String mobile) throws Exception {
        var dataSet = lowLevelExecution.executeProcedure("sp_Userlogin_Auth",
                Arrays.asList(
                        new DbParameters("_Mobile", mobile, Types.VARCHAR),
                        new DbParameters("_Email", email, Types.VARCHAR)
                )
        );
        if (dataSet == null || dataSet.size() != 3)
            throw new Exception("Fail to get user detail. Please contact to admin.");

        List<User> users = objectMapper.convertValue(dataSet.get("#result-set-1"), new TypeReference< List<User>>() {});
        List<Login> logins = objectMapper.convertValue(dataSet.get("#result-set-2"), new TypeReference< List<Login>>() {});
        if (users.size() == 0 || logins.size() == 0)
            return null;

        Map<String, Object> response = new HashMap<>();
        response.put("UserDetail", users.get(0));
        response.put("LoginDetail", logins.get(0));
        return  response;
    }

    @Transactional(rollbackFor = Exception.class)
    private User addUserService(String name, String email) throws Exception {
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
        user.setFriends("[]");
        user.setFollowers("[]");
        user.setJobCategoryId(0);
        user.setCategoryTypeIds("[]");
        user.setJobLocationIds("[]");
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
        loginDetail.setCreatedBy(user.getUserId());
        loginDetail.setCreatedOn(currentDate);
        this.loginRepository.save(loginDetail);

        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(user.getUserId());
        userDetail.setJobTypeId(0);
        userDetail.setExperienceInMonths(0);
        userDetail.setLastWorkingDate(currentDate);
        userDetail.setSalary(BigDecimal.ZERO);
        userDetail.setExpectedSalary(BigDecimal.ZERO);
        userDetail.setCreatedBy(user.getUserId());
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
        userMedicalDetail.setConsultedOn(currentDate);
        userMedicalDetail.setReferenceId(0L);
        userMedicalDetail.setReportId(0);
        userMedicalDetail.setCreatedBy(user.getUserId());
        userMedicalDetail.setCreatedOn(currentDate);
        userMedicalDetailRepository.save(userMedicalDetail);

        return user;
    }

    private LoginResponse getLoginResponse(User user, int roleId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String userDetailJson = mapper.writeValueAsString(user);
        JwtTokenModel jwtTokenModel = new JwtTokenModel();
        jwtTokenModel.setUserDetail(userDetailJson);
        jwtTokenModel.setUserId(user.getUserId());
        jwtTokenModel.setEmail(user.getEmail());
        jwtTokenModel.setCompanyCode("");
        switch (roleId){
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
        Date oldDate = new Date(); // oldDate == current time
        final long hoursInMillis = 60L * 60L * 1000L;
        Date newDate = new Date(oldDate.getTime() + (2L * hoursInMillis)); // Adds 2 hours
        user.setToken(result);
        user.setTokenExpiryDuration(newDate);
        loginResponse.setUserDetail(user);
        loginResponse.setNewUser(false);
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

    @Transactional(rollbackFor = Exception.class)
    public String signupService(Login login) {
        Date utilDate = new Date();
        var currentDate = new Timestamp(utilDate.getTime());
        User user = new User();
        var lastUserId = userRepository.getLastUserId();
        if (lastUserId == null)
            user.setUserId(1L);
        else
            user.setUserId(lastUserId.getUserId()+1);

        String[] splitStr = login.getFullName().split("\\s+");
        if (splitStr.length == 1)
            user.setFirstName(splitStr[0]);
        else {
            user.setFirstName(splitStr[0]);
            user.setLastName(splitStr[1]);
        }
        user.setEmail(login.getEmail());
        user.setMobile(login.getMobile());
        user.setRoleId(0);
        user.setDesignationId(0);
        user.setReporteeId(0);
        user.setActive(true);
        user.setFriends("[]");
        user.setFollowers("[]");
        user.setJobCategoryId(0);
        user.setCategoryTypeIds("[]");
        user.setJobLocationIds("[]");
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
        loginDetail.setEmail(login.getEmail());
        loginDetail.setPassword(login.getPassword());
        loginDetail.setMobile(login.getMobile());
        loginDetail.setRoleId(0);
        loginDetail.setActive(true);
        loginDetail.setCreatedBy(user.getUserId());
        loginDetail.setCreatedOn(currentDate);
        this.loginRepository.save(loginDetail);

        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(user.getUserId());
        userDetail.setJobTypeId(0);
        userDetail.setExperienceInMonths(0);
        userDetail.setLastWorkingDate(currentDate);
        userDetail.setSalary(BigDecimal.ZERO);
        userDetail.setExpectedSalary(BigDecimal.ZERO);
        userDetail.setCreatedBy(user.getUserId());
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
        userMedicalDetail.setConsultedOn(currentDate);
        userMedicalDetail.setReferenceId(0L);
        userMedicalDetail.setReportId(0);
        userMedicalDetail.setCreatedBy(user.getUserId());
        userMedicalDetail.setCreatedOn(currentDate);
        userMedicalDetailRepository.save(userMedicalDetail);

        return "signup completed";
    }
}