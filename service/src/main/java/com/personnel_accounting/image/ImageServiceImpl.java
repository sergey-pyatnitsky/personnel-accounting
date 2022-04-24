package com.personnel_accounting.image;

import com.personnel_accounting.domain.Image;
import com.personnel_accounting.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService{

    @Autowired
    private ImageDAO imageDAO;

    @Autowired
    private MessageSource messageSource;

    @Override
    public Image storeImage(MultipartFile image) {
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());

        try {
            if(fileName.contains(".."))
                throw new FileStorageException(
                        messageSource.getMessage("image.error.path", null, null) + fileName);
            if(!image.getContentType().contains("image"))
                throw new FileStorageException(
                        messageSource.getMessage("image.error.type", null, null));
            Image dbFile = new Image(fileName, image.getContentType(), image.getBytes());
            return imageDAO.save(dbFile);
        } catch (IOException ex) {
            throw new FileStorageException(
                    messageSource.getMessage("image.error", null, null));
        }
    }

    @Override
    public Image getImage(Long id) {
        Image image = imageDAO.findById(id);
        return image;
    }
}
