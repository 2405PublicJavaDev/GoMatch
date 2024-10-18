package com.moijo.gomatch.app.admin;

import com.moijo.gomatch.domain.admin.service.AdminService1;
import com.moijo.gomatch.domain.admin.vo.AdminVO1;
import com.moijo.gomatch.domain.admin.vo.GoodsImageVO;
import com.moijo.gomatch.domain.goods.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin1")
public class AdminController1 {

    private AdminService1 adminService1;

    @Autowired
    public AdminController1(AdminService1 adminService1) {
        this.adminService1 = adminService1;
    }

    // 굿즈 등록 폼을 보여주는 메소드
    @GetMapping("/insert")
    public String showRegisterForm() {
        return "admin1/insert"; // 등록 폼의 템플릿 경로
    }

    @PostMapping("/insert")
    public String registerGoods(@ModelAttribute AdminVO1 admin,
                                @RequestParam("goodsImage") MultipartFile goodsImage,
                                @RequestParam("detailImages") List<MultipartFile> detailImages,
                                @RequestParam("options") List<String> options,  // 옵션 추가
                                Model model) throws IOException {

        // 1. 상품 정보를 DB에 저장하고, 생성된 goodsNo를 가져옴
        Long generatedGoodsNo = adminService1.insertGoods(admin);
        admin.setGoodsNo(generatedGoodsNo);

        // 옵션 저장 (옵션이 null이 아니고 공백이 아닌 경우에만 저장)
        if (options != null && !options.isEmpty()) {
            for (String option : options) {
                if (option != null && !option.trim().isEmpty()) {
                    adminService1.insertGoodsOption(generatedGoodsNo, option);
                }
            }
        }

        // 3. 대표 이미지 저장
        if (!goodsImage.isEmpty()) {
            String repDir = "c:/gomatch/goods/rep/";
            String repFileName = goodsImage.getOriginalFilename();
            String realPath = repDir + repFileName;
            String webPath = "/goods/rep/" + repFileName;

            new File(repDir).mkdirs();
            goodsImage.transferTo(new File(realPath));

            GoodsImageVO goodsImageVO = new GoodsImageVO();
            goodsImageVO.setGoodsImageType("REPRESENTATIVE");
            goodsImageVO.setGoodsImageRepYn("Y");
            goodsImageVO.setGoodsImageRealPath(realPath);
            goodsImageVO.setGoodsImageWebPath(webPath);
            goodsImageVO.setGoodsImageOrder(1);
            goodsImageVO.setGoodsNo(generatedGoodsNo);

            adminService1.insertGoodsImage(goodsImageVO);
        }

        // 4. 상세 이미지 저장 (최대 5장)
        int order = 2;
        String detDir = "c:/gomatch/goods/det/";
        new File(detDir).mkdirs();

        for (MultipartFile detailImage : detailImages) {
            if (!detailImage.isEmpty() && order <= 6) {
                String detFileName = detailImage.getOriginalFilename();
                String realPath = detDir + detFileName;
                String webPath = "/goods/det/" + detFileName;

                detailImage.transferTo(new File(realPath));

                GoodsImageVO goodsImageVO = new GoodsImageVO();
                goodsImageVO.setGoodsImageType("DETAIL");
                goodsImageVO.setGoodsImageRepYn("N");
                goodsImageVO.setGoodsImageRealPath(realPath);
                goodsImageVO.setGoodsImageWebPath(webPath);
                goodsImageVO.setGoodsImageOrder(order++);
                goodsImageVO.setGoodsNo(generatedGoodsNo);

                adminService1.insertGoodsImage(goodsImageVO);
            }
        }

        model.addAttribute("message", "상품과 옵션이 성공적으로 등록되었습니다.");
        return "redirect:/admin1/insert";
    }


//    @PostMapping("/insert")
//    public String registerGoods(@ModelAttribute AdminVO1 admin,
//                                @RequestParam("goodsImage") MultipartFile goodsImage,
//                                @RequestParam("detailImages") List<MultipartFile> detailImages,
//                                Model model) throws IOException {
//
//        // 1. 상품 정보를 DB에 먼저 저장하고, 생성된 goodsNo를 가져옴
//        Long generatedGoodsNo = adminService1.insertGoods(admin);
//
//        System.out.println("Generated Goods No after insert: " + generatedGoodsNo);
//
//        admin.setGoodsNo(generatedGoodsNo);
//
//        // 생성된 goodsNo 출력
//        System.out.println("Generated Goods No: " + generatedGoodsNo);
//
//        // 2. 대표 이미지 저장
//        if (!goodsImage.isEmpty()) {
//            String repDir = "c:/gomatch/goods/rep/";
//            String repFileName = goodsImage.getOriginalFilename();
//            String realPath = repDir + repFileName;
//            String webPath = "/goods/rep/" + repFileName;
//
//            // 디렉토리가 없으면 생성
//            new File(repDir).mkdirs();
//
//            // 파일 저장
//            goodsImage.transferTo(new File(realPath));
//
//            // GOODS_IMAGE 테이블에 데이터 삽입
//            GoodsImageVO goodsImageVO = new GoodsImageVO();
//            goodsImageVO.setGoodsImageType("REPRESENTATIVE");
//            goodsImageVO.setGoodsImageRepYn("Y");
//            goodsImageVO.setGoodsImageRealPath(realPath);
//            goodsImageVO.setGoodsImageWebPath(webPath);
//            goodsImageVO.setGoodsImageOrder(1); // 대표 이미지의 경우 순서 1
//            goodsImageVO.setGoodsNo(generatedGoodsNo);
//
//            // 이미지 정보 저장 (DB에 저장하는 메서드 호출)
//            adminService1.insertGoodsImage(goodsImageVO);
//        }
//
//        // 3. 상세 이미지 저장 (최대 5장)
//        int order = 2;
//        String detDir = "c:/gomatch/goods/det/";
//        new File(detDir).mkdirs();
//
//        for (MultipartFile detailImage : detailImages) {
//            if (!detailImage.isEmpty() && order <= 6) { // 최대 5장만 저장
//                String detFileName = detailImage.getOriginalFilename();
//                String realPath = detDir + detFileName;
//                String webPath = "/goods/det/" + detFileName;
//
//                // 파일 저장
//                detailImage.transferTo(new File(realPath));
//
//                // GOODS_IMAGE 테이블에 데이터 삽입
//                GoodsImageVO goodsImageVO = new GoodsImageVO();
//                goodsImageVO.setGoodsImageType("DETAIL");
//                goodsImageVO.setGoodsImageRepYn("N");
//                goodsImageVO.setGoodsImageRealPath(realPath);
//                goodsImageVO.setGoodsImageWebPath(webPath);
//                goodsImageVO.setGoodsImageOrder(order++);
//                goodsImageVO.setGoodsNo(generatedGoodsNo);
//
//                // 이미지 정보 저장 (DB에 저장하는 메서드 호출)
//                adminService1.insertGoodsImage(goodsImageVO);
//            }
//        }
//
//        model.addAttribute("message", "상품과 이미지가 성공적으로 등록되었습니다.");
//        return "redirect:/admin1/insert"; // 노가다를 위해 list에서 insert로 변경 해놓음
//    }

