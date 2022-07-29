package com.dnd.mountclim.domain.service;

import java.util.List;
import com.dnd.mountclim.domain.dto.UserDto;
import com.dnd.mountclim.domain.entity.UserEntity;
import com.dnd.mountclim.domain.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dnd.mountclim.domain.mapper.UserMapper;
import com.dnd.mountclim.domain.vo.UserVo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public ResponseEntity<?> getUser(){

    	List<UserVo> userVo = userMapper.getUser();

        return ResponseEntity.ok(userVo);
    }

    public ResponseEntity<?> getUser2(){

        List<UserEntity> userEntity = userRepository.findAll();

        return ResponseEntity.ok(userEntity);
    }

    public ResponseEntity<?> save(UserDto userDto){

        userRepository.save(
                UserEntity.builder()
                        .userId(userDto.getUserId())
                        .userPw(userDto.getUserPw())
                        .build());

        return ResponseEntity.ok("save success");
    }
}
