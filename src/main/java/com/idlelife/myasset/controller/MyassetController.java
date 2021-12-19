package com.idlelife.myasset.controller;


import com.idlelife.myasset.service.MyassetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//@Api(tags = "[ 나의 자산 ]")
@Slf4j
@RestController
@EnableWebMvc
@RequestMapping(value = "/myasset", produces="application/json;charset=UTF-8")
public class MyassetController {
    @Autowired
    MyassetService myassetService;

    @GetMapping("/test/get")
    public ResponseEntity getAssetTest(){
        String result = myassetService.getAssetTest();

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/test/post")
    public ResponseEntity getAssetPostTest(){
        String result = myassetService.getAssetTest();

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