    @GetMapping("/list")
    public String getGoodsList(Model model) {
        List<GoodsVO> goodsList = adminService1.getAllGoods(); // 서비스 메소드를 호출하여 상품 목록을 가져옴
        model.addAttribute("goodsList", goodsList); // 모델에 추가
        return "admin1/list"; // Thymeleaf 템플릿 경로 반환
    }

    @GetMapping("/detail/{goodsNo}")
    public String getGoodsDetail(@PathVariable Long goodsNo, Model model) {
        AdminVO1 goods = adminService1.getGoodsById(goodsNo); // 상품 ID로 상품 조회
        model.addAttribute("goods", goods); // 모델에 추가
        return "admin1/detail"; // 상세 조회 템플릿 경로 반환
    }

    // 수정 폼을 보여주는 메소드 (상세 페이지에서 수정)
    @GetMapping("/edit/{goodsNo}")
    public String showEditForm(@PathVariable Long goodsNo, Model model) {
        AdminVO1 admin = adminService1.getGoodsById(goodsNo); // 상품 ID로 상품 조회
        List<GoodsImageVO> goodsImages = adminService1.getGoodsImagesByGoodsNo(goodsNo); // 이미지 조회

        model.addAttribute("goods", admin); // 상품 정보 모델에 추가
        model.addAttribute("goodsImages", goodsImages); // 이미지 정보 모델에 추가

        return "admin1/edit"; // 수정 폼 템플릿 경로 반환
    }

