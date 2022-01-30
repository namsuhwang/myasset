package com.idlelife.myasset.repository;

import com.idlelife.myasset.models.common.form.CommonCodeForm;
import com.idlelife.myasset.models.stock.dto.StockKindCodeDto;
import com.idlelife.myasset.models.common.dto.CommonCodeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommonMapper {

    List<CommonCodeDto> selectCommonCodeDtoList(CommonCodeForm dom);

    int insertStockKindCode(StockKindCodeDto dom);

    int updateStockKindCode(StockKindCodeDto dom);

    List<StockKindCodeDto> selectStockKindCodeList(StockKindCodeDto dom);

    StockKindCodeDto selectStockKindCode(StockKindCodeDto dom);

}
