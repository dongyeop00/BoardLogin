package com.gdy.board.Controller;

import com.gdy.board.DTO.MemberDTO;
import com.gdy.board.Service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/save")
    public String save(){
        return "save";
    }

    @PostMapping("/member/save")
    public String saveForm(@ModelAttribute MemberDTO memberDTO){
        System.out.println("memberDTO = " + memberDTO);
        memberService.save(memberDTO);
        return "index";
    }

    @GetMapping("/member/login")
    public String loginForm(){
        return "login";
    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session){
        System.out.println("memberDTO = " + memberDTO);
        MemberDTO loginResult = memberService.login(memberDTO);
        if(loginResult != null){ //login 성공
            session.setAttribute("loginEmail",loginResult.getMemberEmail());
            return "main";

        }
        else{ //login 실패
            return "login";
        }
    }

    @GetMapping("/member")
    public String findAll(Model model){
        //model : request 영역에 객체를 실어 나르는 역할
        //어떠헌 html로 가져갈 데이터가 있다면 model 사용
        List<MemberDTO> memberDTOList =memberService.findAll();
        model.addAttribute("memberList",memberDTOList);
        return "list";
    }

    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model){
        //{id} 값을 받아오는 법 : @PathVariable 애노테이션 사용하기
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member",memberDTO);
        return "detail"; //값들을 detail로 가져간다
    }
}
