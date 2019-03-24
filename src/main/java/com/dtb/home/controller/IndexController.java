package com.dtb.home.controller;

import com.dtb.entity.Carousel;
import com.dtb.entity.QuestionsAssociation;
import com.dtb.entity.User;
import com.dtb.home.service.CarouselService;
import com.dtb.home.service.QAService;
import com.dtb.home.service.UserService;
import com.dtb.utils.VerifyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @Author：lmx
 * @Description：
 * @Date：Created on 14:54 2019/2/24.
 * @ModifyBy：
 */
@Controller("indexController")
@RequestMapping("home/index")
public class IndexController {

    @Autowired
    private QAService qaService;

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private UserService userService;

    /**
     * @auther lmx
     * @date 2019/3/11 0:35
     * @descript 渲染主页视图
     * @param model
     * @return java.lang.String
     */
    @RequestMapping("index")
    public String index(Model model){
        List<Carousel> resultList = carouselService.findCarouselList();
        model.addAttribute("carouselList",resultList);
        return "home/index";
    }

    /**
     * @auther: lmx
     * @descript: 渲染注册页视图
     * @date: 2019/2/28 17:46
     * @param: []
     * @return: java.lang.String
     */
    @RequestMapping("register")
    public String register(){
        return "home/register";
    }

    /**
     * @auther lmx
     * @date 2019/3/10 0:54
     * @descript 渲染登录页视图渲染
     * @param pagePath 登录后跳转的页面
     * @param model 视图
     * @return java.lang.String
     */
    @RequestMapping("login")
    public String login(@RequestParam(value = "pagePath",required = false,defaultValue = "/home/index/index") String pagePath,
                        Model model){
        model.addAttribute("pagePath",pagePath);
        return "home/login";
    }

    /**
     * @auther: lmx
     * @descript: 返回二维码图片，并且在session存入当前验证码字符串
     * @date: 2019/2/28 17:45
     * @param: [response, request]
     * @return: void
     */
    @GetMapping("/getVerifyCode")
    public void getVerifyCode(HttpServletResponse response, HttpServletRequest request) throws Exception{
        HttpSession session=request.getSession();
        //利用图片工具生成图片
        //第一个参数是生成的验证码，第二个参数是生成的图片
        Object[] objs = VerifyUtil.createImage();
        //将验证码存入Session
        session.setAttribute("verifyCode",objs[0]);

        //将图片输出给浏览器
        BufferedImage image = (BufferedImage) objs[1];
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "png", os);
    }

    /**
     * @auther: lmx
     * @date: 2019/3/3 1:14
     * @descript: 关于我们页面渲染
     * @param:
     * @return: java.lang.String
     */
    @RequestMapping("about")
    public String about(){
        return "home/about";
    }

    /**
     * @auther: lmx
     * @date: 2019/3/3 11:24
     * @descript: 有问有答页面渲染
     * @param:
     * @return: java.lang.String
     */
    @RequestMapping("question")
    public String question(){
        return "home/question";
    }

    /**
     * @auther: lmx
     * @date: 2019/3/9 1:28
     * @descript: 问题详情和用户解答列表页面
     * @param:
     * @return: java.lang.String
     */
    @RequestMapping("answer/{questionId}")
    public String answer(@PathVariable("questionId") Integer questionId,Model model){
        QuestionsAssociation questionAndAnswers = qaService.findAnswerList(questionId);
        model.addAttribute("questionInfo",questionAndAnswers);
        return  "home/answer";
    }

    /**
     * @auther lmx
     * @date 2019/3/14 23:43
     * @descript 提问页面
     * @param invitaId 受邀请答题人id
     * @return java.lang.String
     */
    @RequestMapping("ask")
    public String ask(@RequestParam(value = "invitaId",required = false,defaultValue = "")Integer invitaId,Model model){
        model.addAttribute("invitaId",invitaId);
        List<User> userList = userService.findUserList();
        model.addAttribute("userList",userList);
        return "home/ask";
    }

    /**
     * @auther lmx
     * @date 2019/3/16 17:52
     * @descript 用户信息查看
     * @param userId 用户id
     * @param model
     * @return java.lang.String
     */
    @RequestMapping("userinfo/{userId}")
    public String userInfo(@PathVariable("userId")Integer userId,Model model){
        Map<String,Object> userInfoMap = userService.findUserInfoById(userId);
        model.addAttribute("userInfo",userInfoMap);
        return "home/userinfo";
    }

    /**
     * @auther lmx
     * @date 2019/3/16 22:47
     * @descript 教师/学生用户列表
     * @param userType 用户类型，1学生，2教师
     * @param model 视图模型
     * @return java.lang.String
     */
    @RequestMapping("user/list/{userType}")
    public String userList(@PathVariable("userType")Byte userType,Model model){
        model.addAttribute("userType",userType);
        return "home/user-list";
    }

    /**
     * 积分兑换兑换列表
     *
     * @return java.lang.String
     * @author lmx
     * @date 2019/3/23 18:35
     */
    @RequestMapping("gift/list")
    public String list() {
        return "home/gift-list";
    }

}
