package com.enigma.tokonyadia.service.impl;

import com.enigma.tokonyadia.entity.Admin;
import com.enigma.tokonyadia.entity.ProfilePicture;
import com.enigma.tokonyadia.model.response.FileResponse;
import com.enigma.tokonyadia.repository.ProfilePictureRepository;
import com.enigma.tokonyadia.service.AdminService;
import com.enigma.tokonyadia.service.ProfilePictureAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ProfilePictureAdminServiceImpl implements ProfilePictureAdminService {

    private final ProfilePictureRepository profilePictureRepository;
    private final AdminService adminService;
    @Value("${tokonyadia.path-url}")
    private String root;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public FileResponse upload(MultipartFile multipartFile, String adminId) {
        try {
            Admin admin = adminService.getById(adminId);

            Path path = Paths.get(root); // /Users/user/Downloads/images
            Path directories = Files.createDirectories(path); // /Users/user/Downloads/images
            String filename = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            Path saveFile = directories.resolve(filename); // /Users/user/Downloads/images/124213312_a.png
            Files.copy(multipartFile.getInputStream(), saveFile);

            ProfilePicture picture = ProfilePicture.builder()
                    .name(filename)
                    .contentType(multipartFile.getContentType())
                    .size(multipartFile.getSize())
                    .path(saveFile.toString())
                    .build();

            profilePictureRepository.saveAndFlush(picture);
            admin.setProfilePicture(picture);

            return FileResponse.builder()
                    .fileName(filename)
                    .url(String.format("/api/v1/admin/%s/image", picture.getId()))
                    .build();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "image not valid");
        }
    }

    @Override
    public Resource download(String imageId) {
        ProfilePicture picture = profilePictureRepository.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "profile picture not found"));

        Path path = Paths.get(picture.getPath());
        try {
            Resource resource = new UrlResource(path.toUri());
            return resource;
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "image not valid");
        }
    }
}
