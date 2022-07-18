package com.dnd.mountclim.domain.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dnd.mountclim.domain.mapper.UserMapper;
import com.dnd.mountclim.domain.vo.UserVo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;

    public ResponseEntity<?> getUser(){

    	List<UserVo> userVo = userMapper.getUser();

        return ResponseEntity.ok(userVo);
    }
}
