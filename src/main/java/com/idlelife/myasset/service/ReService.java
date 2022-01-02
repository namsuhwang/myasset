package com.idlelife.myasset.service;

import com.idlelife.myasset.models.dto.AssetReDto;
import com.idlelife.myasset.models.dto.AssetReRentDto;
import com.idlelife.myasset.models.dto.AssetSearch;
import com.idlelife.myasset.models.dto.form.AssetReForm;
import com.idlelife.myasset.models.dto.form.AssetReRentForm;
import com.idlelife.myasset.models.entity.AssetReEntity;
import com.idlelife.myasset.models.entity.AssetReRentEntity;
import com.idlelife.myasset.repository.AssetReMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReService {
    @Autowired
    AssetReMapper assetReMapper;

 
    public AssetReDto getAssetReDto(Long assetId){
        return assetReMapper.selectAssetReDto(assetId);
    }

    public AssetReEntity getAssetRe(Long assetId){
        return assetReMapper.selectAssetRe(assetId);
    }

    public AssetReDto regAssetRe(AssetReForm form){
        AssetReEntity assetReEntity = getAssetReEntityFromForm(form);

        int cnt = assetReMapper.insertAssetRe(assetReEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetReMapper.selectAssetReDto(assetReEntity.getAssetId());
    }

    public AssetReDto modAssetRe(AssetReForm form){
        AssetReEntity assetReEntity = getAssetReEntityFromForm(form);

        int cnt = assetReMapper.updateAssetRe(assetReEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetReMapper.selectAssetReDto(assetReEntity.getAssetId());
    }
    public AssetReDto delAssetRe(Long assetId){
        int cnt = assetReMapper.deleteAssetRe(assetId);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetReMapper.selectAssetReDto(assetId);
    }

    public List<AssetReDto> getAssetReDtoList(AssetSearch dom){
        List<AssetReDto> list = assetReMapper.selectAssetReDtoList(dom);
        return list;
    }

    private AssetReEntity getAssetReEntityFromForm(AssetReForm form){
        AssetReEntity assetReEntity = new AssetReEntity();
        assetReEntity.setAssetId(form.getAssetId());
        assetReEntity.setReType(form.getReType());
        assetReEntity.setReName(form.getReName());
        assetReEntity.setBuyDate(form.getBuyDate());
        assetReEntity.setBuyAmt(form.getBuyAmt());
        assetReEntity.setBuyExtraAmt(form.getBuyExtraAmt());
        assetReEntity.setLoanAmt(form.getLoanAmt());
        assetReEntity.setLoanBalAmt(form.getLoanBalAmt());
        assetReEntity.setLoanRate(form.getLoanRate());
        assetReEntity.setSaleDate(form.getSaleDate());
        assetReEntity.setSaleAmt(form.getSaleAmt());
        assetReEntity.setSaleIncomeTaxAmt(form.getSaleIncomeTaxAmt());
        assetReEntity.setSaleExtraAmt(form.getSaleExtraAmt());
        assetReEntity.setPalAmt(form.getPalAmt());
        assetReEntity.setPalPercentage(form.getPalPercentage());
        assetReEntity.setReRentId(form.getReRentId());
        assetReEntity.setDeleteYn("N");
        return assetReEntity;
    }


    public AssetReRentDto getAssetReRentDto(Long reRentId){
        return assetReMapper.selectAssetReRentDto(reRentId);
    }

    public AssetReRentEntity getAssetReRent(Long assetId){
        return assetReMapper.selectAssetReRent(assetId);
    }

    public AssetReRentDto regAssetReRent(AssetReRentForm form){
        AssetReRentEntity assetReRentEntity = getAssetReRentEntityFromForm(form);

        int cnt = assetReMapper.insertAssetReRent(assetReRentEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetReMapper.selectAssetReRentDto(assetReRentEntity.getReRentId());
    }

    public AssetReRentDto modAssetReRent(AssetReRentForm form){
        AssetReRentEntity assetReRentEntity = getAssetReRentEntityFromForm(form);

        int cnt = assetReMapper.updateAssetReRent(assetReRentEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetReMapper.selectAssetReRentDto(assetReRentEntity.getReRentId());
    }
    public AssetReRentDto delAssetReRent(Long reRentId){
        int cnt = assetReMapper.deleteAssetReRent(reRentId);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetReMapper.selectAssetReRentDto(reRentId);
    }

    public List<AssetReRentDto> getAssetReRentDtoList(AssetSearch dom){
        List<AssetReRentDto> list = assetReMapper.selectAssetReRentDtoList(dom);
        return list;
    }

    private AssetReRentEntity getAssetReRentEntityFromForm(AssetReRentForm form){
        AssetReRentEntity assetReRentEntity = new AssetReRentEntity();
        assetReRentEntity.setReRentId(form.getReRentId());
        assetReRentEntity.setAssetId(form.getAssetId());
        assetReRentEntity.setRentStartDate(form.getRentStartDate());
        assetReRentEntity.setRentEndDate(form.getRentEndDate());
        assetReRentEntity.setRenterName(form.getRenterName());
        assetReRentEntity.setRenterContact(form.getRenterContact());
        assetReRentEntity.setDeposit(form.getDeposit());
        assetReRentEntity.setMonthFee(form.getMonthFee());
        assetReRentEntity.setDeleteYn("N");
        return assetReRentEntity;
    }

}
