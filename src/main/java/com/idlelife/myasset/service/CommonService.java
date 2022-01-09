package com.idlelife.myasset.service;

import com.idlelife.myasset.models.dto.*;
import com.idlelife.myasset.models.dto.form.AssetForm;
import com.idlelife.myasset.models.dto.form.CommonCodeForm;
import com.idlelife.myasset.models.entity.AssetEntity;
import com.idlelife.myasset.repository.AssetMapper;
import com.idlelife.myasset.repository.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonService {
    @Autowired
    CommonMapper commonMapper;


    public List<CommonCodeDto> getCommonCodeDtoList(CommonCodeForm dom){
        List<CommonCodeDto> list = commonMapper.selectCommonCodeDtoList(dom);
        return list;
    }

}
