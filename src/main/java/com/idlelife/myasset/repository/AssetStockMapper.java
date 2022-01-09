package com.idlelife.myasset.repository;

import com.idlelife.myasset.models.dto.*;
import com.idlelife.myasset.models.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AssetStockMapper {

    int insertAssetStock(AssetStockEntity dom);

    int updateAssetStock(AssetStockEntity dom);

    int deleteAssetStock(long assetId);

    List<AssetStockDto> selectAssetStockDtoList(AssetSearch dom);

    AssetStockEntity selectAssetStock(long assetId);

    AssetStockDto selectAssetStockDto(long assetId);


    long createStockKindId();

    int insertStockKind(StockKindEntity dom);

    int updateStockKind(StockKindEntity dom);

    int deleteStockKind(long stockKindId);

    List<StockKindDto> selectStockKindDtoList(AssetSearch dom);

    StockKindEntity selectStockKind(long stockKindId);

    StockKindDto selectStockKindDto(long stockKindId);

    int updateStockKindCurrentStatus(StockKindEntity dom);



    long createStockTradeId();

    int insertStockTrade(StockTradeEntity dom);

    int updateStockTrade(StockTradeEntity dom);

    int deleteStockTrade(long stockTradeId);

    List<StockTradeDto> selectStockTradeDtoList(AssetSearch dom);

    StockTradeEntity selectStockTrade(long stockTradeId);

    StockTradeEntity selectLastStockTrade(long stockKindId);

    StockTradeDto selectStockTradeDto(long stockTradeId);
}
