package com.personnel_accounting.image;

import com.personnel_accounting.domain.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image storeImage(MultipartFile image);
    Image getImage(Long id);
}
