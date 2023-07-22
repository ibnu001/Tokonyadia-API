package com.enigma.tokonyadia.service;

import com.enigma.tokonyadia.model.response.FileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ProfilePictureAdminService {

    FileResponse upload(MultipartFile multipartFile, String adminId);
    Resource download(String imageId);

}
