package com.dnd.mountclim.common.redis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import com.dnd.mountclim.common.redis.domain.Point;
import com.dnd.mountclim.common.redis.repository.PointRedisRepository;

@SpringBootTest
public class RedisTest {

	@Autowired
    private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private PointRedisRepository pointRedisRepository;
	
	@Test
	public void 기본_등록_조회가능_string() {
		// given
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		String key = "haeyonghahn_string";
		
		// when 
		valueOperations.set(key, "hello");
		
		// then
        String value = valueOperations.get(key);
        System.out.println("=== String ===");
        System.out.println("key : " + "haeyonghahn_string, " + "value : " + value);
        assertEquals("hello", value);
	}
	
	@Test
    void 기본_등록_조회가능_set() {
        // given
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        String key = "haeyonghahn_set";

        // when
        setOperations.add(key, "h", "e", "l", "l", "o");

        // then
        Set<String> members = setOperations.members(key);
        Long size = setOperations.size(key);
        System.out.println("=== Set ===");
        System.out.println(members.toString());
        System.out.println(size);
        assertEquals(4, size);
    }

	@Test
	public void 기본_등록_조회가능_hash() {
		// given
		String id = "haeyonghahn_hash";
		LocalDateTime refreshTime = LocalDateTime.of(2018, 5, 26, 0, 0);
		Point point = Point.builder()
				.id(id)
				.amount(1000L)
				.refreshTime(refreshTime)
				.build();
		
		// when
		pointRedisRepository.save(point);
		
		// then
		Point savedPoint = pointRedisRepository.findById(id).get();
		System.out.println("=== Hash ===");
		System.out.println("savedPointEntity.getAmount() : " + savedPoint.getAmount());
		assertEquals(1000L, savedPoint.getAmount());
		System.out.println("savedPointEntity.getRefreshTime() : " + savedPoint.getRefreshTime());
		assertEquals(refreshTime, savedPoint.getRefreshTime());
	}
}
