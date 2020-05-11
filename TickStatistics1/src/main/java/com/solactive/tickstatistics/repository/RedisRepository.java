package com.solactive.tickstatistics.repository;

import java.util.Map;

public interface RedisRepository {

    void insert(String instrument, Long timestamp);

    void delete(String instrument);

    Long get(String instrument);

    Map<String, Object> getMap();
}
