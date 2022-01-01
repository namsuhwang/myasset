package com.idlelife.myasset.service;

import com.idlelife.myasset.models.dto.AssetBankDto;
import com.idlelife.myasset.models.dto.AssetSearch;
import com.idlelife.myasset.models.dto.form.AssetBankForm;
import com.idlelife.myasset.models.entity.AssetBankEntity;
import com.idlelife.myasset.repository.AssetBankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {
    @Autowired
    AssetBankMapper assetBankMapper;
 
    public AssetBankDto getAssetBankDto(Long assetId){
        return assetBankMapper.selectAssetBankDto(assetId);
    }

    public AssetBankEntity getAssetBank(Long assetId){
        return assetBankMapper.selectAssetBank(assetId);
    }

    public AssetBankDto regAssetBank(AssetBankForm form){
        AssetBankEntity assetBankEntity = getAssetBankEntityFromForm(form);

        int cnt = assetBankMapper.insertAssetBank(assetBankEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetBankMapper.selectAssetBankDto(assetBankEntity.getAssetId());
    }

    public AssetBankDto modAssetBank(AssetBankForm form){
        AssetBankEntity assetBankEntity = getAssetBankEntityFromForm(form);

        int cnt = assetBankMapper.updateAssetBank(assetBankEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetBankMapper.selectAssetBankDto(assetBankEntity.getAssetId());
    }
    public AssetBankDto delAssetBank(Long assetId){
        int cnt = assetBankMapper.deleteAssetBank(assetId);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetBankMapper.selectAssetBankDto(assetId);
    }

    public List<AssetBankDto> getAssetBankDtoList(AssetSearch dom){
        List<AssetBankDto> list = assetBankMapper.selectAssetBankDtoList(dom);
        return list;
    }

    private AssetBankEntity getAssetBankEntityFromForm(AssetBankForm form){
        AssetBankEntity assetBankEntity = new AssetBankEntity();
        assetBankEntity.setAssetId(form.getAssetId());
        assetBankEntity.setAcnoType(form.getAcnoType());
        assetBankEntity.setBankCd(form.getBankCd());
        assetBankEntity.setBankName(form.getBankName());
        assetBankEntity.setAcno(form.getAcno());
        assetBankEntity.setAbleAmt(form.getAbleAmt());
        assetBankEntity.setLoanBalAmt(form.getLoanBalAmt());
        assetBankEntity.setIntRate(form.getIntRate());
        assetBankEntity.setLoanRate(form.getLoanRate());
        assetBankEntity.setDeleteYn("N");
        return assetBankEntity;
    }

}
