package com.idlelife.myasset.repository;

import com.idlelife.myasset.models.re.dto.AssetReDto;
import com.idlelife.myasset.models.re.dto.AssetReRentDto;
import com.idlelife.myasset.models.stock.StockSearch;
import com.idlelife.myasset.models.re.entity.AssetReEntity;
import com.idlelife.myasset.models.re.entity.AssetReRentEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AssetReMapper {

    int insertAssetRe(AssetReEntity dom);

    int updateAssetRe(AssetReEntity dom);

    int deleteAssetRe(long assetId);

    List<AssetReDto> selectAssetReDtoList(StockSearch dom);

    AssetReEntity selectAssetRe(long assetId);

    AssetReDto selectAssetReDto(long assetId);


    int insertAssetReRent(AssetReRentEntity dom);

    int updateAssetReRent(AssetReRentEntity dom);

    int deleteAssetReRent(long reRentId);

    List<AssetReRentDto> selectAssetReRentDtoList(StockSearch dom);

    AssetReRentEntity selectAssetReRent(long reRentId);

    AssetReRentDto selectAssetReRentDto(long reRentId);
}
