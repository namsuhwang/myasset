package com.idlelife.myasset.service;

import com.idlelife.myasset.repository.MyassetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyassetService {
    @Autowired
    MyassetMapper myassetMapper;

    public String getAssetTest(){
        return (String)myassetMapper.selectAssetTest();
    }
}
