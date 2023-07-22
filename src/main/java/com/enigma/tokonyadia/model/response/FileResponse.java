package com.enigma.tokonyadia.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class FileResponse {
    private String fileName;
    private String url;
}
