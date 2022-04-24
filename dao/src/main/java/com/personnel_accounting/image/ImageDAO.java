package com.personnel_accounting.image;

import com.personnel_accounting.domain.Image;

public interface ImageDAO {
    Image save(Image image);
    Image findById(Long id);
    boolean removeById(Long id);
}
