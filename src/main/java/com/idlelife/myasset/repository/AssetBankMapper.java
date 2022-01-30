package com.idlelife.myasset.repository;

import com.idlelife.myasset.models.bank.dto.AssetBankDto;
import com.idlelife.myasset.models.stock.StockSearch;
import com.idlelife.myasset.models.bank.entity.AssetBankEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AssetBankMapper {
 

    int insertAssetBank(AssetBankEntity dom);

    int updateAssetBank(AssetBankEntity dom);

    int deleteAssetBank(long assetId);

    List<AssetBankDto> selectAssetBankDtoList(StockSearch dom);

    AssetBankEntity selectAssetBank(long assetId);

    AssetBankDto selectAssetBankDto(long assetId);
}
