package com.personnel_accounting.position;

import com.personnel_accounting.domain.Position;

import java.util.List;

public interface PositionDAO {
    Position find(Long id);
    List<Position> findAll();
    Position findByName(String name);

    Position save(Position position);
    Position merge(Position position);
    boolean removeById(Long id);
    boolean remove(Position position);
}
