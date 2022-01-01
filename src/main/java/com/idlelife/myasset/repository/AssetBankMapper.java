package com.idlelife.myasset.repository;

import com.idlelife.myasset.models.dto.AssetBankDto;
import com.idlelife.myasset.models.dto.AssetDto;
import com.idlelife.myasset.models.dto.AssetSearch;
import com.idlelife.myasset.models.entity.AssetBankEntity;
import com.idlelife.myasset.models.entity.AssetEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AssetBankMapper {
 

    int insertAssetBank(AssetBankEntity dom);

    int updateAssetBank(AssetBankEntity dom);

    int deleteAssetBank(long assetId);

    List<AssetBankDto> selectAssetBankDtoList(AssetSearch dom);

    AssetBankEntity selectAssetBank(long assetId);

    AssetBankDto selectAssetBankDto(long assetId);
}
