package com.ktdsuniversity.edu.hello_spring.bbs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ktdsuniversity.edu.hello_spring.bbs.service.BoardService;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardListVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.BoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.ModifyBoardVO;
import com.ktdsuniversity.edu.hello_spring.bbs.vo.WriteBoardVO;
import com.ktdsuniversity.edu.hello_spring.common.beans.FileHandler;

import jakarta.validation.Valid;

@Controller
public class BoardController {
	
	@Autowired
	private FileHandler fileHandler;

	@Autowired
	private BoardService boardService;
	
	@GetMapping("/board/list") // http://localhost:8080/board/list
	public String viewBoardList(Model model) {
		
		BoardListVO boardListVO = this.boardService.getAllBoard();
		
		model.addAttribute("boardListVO", boardListVO);
		
		return "board/boardlist";
	}
	
	@GetMapping("/board/write")
	public String viewBoardWritePage() {
		return "board/boardwrite";
	}
	
	@PostMapping("/board/write")
	public String doSomething(@Valid WriteBoardVO writeBoardVO // @Valid WriteBoardVO의 Validation Check 수행
								,BindingResult bindingResult // @Valid의 실패 결과만 할당받는다
								, Model model
								) { 
		if(bindingResult.hasErrors()) {
			model.addAttribute("writeBoardVO", writeBoardVO);
			return "board/boardwrite";
		}
		
		boolean isCreate = this.boardService.createNewBoard(writeBoardVO);
		System.out.println("게시글 등록 결과: " + isCreate);
		return "redirect:/board/list";
	}
	
	@GetMapping("/board/view")
	public String viewBoard(Model model, @RequestParam int id) {
		
		BoardVO boardVO = this.boardService.selectOneBoard(id, true);
		
		model.addAttribute("boardVO", boardVO);
		
		return "board/boardview";
	}
	
	@GetMapping("/board/modify/{id}")
	public String viewBoardModifyPage(Model model, @PathVariable int id) {
		
		BoardVO boardVO = this.boardService.selectOneBoard(id, false);
		
		model.addAttribute("boardVO", boardVO);
		
		return "board/boardmodify";
	}
	
	@PostMapping("/board/modify/{id}")
	public String doModifyOneBoard(@Valid ModifyBoardVO modifyBoardVO, BindingResult bindingResult, @PathVariable int id, Model model ) { // ModifyBoardVO에는 id가 없어서 따로 전달
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("modifyBoardVO", modifyBoardVO);
			return "board/boardmodify";
		}
		
		// set ID
		modifyBoardVO.setId(id);
		boolean isUpdated = this.boardService.updateOneBoard(modifyBoardVO);
		
		// post update process
		if(isUpdated) {
			// 성공적으로 수정했다면, 수정한 게시글의 상세조회 페이지로 이동시킨다.
			
			return "redirect:/board/view?id="+id;
		}else {
			// 사용자가 작성했던 내용을 JSP에 그대로 보내준다.
			model.addAttribute("boardVO", modifyBoardVO);
			return "board/boardmodify";
		}
	}

	
	@GetMapping("/board/delete/{id}") //PostMapping form을 이용해서 전송할 때만 쓴다. GetMapping url이 바뀔 때 사용
	public String doDeleteOneBoard(@PathVariable int id) {
		
		boolean isDeleted = this.boardService.deleteOneBoard(id);
		
		if(isDeleted) {
			return "redirect:/board/list";
		}else {
			return "redirect:/board/view?id=" + id;
		}

	}
	
	@GetMapping("/board/file/download/{id}")
	public ResponseEntity<Resource> doDownloadFile(@PathVariable int id) {
		
		// 1. 다운로드 할 파일의 이름을 알기 위해 게시글을 조회한다.
		BoardVO boardVO = this.boardService.selectOneBoard(id, false);
		
		return this.fileHandler.downloadFile(boardVO.getFileName(), boardVO.getOriginFileName());
	}
}
