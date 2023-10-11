package com.smhrd.player.converter;

import java.io.IOException;

public interface PlayerImageConverter<F, S> {
	//<F, S>
	//이미지 파일을(F) byte 문자열 형태(S)로 변환할 때
	//현재 인터페이스를 상속받아 정해진 틀대로 변환
	
	//타입(S): byte 문자열 형태
	//매개변수(F): 실제 파일
	
	//IOException: 입출력과 관련된 예외처리
	public S converte(F f) throws IOException;
	
}
