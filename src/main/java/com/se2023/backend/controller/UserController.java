package com.se2023.backend.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.se2023.backend.config.EncryptionWithKeyConfig;
import com.se2023.backend.entity.Email;
import com.se2023.backend.entity.User;
import com.se2023.backend.mapper.EmailMapper;
import com.se2023.backend.mapper.UserMapper;
import com.se2023.backend.utils.MailService;
import com.se2023.backend.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.coyote.http11.filters.IdentityOutputFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(value="User",tags = "用户管理")
@RestController
public class UserController {
    private final UserMapper userMapper;
    private final EmailMapper emailMapper;
    private final MailService mailService;

    private final int EXPIRE_DATE = 60 * 60 * 1000;

    @Autowired
    public UserController(UserMapper userMapper, EmailMapper emailMapper, MailService mailService) {
        this.userMapper = userMapper;
        this.emailMapper = emailMapper;
        this.mailService = mailService;
    }


    @ApiOperation("Login")
    @GetMapping("/login")
    //加载login页面,并且自动加入一个staff账号和一个manager账号
    public JsonResult login() {
        if (userMapper.queryUserByUsername("Manager1") == null) {
            User manager = new User();
            manager.setUsername("Manager1");
            manager.setEmail("350012471@qq.com");//所有staff注册通过此邮箱验证
            manager.setPassword("123456");
            manager.setRole("Manager");
            userMapper.addUser(manager);
        }
        if (userMapper.queryUserByUsername("Staff1") == null) {
            User staff = new User();
            staff.setUsername("Staff1");
            staff.setPassword("123456");
            staff.setRole("Staff");
            userMapper.addUser(staff);
        }
        return new JsonResult(500, "Login in page", "Login in page", "success");
    }


    @PostMapping("/login")
    //login页面提交后检验是否登陆成功
    public JsonResult Login(@RequestBody User userSubmit) {
        //确认username和password均输入
        if (userSubmit.getPassword() == null || userSubmit.getUsername() == null) {
            return new JsonResult(400, null, "Username or password missing", "failed");
        }
        //查询username是否存在
        String username_submit = userSubmit.getUsername();
        User user = userMapper.queryUserByUsername(username_submit);
        if (user == null) {
            return new JsonResult(400, null, "Invalid username", "failed");
        }
        // 检查密码是否正确
        String code_password = SecureUtil.md5(userSubmit.getPassword());
        if (user.getPassword().equals(code_password)) {
            String token;
            try {
                // 过期时间
                Date date = new Date(System.currentTimeMillis() + EXPIRE_DATE);
                // algorithm
                Algorithm algorithm = Algorithm.HMAC256(EncryptionWithKeyConfig.KEY);
                Map<String, Object> header = new HashMap<>();
                header.put("alg", "HS256");
                header.put("typ", "JWT");
                token = JWT.create()
                        .withHeader(header)
                        .withClaim("username", user.getUsername())
                        .withClaim("role", user.getRole())
                        .withExpiresAt(date)
                        .sign(algorithm);
            } catch (Exception e) {
                e.printStackTrace();
                return new JsonResult(400, null, "Token error", "failed");
            }
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            if (user.getRole().equals("Consumer")) {
                return new JsonResult(0, map, "Consumer login", "success");
            } else if (user.getRole().equals("Staff")) {
                return new JsonResult(0, map, "Staff login", "success");
            } else {
                return new JsonResult(0, map, "Manager login", "success");
            }
        } else {
            return new JsonResult(400, null, "Invalid password", "failed");
        }
    }


    //对用户输入的邮箱发送验证码，并存储邮箱、验证码到email_confirm表中
    @PostMapping("/Consumer/{email}")
    public JsonResult consumer_email(@PathVariable("email") String email) {
        try {
            //存储email, confirm code信息
            String confirmCode = RandomUtil.randomString(6);
            Email existEmail = emailMapper.queryEmailByName(email);
            //检查是否需要更新验证码
            if (existEmail == null) {
                Email target = new Email(email, confirmCode);
                emailMapper.addConfirm(target);
            } else {
                emailMapper.updateConfirm(confirmCode, email);
            }
            //发送邮件
            String subject = "Sports center registry confirm code ~";
            String text = "This is your registry confirm code: " + confirmCode + ". Please keep it to yourself ~";
            mailService.sendEmail(email, subject, text);
            return new JsonResult(0, email, "send email success", "success");
        } catch (Exception e) {
            System.out.println(e);
            return new JsonResult(500, null, "Something wrong", "failed");
        }
    }

