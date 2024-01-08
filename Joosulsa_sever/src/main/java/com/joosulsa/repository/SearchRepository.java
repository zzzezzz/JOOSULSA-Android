package com.joosulsa.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.joosulsa.entity.Tb_Recycling;

@Repository
public interface SearchRepository extends JpaRepository<Tb_Recycling, String>{
	
	// 이름 기준으로 가져오겠다.
	public Tb_Recycling findByTrashName(String search);
	
}