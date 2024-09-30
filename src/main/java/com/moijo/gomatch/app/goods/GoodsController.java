package com.moijo.gomatch.app.goods;

import com.moijo.gomatch.domain.goods.service.GoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GoodsController {

    @GetMapping("/insert")
        public String insertPage() {
        return "insert1";
    }


}
