package com.idlelife.myasset.service;

import com.idlelife.myasset.models.dto.AssetBankDto;
import com.idlelife.myasset.models.dto.AssetDto;
import com.idlelife.myasset.models.dto.AssetSearch;
import com.idlelife.myasset.models.dto.form.AssetBankForm;
import com.idlelife.myasset.models.dto.form.AssetForm;
import com.idlelife.myasset.models.entity.AssetBankEntity;
import com.idlelife.myasset.models.entity.AssetStockEntity;
import com.idlelife.myasset.repository.AssetBankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {
    @Autowired
    AssetBankMapper assetBankMapper;

    @Autowired
    MyassetService assetService;
 
    public AssetBankDto getAssetBankDto(Long assetId){
        return assetBankMapper.selectAssetBankDto(assetId);
    }

    public AssetBankEntity getAssetBank(Long assetId){
        return assetBankMapper.selectAssetBank(assetId);
    }

    public AssetBankDto regAssetBank(AssetBankForm form){
        AssetForm assetForm = new AssetForm();
        assetForm.setAssetName(form.getAssetName());
        assetForm.setAssetType("BANK");
        assetForm.setMemberId(form.getMemberId());
        assetForm.setEvalAmt(0L);  // 처음 자산 등록시에는 평가금액 0원. 추후 세부 자산 등록시 업데이트해야 함.
        AssetDto assetDto = assetService.regAsset(assetForm);
        if(assetDto == null){
            throw new RuntimeException();
        }

        form.setAssetId(assetDto.getAssetId());
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
//        assetBankEntity.setAssetId(form.getAssetId());
        assetBankEntity.setAcnoType(form.getAcnoType());
        assetBankEntity.setOrgCd(form.getOrgCd());
        assetBankEntity.setOrgName(form.getOrgName());
        assetBankEntity.setAcno(form.getAcno());
        assetBankEntity.setAbleAmt(form.getAbleAmt());
        assetBankEntity.setLoanBalAmt(form.getLoanBalAmt());
        assetBankEntity.setIntRate(form.getIntRate());
        assetBankEntity.setLoanRate(form.getLoanRate());
        assetBankEntity.setDeleteYn("N");
        return assetBankEntity;
    }

}
