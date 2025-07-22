package com.rsp.platform.domain.file.repository;

import com.rsp.platform.domain.file.entity.AttachFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachFileRepository extends JpaRepository<AttachFileEntity, Long> {
    // 필요하면 커스텀 쿼리 작성 가능
    // Optional<AttachFileEntity> findBySaveFilename(String saveFilename);
}
