package com.enigma.tokonyadia.controller;

import com.enigma.tokonyadia.service.ProfilePictureAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProfilePictureAdminService profilePictureAdminService;

    @PostMapping(path = "/upload/{adminId}")
    public ResponseEntity<?> uploadProfilePicture(@RequestPart(name = "image") MultipartFile multipartFile,
                                                  @PathVariable String adminId) {
        profilePictureAdminService.upload(multipartFile, adminId);
        return ResponseEntity.ok().build();
    }

}
