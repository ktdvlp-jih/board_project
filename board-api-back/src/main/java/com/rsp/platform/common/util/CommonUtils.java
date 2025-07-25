package com.rsp.platform.common.util;

import java.time.LocalDateTime;

public class CommonUtils {
    /**
     * LocalDateTime 날짜 파라미터 유효성 검증
     * LocalDateTime은 이미 유효한 날짜이므로 null 체크와 범위 체크만 수행
     */
    public static void validateDateParameters(LocalDateTime fromDate, LocalDateTime toDate) {
        // 필수값 체크
        if (fromDate == null) {
            throw new IllegalArgumentException("시작일자(fromDate)는 필수값입니다.");
        }
        if (toDate == null) {
            throw new IllegalArgumentException("종료일자(toDate)는 필수값입니다.");
        }
        
        // 날짜 범위 체크 (시작일이 종료일보다 늦으면 안됨)
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("시작일자가 종료일자보다 늦을 수 없습니다.");
        }
    }
}
