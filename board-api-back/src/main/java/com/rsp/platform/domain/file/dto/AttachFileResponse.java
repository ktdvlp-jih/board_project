package com.rsp.platform.domain.file.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachFileResponse {
    private Long attachId;
    private String originalFilename;
    private String saveFilename;
    private String filePath;
    private Long fileSize;
}