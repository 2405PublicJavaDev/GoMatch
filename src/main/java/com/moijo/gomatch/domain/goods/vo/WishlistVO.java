package com.moijo.gomatch.domain.goods.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class WishlistVO {
    private String memberId;    // MEMBER_ID: 회원 ID
    private Long goodsNo;       // GOODS_NO: 상품 번호
    private Timestamp regDate;  // REG_DATE: 찜한 날짜

    // 추가적으로 필요할 수 있는 필드들
    private String goodsName;   // 상품명 (필요한 경우)
    private String goodsImageUrl; // 상품 이미지 URL (필요한 경우)
    private int goodsPrice;     // 상품 가격 (필요한 경우)
}
