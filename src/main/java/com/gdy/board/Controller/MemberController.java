package com.gdy.board.Controller;

import com.gdy.board.DTO.MemberDTO;
import com.gdy.board.Service.MemberService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.Member;
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

    @GetMapping("/member/update")
    public String updateForm(HttpSession session, Model model){
        //내 정보는 session에 담아 놨음
        //session에 있는 이메일 값을 가져오고, 이메일로 나의 디비 전체 정보를 담고
        //update된 model을 전달

        String myEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember",memberDTO);
        return "update";
    }

    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO){
        memberService.update(memberDTO);
        //다른 메서드가 가지고 있는 주소를 요청
        return "redirect:/member/" + memberDTO.getId();
    }

    @GetMapping("/member/delete/{id}")
    public String update(@PathVariable Long id){
        memberService.deleteById(id);
        //return "list"; //우리는 dto를 list로 담아서 보냇기 때문에 list를 리턴하면 안됨
        return "redirect:/member";
    }

    @GetMapping("/member/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }
}
