package com.ktdsuniversity.edu.hello_spring.member.dao;

import com.ktdsuniversity.edu.hello_spring.member.vo.LoginMemberVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberRegistVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

public interface MemberDao {

	public String NAMESPACE = "com.ktdsuniversity.edu.hello_spring.member.dao.MemberDao";
	
	public int selectEmailCount(String email);
	
	public int insertNewMember(MemberRegistVO memberRegistVO);
	
	public String selectSalt(String email);
	
	public MemberVO selectOneMember(LoginMemberVO loginMemberVO);
}
