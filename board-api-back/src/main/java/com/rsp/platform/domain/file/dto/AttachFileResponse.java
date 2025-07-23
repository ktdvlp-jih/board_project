package com.rsp.platform.domain.file.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachFileResponse {
    private Long attachFileId;
    private String fileName;
}