package com.moijo.gomatch.domain.admin.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
public class GoodsImageVO {
    private Long goodsImageNo;       // GOODS_IMAGE_NO
    private String goodsImageType;   // GOODS_IMAGE_TYPE
    private String goodsImageRepYn;  // GOODS_IMAGE_REP_YN
    private String goodsImageRealPath; // GOODS_IMAGE_REAL_PATH
    private String goodsImageWebPath;  // GOODS_IMAGE_WEB_PATH
    private int goodsImageOrder;      // GOODS_IMAGE_ORDER
    private Long goodsNo;              // GOODS_NO
    private Timestamp regDate;        // REG_DATE
    private Timestamp updateDate;     // UPDATE_DATE
}
