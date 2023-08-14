/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pubilc.sw.monitoring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pubilc.sw.monitoring.dto.UserDTO;
import pubilc.sw.monitoring.service.UserService;

/**
 *
 * @author qntjd
 */
@Controller
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService; // UserService 클래스 사용을 위한 변수
    
    @GetMapping("/register")
    public String register(){
        return "register";
    }
    
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    
    /**
     * 
     * @param userDTO 로그인을 원하는 사용자의 입력
     * @param attrs
     * @return 로그인 성공 여부에 따른 URL 전송
     */
    @PostMapping("/signin")
    public String signIn(@ModelAttribute UserDTO userDTO, RedirectAttributes attrs){
        if(userService.signIn(userDTO)){
            attrs.addFlashAttribute("msg", "로그인에 성공하였습니다.");
        }else{
            attrs.addFlashAttribute("msg", "로그인에 실패하였습니다.");
        }
        return "redirect:/";
    }
    
    /**
     * 
     * @param userDTO 회원 가입을 원하는 사용자의 입력
     * @param attrs
     * @return 회원가입 성공 여부에 따른 URL 전송
     */
    @PostMapping("/signup")
    public String signUp(@ModelAttribute UserDTO userDTO, RedirectAttributes attrs){
        if(userService.signUp(userDTO)){
            attrs.addFlashAttribute("msg", "회원가입에 성공하였습니다.");
        }else{
            attrs.addFlashAttribute("msg", "회원가입에 실패하였습니다.");
        }
        return "redirect:/";
    }
    
    /**
     * 
     * @param id 아이디 중복 검사를 위해 사용자가 입력한 정보
     * @return 아이디의 존재 여부 아이디가 존재하면 true, 없으면 false
     */
    @PostMapping("/idcheck/{id}")
    public @ResponseBody Boolean idCheck(@PathVariable String id){
        return userService.idExists(id);
    }
}