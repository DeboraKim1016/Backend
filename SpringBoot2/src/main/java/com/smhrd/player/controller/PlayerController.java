package com.smhrd.player.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.player.service.PlayerService;

@RestController //리액트 서버로 데이터만 응답
@CrossOrigin("http://localhost:3000") //리액트 서버 허용
public class PlayerController {

	
	@Autowired
	private PlayerService playerService;
	
	//select * from player;
	
	//Spring->React로 JSON 파일 전송하기
	@RequestMapping("/PlayerList.do")
	public JSONArray PlayerList() {
		
		System.out.println("Spring: PlayerList.do ");
		
		//리액트 서버로 JSON 파일 응답
		return playerService.PlayerList();
	}
	
	//React->Spring
	@RequestMapping("/PlayerDetail.do")
	public JSONObject PlayerDetail(@RequestParam String name) {
		System.out.println("이름: "+name);
		
		//DB에서 클릭한 사람의 정보만 가져와서 리액트로 전송
		
		return playerService.PlayerDetail(name);
	}
}
