package com.idlelife.myasset.repository;

import com.idlelife.myasset.models.stock.StockSearch;
import com.idlelife.myasset.models.stock.dto.*;
import com.idlelife.myasset.models.stock.entity.StockInterestEntity;
import com.idlelife.myasset.models.stock.form.StockKindForm;
import com.idlelife.myasset.models.stock.entity.AssetStockEntity;
import com.idlelife.myasset.models.stock.entity.StockKindEntity;
import com.idlelife.myasset.models.stock.entity.StockTradeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StockMapper {

    int insertAssetStock(AssetStockEntity dom);

    int updateAssetStock(AssetStockEntity dom);

    int deleteAssetStock(long assetId);

    List<AssetStockDto> selectAssetStockDtoList(StockSearch dom);

    AssetStockEntity selectAssetStock(StockSearch dom);

    AssetStockDto selectAssetStockDto(StockSearch dom);


    long createStockKindId();

    int insertStockKind(StockKindEntity dom);

    int updateStockKind(StockKindEntity dom);

    int deleteStockKind(long stockKindId);

    List<StockKindDto> selectStockKindDtoList(StockSearch dom);


    List<StockKindTotalDto> selectStockKindTotalList(StockSearch dom);

    List<StockKindCodeDto> selectStockKindCodeDtoList(StockSearch dom);

    StockKindEntity selectStockKind(StockSearch dom);

    StockKindDto selectStockKindDto(StockSearch dom);

    int updateStockKindCurrentStatus(StockKindEntity dom);



    long createStockTradeId();

    int insertStockTrade(StockTradeEntity dom);

    int updateStockTrade(StockTradeEntity dom);

    int deleteStockTrade(long stockTradeId);

    List<StockTradeDto> selectStockTradeDtoList(StockSearch dom);

    StockTradeEntity selectStockTrade(long stockTradeId);

    StockTradeEntity selectLastStockTrade(long stockKindId);

    StockTradeDto selectStockTradeDto(long stockTradeId);


    long createStockInterestId();

    long createStockInterestOrderNo(long memberId);

    List<StockInterestDto> selectStockInterestDtoList(long memberId);

    StockInterestDto selectStockInterestDto(StockSearch dom);

    StockInterestEntity selectStockInterest(StockSearch dom);

    int insertStockInterest(StockInterestEntity dom);

    int updateStockInterest(StockInterestEntity dom);

    int deleteStockInterest(long stockInterestId);

    int updateStockInterestOrder(StockSearch dom);

    int chgStockInterestOrderNoPlus(StockSearch dom);

    int chgStockInterestOrderNoMinus(StockSearch dom);

    int chgStockInterestOrderNoDel(StockSearch dom);

}
