package com.dnd.mountclim.common.redis.repository;

import org.springframework.data.repository.CrudRepository;

import com.dnd.mountclim.common.redis.domain.Point;

public interface PointRedisRepository extends CrudRepository<Point, String> {
}
