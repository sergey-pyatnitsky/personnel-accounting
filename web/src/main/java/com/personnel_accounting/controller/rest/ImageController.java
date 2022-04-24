package com.personnel_accounting.controller.rest;

import com.personnel_accounting.domain.Image;
import com.personnel_accounting.employee.EmployeeService;
import com.personnel_accounting.image.ImageService;
import com.personnel_accounting.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/api/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam(name = "file", required = false) MultipartFile file,
                                        Authentication authentication) {
        Image image = employeeService.editProfileImage(imageService.storeImage(file),
                userService.findByUsername(authentication.getName()));

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/downloadFile/" + image.getId())
                .toUriString();
        return new ResponseEntity(fileDownloadUri, HttpStatus.OK);
    }

    @GetMapping("/api/downloadFile/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        Image image = imageService.getImage(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                .body(new ByteArrayResource(image.getData()));
    }
}
