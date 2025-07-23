package com.rsp.platform.domain.file.repository;

import com.rsp.platform.domain.file.entity.LinkFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkFileRepository extends JpaRepository<LinkFileEntity, Long> {

    long countByRefId(Long refId);  // ← 자동으로 count 쿼리 실행!

    // refId(게시글PK 등) 기준으로 파일 연결 목록 조회
    List<LinkFileEntity> findByRefId(Long refId);

    @Query("SELECT lf.refId, COUNT(lf) FROM LinkFileEntity lf WHERE lf.refId IN :boardIds GROUP BY lf.refId")
    List<Object[]> countFilesByBoardIds(@Param("boardIds") List<Long> boardIds);

}