    @PostMapping("/edit")
    public String updateGoods(
            @ModelAttribute AdminVO1 admin,
            @RequestParam("goodsImage") MultipartFile goodsImage,
            @RequestParam("detailImages") List<MultipartFile> detailImages,
            @RequestParam(value = "removeImages", required = false) List<Long> removeImages, // 필수 아님
            Model model) throws IOException {

        // 1. 삭제할 이미지 처리 (removeImages가 null일 수 있으므로 확인)
        if (removeImages != null && !removeImages.isEmpty()) {
            for (Long imageId : removeImages) {
                adminService1.deleteGoodsImage(imageId); // 이미지 삭제
            }
        }

        // 2. AdminVO1을 GoodsVO로 변환하여 기존 상품 수정
        GoodsVO goods = new GoodsVO();
        goods.setGoodsNo(admin.getGoodsNo());
        goods.setGoodsTeam(admin.getGoodsTeam());
        goods.setGoodsProductName(admin.getGoodsProductName());
        goods.setGoodsPrice(admin.getGoodsPrice());
        goods.setGoodsProductCode(admin.getGoodsProductCode());
        goods.setGoodsFrom(admin.getGoodsFrom());
        goods.setGoodsMakeDate(admin.getGoodsMakeDate());
        goods.setGoodsOutDate(admin.getGoodsOutDate());
        goods.setGoodsCategory(admin.getGoodsCategory());

        adminService1.updateGoods(goods); // 기존 상품 정보 수정

        // 3. 대표 이미지 수정 (기존 이미지 삭제 후 새로 저장)
        if (!goodsImage.isEmpty()) {
            adminService1.deleteRepresentativeImage(admin.getGoodsNo()); // 기존 이미지 삭제

            String repDir = "c:/gomatch/goods/rep/";
            String repFileName = goodsImage.getOriginalFilename();
            String realPath = repDir + repFileName;
            String webPath = "/goods/rep/" + repFileName;

            new File(repDir).mkdirs(); // 디렉터리 생성
            goodsImage.transferTo(new File(realPath)); // 파일 저장

            GoodsImageVO goodsImageVO = new GoodsImageVO();
            goodsImageVO.setGoodsImageType("REPRESENTATIVE");
            goodsImageVO.setGoodsImageRepYn("Y");
            goodsImageVO.setGoodsImageRealPath(realPath);
            goodsImageVO.setGoodsImageWebPath(webPath);
            goodsImageVO.setGoodsImageOrder(1);
            goodsImageVO.setGoodsNo(admin.getGoodsNo());

            adminService1.insertGoodsImage(goodsImageVO); // 새 이미지 저장
        }

        // 4. 상세 이미지 저장 (최대 5장)
        int order = 2;
        String detDir = "c:/gomatch/goods/det/";
        new File(detDir).mkdirs(); // 디렉터리 생성

        for (MultipartFile detailImage : detailImages) {
            if (!detailImage.isEmpty() && order <= 6) {
                String detFileName = detailImage.getOriginalFilename();
                String realPath = detDir + detFileName;
                String webPath = "/goods/det/" + detFileName;

                detailImage.transferTo(new File(realPath)); // 파일 저장

                GoodsImageVO goodsImageVO = new GoodsImageVO();
                goodsImageVO.setGoodsImageType("DETAIL");
                goodsImageVO.setGoodsImageRepYn("N");
                goodsImageVO.setGoodsImageRealPath(realPath);
                goodsImageVO.setGoodsImageWebPath(webPath);
                goodsImageVO.setGoodsImageOrder(order++);
                goodsImageVO.setGoodsNo(admin.getGoodsNo());

                adminService1.insertGoodsImage(goodsImageVO); // 새 이미지 저장
            }
        }

        model.addAttribute("message", "상품이 성공적으로 수정되었습니다.");
        return "redirect:/admin1/list";
    }

    @PostMapping("/delete") // 삭제 요청을 처리하는 메소드
    public String deleteGoods(@RequestParam("goodsNo") Long goodsNo, Model model) {
        adminService1.deleteGoods(goodsNo); // 서비스 메소드를 호출하여 상품 삭제
        model.addAttribute("message", "상품이 성공적으로 삭제되었습니다.");
        return "redirect:/admin1/list"; // 삭제 후 상품 목록으로 리다이렉트
    }
}
