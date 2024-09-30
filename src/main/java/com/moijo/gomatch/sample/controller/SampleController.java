package com.moijo.gomatch.sample.controller;

import com.moijo.gomatch.common.FileUtil;
import com.moijo.gomatch.common.UploadCategory;
import com.moijo.gomatch.common.exception.EmptyResponse;
import com.moijo.gomatch.sample.dto.SampleSearchCondition;
import com.moijo.gomatch.sample.dto.SampleWriteDTO;
import com.moijo.gomatch.sample.service.SampleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SampleController {
    private final FileUtil fileUtil;
    private final SampleService sampleService;
    @GetMapping("/")
    public String sampleAll(Model model) {
        model.addAttribute("samples", sampleService.selectAll());
//        throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
         return "sample/home";
    }
    @GetMapping("/page/{currentPage}")
    public String samplePage(@PathVariable int currentPage, Model model) {
        model.addAttribute("samples", sampleService.selectPage(currentPage));
        return "sample/page";
    }

    @GetMapping("/pagesearch/{currentPage}")
    public String samplePageSearch(@PathVariable int currentPage, @ModelAttribute SampleSearchCondition searchCondition, Model model) {
        model.addAttribute("samples", sampleService.selectPageSearch(currentPage, searchCondition));
        return "sample/page";
    }

    @PostMapping("/write")
    @ResponseBody
    public ResponseEntity<String> write(@RequestBody @Valid SampleWriteDTO dto) {
        if("error".equals(dto.getContent())){
            throw new RuntimeException();
        }
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/writePage")
    public String writePage() {
        return "sample/write";
    }

    @PostMapping("/writePage")
    @ResponseBody
    public EmptyResponse writePage(@ModelAttribute @Valid SampleWriteDTO dto, @RequestParam("uploadFile") List<MultipartFile> uploadFiles) throws IOException {
        log.info("size: {}", uploadFiles.size());
        for(MultipartFile file : uploadFiles){
            log.info("file: {}", file.getOriginalFilename());
        }
        sampleService.insertSample(dto, uploadFiles);
        return new EmptyResponse();
    }

    @GetMapping("/deleteSampleFile")
    public String deleteSampleFile() throws IOException {
        fileUtil.deleteFiles(UploadCategory.SAMPLE, 1L);
        return "sample/home";
    }
}