    //register页面提交后检验是否注册成功
    @PostMapping("/Consumer")
    public JsonResult c_register(@RequestBody User user) {
        //确保所有信息输入完整
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null || user.getConfirmCode() == null) {
            return new JsonResult(500, null, "Something missing!", "failed");
        }
        //控制username不能重复
        String username_submit = user.getUsername();
        if (userMapper.queryUserByUsername(username_submit) != null) {
            return new JsonResult(500, null, "This username is already used.", "failed");
        }
//        //检查重复的密码是否一致
//        if(!user.getPassword().equals()){
//            return new JsonResult(500,null,"Invalid confirm password","fail");
//        }
        //检查验证码是否一致
        //获取输入的email的正确验证码
        String email = user.getEmail();
        if (emailMapper.queryConfirmCodeByEmail(email) == null) {
            return new JsonResult(500, null, "Invalid email", "failed");
        } else {
            String right_confirm = emailMapper.queryConfirmCodeByEmail(email);
            if (!user.getConfirmCode().equals(right_confirm)) {
                return new JsonResult(500, null, "Invalid confirm code", "failed");
            }
        }
        try {
            //加密密码
            String code_password = SecureUtil.md5(user.getPassword());
            user.setPassword(code_password);
            user.setRole("Consumer");
            userMapper.addUser(user);
            return new JsonResult(0, user, "Registry Success", "success");
        } catch (Exception e) {
            System.out.println(e);
            return new JsonResult(500, null, "Registry Failed!", "failed");
        }
    }

    @PostMapping("/Staff_email")
    public JsonResult staff_email() {
        try {
            //存储email, 更新/添加confirm code信息
            String confirmCode = RandomUtil.randomString(6);//生成随机验证码
            //获取manager邮箱，设置为接受验证码的邮箱
            User manager = userMapper.queryUserByUsername("Manager1");
            String email = manager.getEmail();
            Email existEmail = emailMapper.queryEmailByName(email);
            //检查是否需要更新验证码
            if (existEmail == null) {
                Email target = new Email(email, confirmCode);
                emailMapper.addConfirm(target);//不需要更新，直接添加
            } else {
                emailMapper.updateConfirm(confirmCode, email);
            }
            //发送邮件
            String subject = "Sports center registry confirm code ~";
            String text = "This is your registry confirm code: " + confirmCode + ". Please keep it to yourself ~";
            mailService.sendEmail(email, subject, text);
            return new JsonResult(0, confirmCode, "send email success", "success");
        } catch (Exception e) {
            System.out.println(e);
            return new JsonResult(500, null, "Something wrong", "failed");
        }
    }

    @PostMapping("/Staff")
    public JsonResult s_register(@RequestBody User user) {
        //确保所有信息输入完整
        if (user.getUsername() == null || user.getPassword() == null || user.getConfirmCode() == null) {
            return new JsonResult(500, null, "Something missing!", "failed");
        }
        //控制username不能重复
        String username_submit = user.getUsername();
        if (userMapper.queryUserByUsername(username_submit) != null) {
            return new JsonResult(500, null, "This username is already used.", "failed");
        }
//        //检查重复的密码是否一致
//        if(!user.getPassword().equals()){
//            return new JsonResult(500,null,"Invalid confirm password","fail");
//        }
        //检查验证码是否一致
        //获取输入的email的正确验证码
        User manager = userMapper.queryUserByUsername("Manager1");
        String email = manager.getEmail();
        String right_confirm = emailMapper.queryConfirmCodeByEmail(email);
        if (!user.getConfirmCode().equals(right_confirm)) {
            return new JsonResult(500, null, "Invalid confirm code", "failed");
        }
        try {
            //加密密码
            String code_password = SecureUtil.md5(user.getPassword());
            user.setPassword(code_password);
            user.setRole("Staff");
            userMapper.addUser(user);
            // 清空验证码
            emailMapper.updateConfirm(null, email);
            return new JsonResult(0, user, "Registry Success", "success");
        } catch (Exception e) {
            System.out.println(e);
            return new JsonResult(500, null, "Registry Failed!", "failed");
        }
    }


    @GetMapping("/users")
    public JsonResult queryUser() {
        return new JsonResult(0, userMapper.queryAllUser(), "Query all users successfully.", "success");
    }

    @GetMapping(value = "/user")
    public JsonResult getUserInfo(@RequestHeader("Authorization") String token) {
        Algorithm algorithm = Algorithm.HMAC256(EncryptionWithKeyConfig.KEY);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        String username = jwt.getClaim("username").asString();
        User user = userMapper.queryUserByUsername(username);
        if (user == null) {
            return new JsonResult(400, null, "The user does not exists.", "failed");
        }
        JSONObject res = new JSONObject();
        res.put("id", user.getId());
        res.put("username", user.getUsername());
        res.put("email", user.getEmail());
        JSONObject role = new JSONObject();
        role.put("roleName", user.getRole());
        role.put("value", user.getRole());
        ArrayList<JSONObject> roles = new ArrayList<>();
        roles.add(role);
        res.put("roles", roles);
        if (user.getRole().equals("Manager")) {
            res.put("homePath", "/facilities/overview");
        } else if (user.getRole().equals("Staff")) {
            res.put("homePath", "/facilities/overview");
        }
        return new JsonResult(0, res, "Successfully achieved the user's info.", "success");
    }

    @GetMapping(value = "/user/logout")
    public JsonResult logout(@RequestHeader("Authorization") String token) {
        if (token != null) {
            return new JsonResult(0, null, "Logout successfully.", "success");
        }
        return new JsonResult(400, null, "Logout failed", "failed");
    }

    @GetMapping(value = "/user/nonmembers")
    public JsonResult queryAllNonmembers() {
        return new JsonResult(0, userMapper.queryUserByRole("Nonmember"), "Successfully achieved the nonmembers' info.", "success");
    }

    @GetMapping(value = "/user/staffs")
    public JsonResult queryAllStaffs() {
        return new JsonResult(0, userMapper.queryUserByRole("Staff"), "Successfully achieved the staffs' info.", "success");
    }

    @GetMapping(value = "/user/members")
    public JsonResult queryAllMembers() {
        return new JsonResult(0, userMapper.queryUserByRole("Member"), "Successfully achieved the members' info.", "success");
    }




    @PostMapping(value="/user/setMembership")
    public JsonResult setMembership(@RequestBody User user){
        if(user.getId()!=null){
            //check 是否已经是membership
            Integer user_id=user.getId();
            if (userMapper.queryMembership(user_id)==null){
                //user表里set membership为1
                userMapper.setMembership(user.getId());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String create_time = sdf.format(new Date());
                Date ex=new Date();
                ex.setTime(ex.getTime()+365*24*60*60*1000L );//会员默认保质期一年
                String expire_time=sdf.format(ex);
                //membership表里add对象
                userMapper.addMemebrship(user_id,create_time,expire_time);
                return new JsonResult(0, null,"Successfully join in the membership!", "success");
            }
            else{
                System.out.println(userMapper.queryMembership(user_id));
                return new JsonResult(500, null,"You are already a membership", "fail");
            }
        }
        else{
            return  new JsonResult(500,null,"Something missing!","fail");
        }
    }

    @PostMapping(value="/user/removeMembership")
    public JsonResult removeMembership(@RequestBody User user){
        if(user.getId()!=null){
            Integer user_id=user.getId();
            if(userMapper.queryMembership(user_id)!=null){
                userMapper.removeMembership(user.getId());
                userMapper.deleteMembership(user.getId());
                return new JsonResult(0,null,"Successfully remove this membership!","success");
            }else{
                return new JsonResult(500,null,"Invalid membership!","fail");
            }
        }else{
            return new JsonResult(500,null,"Missing user id!","fail");
        }
    }

    @GetMapping(value="/user/membership")
    public JsonResult queryAllMembership(){
        return new JsonResult(0, userMapper.queryAllMembership(),"Successfully query all membership", "success");
    }
}
