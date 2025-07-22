package com.rsp.platform.domain.file.repository;

import com.rsp.platform.domain.file.entity.LinkFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkFileRepository extends JpaRepository<LinkFileEntity, Long> {

    // refId(게시글PK 등) 기준으로 파일 연결 목록 조회
    List<LinkFileEntity> findByRefId(Long refId);

    // refId + attachFileId로 연결정보 단건 조회
    // Optional<LinkFileEntity> findByRefIdAndAttachFileId(Long refId, Long attachFileId);
}
