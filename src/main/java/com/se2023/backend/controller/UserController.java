package com.se2023.backend.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.se2023.backend.entity.Email;
import com.se2023.backend.entity.User;
import com.se2023.backend.mapper.EmailMapper;
import com.se2023.backend.mapper.UserMapper;
import com.se2023.backend.utils.MailService;
import com.se2023.backend.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserMapper userMapper;
    private final EmailMapper emailMapper;
    private final MailService mailService;

    @Autowired
    public UserController(UserMapper userMapper, EmailMapper emailMapper, MailService mailService) {
        this.userMapper = userMapper;
        this.emailMapper = emailMapper;
        this.mailService = mailService;
    }



    @GetMapping("/login")
    //加载login页面,并且自动加入一个staff账号和一个manager账号
    public JsonResult login(){
        if(userMapper.queryUserByUsername("Manager1")==null){
            User manager = new User();
            manager.setUsername("Manager1");
            manager.setEmail("350012471@qq.com");//所有staff注册通过此邮箱验证
            manager.setPassword("123456");
            manager.setRole("Manager");
            userMapper.addUser(manager);
        }
        if(userMapper.queryUserByUsername("Staff1")==null){
            User staff = new User();
            staff.setUsername("Staff1");
            staff.setPassword("123456");
            staff.setRole("Staff");
            userMapper.addUser(staff);
        }
        return new JsonResult(500,"Login in page","Login in page","success");
    }


    @PostMapping("/login")
    //login页面提交后检验是否登陆成功
    public JsonResult Login(@RequestBody User userSubmit){
        //确认username和password均输入
        if(userSubmit.getPassword()==null||userSubmit.getUsername()==null){
            return new JsonResult(400,null,"Username or password missing","failed");
        }
        //查询username是否存在
        String username_submit = userSubmit.getUsername();
        User user=userMapper.queryUserByUsername(username_submit);
        if (user==null){
            return new JsonResult(400,null,"Invalid username","failed");
        }
        //检查密码是否正确
        String code_password = SecureUtil.md5(userSubmit.getPassword());
        if(user.getPassword().equals(code_password)){
            if(user.getRole().equals("Consumer")){
                return new JsonResult(500,user.getUsername(),"Consumer login","success");
            }else if(user.getRole().equals("Staff")){
                return new JsonResult(500,user.getUsername(),"Staff login","success");
            }else{
                return new JsonResult(500,user.getUsername(),"Manager login","success");
            }
        }else{
            return new JsonResult(400,null,"Invalid password","failed");
        }
    }





    //对用户输入的邮箱发送验证码，并存储邮箱、验证码到email_confirm表中
    @PostMapping("/Consumer/{email}")
    public JsonResult consumer_email(@PathVariable("email") String email){
        try{
            //存储email, confirm code信息
            String confirmCode= RandomUtil.randomString(6);
            Email existEmail=emailMapper.queryEmailByname(email);
            //检查是否需要更新验证码
            if (existEmail==null){
                Email target=new Email(email,confirmCode);
                emailMapper.addConfirm(target);
            }else{
                emailMapper.updateConfirm(confirmCode,email);
            }
            //发送邮件
            String subject="Sports center registry confirm code ~";
            String text="This is your registry confirm code: "+confirmCode+". Please keep it to yourself ~";
            mailService.sendEmail(email,subject,text);
            return new JsonResult(400,email,"send email success","success");
        }catch(Exception e){
            System.out.println(e);
            return new JsonResult(500,null,"Something wrong","failed");
        }
    }
    //register页面提交后检验是否注册成功
    @PostMapping("/Consumer")
    public JsonResult c_register(@RequestBody User user){
        //确保所有信息输入完整
        if(user.getUsername()==null||user.getPassword()==null||user.getEmail()==null||user.getConfirmCode()==null){
            return new JsonResult(500,null,"Something missing!","failed");
        }
        //控制username不能重复
        String username_submit=user.getUsername();
        if(userMapper.queryUserByUsername(username_submit)!=null){
            return new JsonResult(500,null,"This username is already used.","failed");
        }
//        //检查重复的密码是否一致
//        if(!user.getPassword().equals()){
//            return new JsonResult(500,null,"Invalid confirm password","fail");
//        }
        //检查验证码是否一致
        //获取输入的email的正确验证码
        String email=user.getEmail();
        if(emailMapper.queryConfirmCodeByEmail(email)==null){
            return new JsonResult(500,null,"Invalid email","failed");
        }else{
            String right_confirm=emailMapper.queryConfirmCodeByEmail(email);
            if(!user.getConfirmCode().equals(right_confirm)){
                return new JsonResult(500,null,"Invalid confirm code","failed");
            }
        }
        try{
            //加密密码
            String code_password = SecureUtil.md5(user.getPassword());
            user.setPassword(code_password);
            user.setRole("Consumer");
            userMapper.addUser(user);
            return new JsonResult(0,user,"Registry Success","success");
        }catch(Exception e){
            System.out.println(e);
            return new JsonResult(500,null,"Registry Failed!","failed");
        }
    }




    @PostMapping("/Staff_email")
    public JsonResult staff_email(){
        try{
            //存储email, 更新/添加confirm code信息
            String confirmCode= RandomUtil.randomString(6);//生成随机验证码
            //获取manager邮箱，设置为接受验证码的邮箱
            User manager=userMapper.queryUserByUsername("Manager1");
            String email=manager.getEmail();
            Email existEmail=emailMapper.queryEmailByname(email);
            //检查是否需要更新验证码
            if(existEmail==null){
                Email target=new Email(email,confirmCode);
                emailMapper.addConfirm(target);//不需要更新，直接添加
            }else{
                emailMapper.updateConfirm(confirmCode,email);
            }
            //发送邮件
            String subject="Sports center registry confirm code ~";
            String text="This is your registry confirm code: "+confirmCode+". Please keep it to yourself ~";
            mailService.sendEmail(email,subject,text);
            return new JsonResult(400,confirmCode,"send email success","success");
        }catch(Exception e){
            System.out.println(e);
            return new JsonResult(500,null,"Something wrong","failed");
        }
    }

    @PostMapping("/Staff")
    public JsonResult s_register(@RequestBody User user){
        //确保所有信息输入完整
        if(user.getUsername()==null||user.getPassword()==null||user.getConfirmCode()==null){
            return new JsonResult(500,null,"Something missing!","failed");
        }
        //控制username不能重复
        String username_submit=user.getUsername();
        if(userMapper.queryUserByUsername(username_submit)!=null){
            return new JsonResult(500,null,"This username is already used.","failed");
        }
//        //检查重复的密码是否一致
//        if(!user.getPassword().equals()){
//            return new JsonResult(500,null,"Invalid confirm password","fail");
//        }
        //检查验证码是否一致
        //获取输入的email的正确验证码
        User manager=userMapper.queryUserByUsername("Manager1");
        String email=manager.getEmail();
        String right_confirm=emailMapper.queryConfirmCodeByEmail(email);
        if(!user.getConfirmCode().equals(right_confirm)){
            return new JsonResult(500,null,"Invalid confirm code","failed");
        }
        try{
            //加密密码
            String code_password = SecureUtil.md5(user.getPassword());
            user.setPassword(code_password);
            user.setRole("Staff");
            userMapper.addUser(user);
            return new JsonResult(0,user,"Registry Success","success");
        }catch(Exception e){
            System.out.println(e);
            return new JsonResult(500,null,"Registry Failed!","failed");
        }
    }


    @GetMapping("/user")
    public JsonResult queryUser() {
        return new JsonResult(0, userMapper.queryAllUser(), "查询成功", "success");
    }

}
