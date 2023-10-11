package com.smhrd.player.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.smhrd.player.converter.ImageToBase64;
import com.smhrd.player.converter.PlayerImageConverter;
import com.smhrd.player.entity.Player;
import com.smhrd.player.mapper.PlayerMapper;

@Service
public class PlayerService {

	@Autowired
	private PlayerMapper playerMapper;

	@Autowired
	private ResourceLoader resourceLoader; //특정경로에 있는 파일 읽는 객체
	
	public JSONArray PlayerList() {
		
		List<Player> p_list=playerMapper.PlayerList();
		
		//3. JSONArray 전역변수로 선언
		JSONArray jsonPlayerList=new JSONArray();
	
		//데이터 가공
		//DB에서 가지고 온 선수의 정보 중 imgSrc는 실제 경로, 실제 파일이 아님
		//리액트 서버에 이미지를 직접 전송해야 하기 때문에 이미지 데이터 가공
		//imgSrc 파일이름->실제 파일->이미지(byte 형태의 문자열) 전송
		
		//ImageToBase64 객체 호출
		
		PlayerImageConverter<File, String> converter=new ImageToBase64();
		
		//Mapper에서 가져온 선수정보 리스트 JSON으로 변환하기
		for(Player p: p_list) {
			//1. 파일이름->실제 파일->byte 문자열
			//1-1. 파일이름 불러오기(getImgSrc)
			//claspath: src/main/resources 경로 설정
			String filepath="static/player_img/"+p.getImgSrc();
		
			//1-2. 실제 파일로 불러오기
//			Resource resource= resourceLoader.getResource(filepath);
			
			//1-3. converter를 통해 byte 문자열로 변환
			String fileString=null;
			 File TempFile = null;
			
			try {
	            // 배포 시 파일로 불러오면 파일을 못찾기 때문에 inputStream으로 진행
	            InputStream inputStream = new ClassPathResource(filepath).getInputStream();
	            TempFile = File.createTempFile(p.getImgSrc(), "");
	             FileUtils.copyInputStreamToFile(inputStream, TempFile);
	             // 1-3. converter를 통해서 byte문자열로 변환
	             fileString = converter.converte(TempFile);
	         } catch (IOException e) {
				e.printStackTrace();
			}
			
			//System.out.println("fileString: "+ fileString);
			
			//2. imgSrc 필드 값 변경: 파일 이름->byte 문자열
			p.setImgSrc(fileString);
			
			//3. JSONArray에 byte문자열로 변환환 데이터 담기
			jsonPlayerList.add(p);
			
		}
		
		//JSON 객체 return
		return jsonPlayerList;
	}

	//선택한 선수의 정보 조회하기
	public JSONObject PlayerDetail(String name) {
		
		Player p=playerMapper.PlayerDetail(name);
		
		//3. JSONObject 전역변수로 선언
		JSONObject jsonPlayer=new JSONObject();
		
		//ImageToBase64 객체 호출
		PlayerImageConverter<File, String> converter=new ImageToBase64();
		
		//Mapper에서 가져온 선수정보 JSON으로 변환하기
		//1. 파일이름->실제 파일->byte 문자열
		//1-1. 파일이름 불러오기(getImgSrc)
		String filepath="classpath:/static/player_img/"+p.getImgSrc();
		
		//1-2. 실제 파일로 불러오기
		Resource resource= resourceLoader.getResource(filepath);
		
		//1-3. converter를 통해 byte 문자열로 변환
		String fileString=null;
		
		try {
		
			fileString=converter.converte(resource.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//2. imgSrc 필드 값 변경: 파일 이름->byte 문자열
		p.setImgSrc(fileString);
		
		//3. JSONObject에 byte문자열로 변환환 데이터 담기(key, value 형태)
		jsonPlayer.put("p",p);
		
		return jsonPlayer;
	}

}
