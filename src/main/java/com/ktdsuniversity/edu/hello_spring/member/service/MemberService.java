package com.ktdsuniversity.edu.hello_spring.member.service;

import com.ktdsuniversity.edu.hello_spring.member.vo.LoginMemberVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberRegistVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

public interface MemberService {
	
	public boolean insertNewMember(MemberRegistVO memberRegistVO);
	
	public boolean checkAvailableEmail(String email);
	
	public MemberVO readMember(LoginMemberVO loginMemberVO);

}
