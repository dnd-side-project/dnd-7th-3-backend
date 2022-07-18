package com.dnd.mountclim.domain.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.dnd.mountclim.domain.vo.UserVo;

@Component
@Mapper
public interface UserMapper {
    List<UserVo> getUser();
}
