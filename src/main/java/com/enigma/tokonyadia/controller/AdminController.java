package com.enigma.tokonyadia.controller;

import com.enigma.tokonyadia.model.response.CommonResponse;
import com.enigma.tokonyadia.model.response.FileResponse;
import com.enigma.tokonyadia.service.ProfilePictureAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
        FileResponse response = profilePictureAdminService.upload(multipartFile, adminId);
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully upload")
                .data(response)
                .build();

        return ResponseEntity.ok()
                .body(commonResponse);
    }

    @GetMapping(path = "/{image-id}/image")
    public ResponseEntity<?> downloadProfilePicture(@PathVariable(name = "image-id") String id) {
        Resource resource = profilePictureAdminService.download(id);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
