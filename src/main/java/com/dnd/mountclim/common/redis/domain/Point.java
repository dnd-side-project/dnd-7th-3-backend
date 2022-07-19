package com.dnd.mountclim.common.redis.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Getter
@RedisHash("point")
public class Point implements Serializable {
	
	@Id
	private String id;	// redis hash의 키 값은 redishash의 값 + ":" + id 가 된다. 예시) point:haeyonghahn
	private Long amount;
	private LocalDateTime refreshTime;
	
	@Builder
    public Point(String id, Long amount, LocalDateTime refreshTime) {
        this.id = id;
        this.amount = amount;
        this.refreshTime = refreshTime;
    }

    public void refresh(long amount, LocalDateTime refreshTime) {
        if(refreshTime.isAfter(this.refreshTime)){ // 저장된 데이터보다 최신 데이터일 경우
            this.amount = amount;
            this.refreshTime = refreshTime;
        }
    }
}
