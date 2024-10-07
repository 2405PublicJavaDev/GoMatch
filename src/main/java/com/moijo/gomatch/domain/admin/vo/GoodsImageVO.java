package com.moijo.gomatch.domain.admin.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GoodsImageVO {
    private Long goodsImageNo; // 이미지 번호 (Primary Key)
    private String goodsImageRepYn; // 대표 이미지 여부 ('Y' 또는 'N')
    private String goodsImageRealPath; // 실제 파일 경로
    private String goodsImageWebPath; // 웹에서 접근 가능한 경로
    private Integer goodsImageOrder; // 이미지 순서
    private Long goodsNo; // 참조하는 상품 번호 (Foreign Key)
    private LocalDateTime regDate; // 등록일
    private LocalDateTime updateDate; // 수정일
}
