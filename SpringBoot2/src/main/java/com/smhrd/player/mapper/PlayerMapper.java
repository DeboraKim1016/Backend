package com.smhrd.player.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.smhrd.player.entity.Player;

@Mapper
public interface PlayerMapper {

	public List<Player> PlayerList();

	//선택한 선수의 정보 조회하기
	public Player PlayerDetail(String name);

}
