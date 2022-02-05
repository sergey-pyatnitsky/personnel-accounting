package com.dao.position;

import com.core.domain.Position;

import java.util.List;

public interface PositionDAO {
    Position find(Long id);
    List<Position> findAll();
    Position findByName(String name);

    Position save(Position position);
    Position update(Position position);
    boolean removeById(Long id);
    boolean remove(Position position);
}
