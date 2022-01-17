package com.idlelife.myasset.repository;

import com.idlelife.myasset.models.dto.AssetDto;
import com.idlelife.myasset.models.dto.AssetSearch;
import com.idlelife.myasset.models.dto.TotalAssetSummaryDto;
import com.idlelife.myasset.models.entity.AssetEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AssetMapper {

    String selectAssetTest() ;

    long createAssetId();

    int insertAsset(AssetEntity dom);

    int updateAsset(AssetEntity dom);

    int deleteAsset(long assetId);

    List<AssetDto> selectAssetDtoList(AssetSearch dom);

    AssetEntity selectAsset(long assetId);

    AssetDto selectAssetDto(long assetId);

    // 종합현황 요약
    //TotalAssetSummaryDto selectTotalAssetSummary(Long memberId);
}
