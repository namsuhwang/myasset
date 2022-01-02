package com.idlelife.myasset.service;

import com.idlelife.myasset.models.dto.AssetDto;
import com.idlelife.myasset.models.dto.AssetSearch;
import com.idlelife.myasset.models.dto.TotalAssetSummaryDto;
import com.idlelife.myasset.models.dto.form.AssetForm;
import com.idlelife.myasset.models.entity.AssetEntity;
import com.idlelife.myasset.repository.AssetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyassetService {
    @Autowired
    AssetMapper assetMapper;

    public String getAssetTest(){
        return (String) assetMapper.selectAssetTest();
    }

    public AssetDto getAssetDto(Long assetId){
        return assetMapper.selectAssetDto(assetId);
    }

    public AssetEntity getAsset(Long assetId){
        return assetMapper.selectAsset(assetId);
    }

    public AssetDto regAsset(AssetForm form){
        AssetEntity assetEntity = getAssetEntityFromForm(form);

        int cnt = assetMapper.insertAsset(assetEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetMapper.selectAssetDto(assetEntity.getAssetId());
    }

    public AssetDto modAsset(AssetForm form){
        AssetEntity assetEntity = getAssetEntityFromForm(form);

        int cnt = assetMapper.updateAsset(assetEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetMapper.selectAssetDto(assetEntity.getAssetId());
    }
    public AssetDto delAsset(Long assetId){
        int cnt = assetMapper.deleteAsset(assetId);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetMapper.selectAssetDto(assetId);
    }

    public List<AssetDto> getAssetDtoList(AssetSearch dom){
        List<AssetDto> list = assetMapper.selectAssetDtoList(dom);
        return list;
    }

    private AssetEntity getAssetEntityFromForm(AssetForm form){
        AssetEntity assetEntity = new AssetEntity();
        if(form.getAssetId() == null || form.getAssetId() <= 0){
            long assetId = assetMapper.createAssetId();
            assetEntity.setAssetId(assetId);
        }else{
            assetEntity.setAssetId(form.getAssetId());
        }
        assetEntity.setMemberId(form.getMemberId());
        assetEntity.setAssetType(form.getAssetType());
        assetEntity.setAssetName(form.getAssetName());
        assetEntity.setEvalAmt(form.getEvalAmt());
        assetEntity.setDeleteYn("N");
        return assetEntity;
    }

    // 종합현황 요약
    public TotalAssetSummaryDto getTotalAssetSummary(Long memberId){
        TotalAssetSummaryDto totalAssetSummary = assetMapper.selectTotalAssetSummary(memberId);
        return totalAssetSummary;
    }
}
