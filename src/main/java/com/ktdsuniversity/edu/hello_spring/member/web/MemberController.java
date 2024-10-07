package com.ktdsuniversity.edu.hello_spring.member.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ktdsuniversity.edu.hello_spring.member.service.MemberService;
import com.ktdsuniversity.edu.hello_spring.member.vo.LoginMemberVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberRegistVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService memberService;

	@GetMapping("/member/regist")
	public String viewRegistMemberPage() {
		return "member/memberregist";
	}
	
	@PostMapping("/member/regist")
	public String doRegistMember(@Valid MemberRegistVO memberRegistVO, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("memberRegistVO", memberRegistVO);
			return "member/memberregist";
		}
		
		boolean isSuccess = memberService.insertNewMember(memberRegistVO);
		
		if(isSuccess) {
			return "redirect:/member/login";
		}
		
		model.addAttribute("memberVO", memberRegistVO);
		return "member/memberregist";
	}
	
	@ResponseBody
	@GetMapping("/member/regist/available")
	public Map<String, Object> doCheckAvailableEmail(@RequestParam String email) {
		
		boolean isAvailableEmail = this.memberService.checkAvailableEmail(email);
		
		Map<String, Object>response = new HashMap<String, Object>();
		response.put("email", email);
		response.put("available", isAvailableEmail);
		return response;
	}
	
	@GetMapping("/member/login")
	public String viewLoginPage() {
		return "member/memberlogin";
	}
	
	@PostMapping("/member/login")
	public String doLogin(@Valid LoginMemberVO loginMemberVO
						, BindingResult bindingResult
						, HttpSession session
						, Model model) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("loginMemberVO", loginMemberVO);
			return "member/memberlogin";
		}
		
		try {
			MemberVO memberVO = this.memberService.readMember(loginMemberVO);
			
			// 로그인 상태를 서버에 저장시킨다
			session.setAttribute("_LOGIN_USER", memberVO);
		}
		catch(IllegalArgumentException iae) {
			model.addAttribute("loginMemberVO", loginMemberVO);
			model.addAttribute("message", iae.getMessage());
			return "member/memberlogin";
		}
		
		return "redirect:/board/list";
	}
}
