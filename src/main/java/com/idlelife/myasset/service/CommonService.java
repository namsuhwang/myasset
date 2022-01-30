package com.idlelife.myasset.service;

import com.idlelife.myasset.common.exception.MyassetException;
import com.idlelife.myasset.models.common.form.CommonCodeForm;
import com.idlelife.myasset.models.stock.dto.StockKindCodeDto;
import com.idlelife.myasset.models.common.dto.CommonCodeDto;
import com.idlelife.myasset.repository.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.idlelife.myasset.models.common.ErrorCode.MYASSET_ERROR_1000;

@Service
@Transactional
public class CommonService {
    @Autowired
    CommonMapper commonMapper;

    public List<CommonCodeDto> getCommonCodeDtoList(CommonCodeForm dom){
        List<CommonCodeDto> list = commonMapper.selectCommonCodeDtoList(dom);
        return list;
    }

    public List<StockKindCodeDto> getStockKindCodeList(StockKindCodeDto dom){
        List<StockKindCodeDto> list = commonMapper.selectStockKindCodeList(dom);
        return list;
    }

    public StockKindCodeDto getStockKindCode(String code){
        StockKindCodeDto param = new StockKindCodeDto();
        param.setCode(code);
        StockKindCodeDto dto = commonMapper.selectStockKindCode(param);
        return dto;
    }


    public void insertStockKindCode(StockKindCodeDto dom){
        int cnt = commonMapper.insertStockKindCode(dom);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 주식 종목 코드 등록 실패", MYASSET_ERROR_1000);
        }
        return;
    }


    public void updateStockKindCode(StockKindCodeDto dom){
        int cnt = commonMapper.updateStockKindCode(dom);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 주식 종목 코드 수정 실패", MYASSET_ERROR_1000);
        }
        return;
    }

}
