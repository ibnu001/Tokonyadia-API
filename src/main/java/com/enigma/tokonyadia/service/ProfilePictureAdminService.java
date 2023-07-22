package com.enigma.tokonyadia.service;

import org.springframework.web.multipart.MultipartFile;

public interface ProfilePictureAdminService {

    void upload(MultipartFile multipartFile, String adminId);

}
