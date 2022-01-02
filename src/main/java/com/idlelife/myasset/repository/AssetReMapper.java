package com.idlelife.myasset.repository;

import com.idlelife.myasset.models.dto.AssetReDto;
import com.idlelife.myasset.models.dto.AssetReRentDto;
import com.idlelife.myasset.models.dto.AssetSearch;
import com.idlelife.myasset.models.entity.AssetReEntity;
import com.idlelife.myasset.models.entity.AssetReRentEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AssetReMapper {

    int insertAssetRe(AssetReEntity dom);

    int updateAssetRe(AssetReEntity dom);

    int deleteAssetRe(long assetId);

    List<AssetReDto> selectAssetReDtoList(AssetSearch dom);

    AssetReEntity selectAssetRe(long assetId);

    AssetReDto selectAssetReDto(long assetId);


    int insertAssetReRent(AssetReRentEntity dom);

    int updateAssetReRent(AssetReRentEntity dom);

    int deleteAssetReRent(long reRentId);

    List<AssetReRentDto> selectAssetReRentDtoList(AssetSearch dom);

    AssetReRentEntity selectAssetReRent(long reRentId);

    AssetReRentDto selectAssetReRentDto(long reRentId);
}
