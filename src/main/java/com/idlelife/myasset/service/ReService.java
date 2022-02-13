package com.idlelife.myasset.service;

import com.idlelife.myasset.models.re.dto.AssetReDto;
import com.idlelife.myasset.models.re.dto.AssetReRentDto;
import com.idlelife.myasset.models.stock.StockSearch;
import com.idlelife.myasset.models.re.form.AssetReForm;
import com.idlelife.myasset.models.re.form.AssetReRentForm;
import com.idlelife.myasset.models.re.entity.AssetReEntity;
import com.idlelife.myasset.models.re.entity.AssetReRentEntity;
import com.idlelife.myasset.repository.ReMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReService {
    @Autowired
    ReMapper reMapper;

 
    public AssetReDto getAssetReDto(Long assetId){
        return reMapper.selectAssetReDto(assetId);
    }

    public AssetReEntity getAssetRe(Long assetId){
        return reMapper.selectAssetRe(assetId);
    }

    public AssetReDto regAssetRe(AssetReForm form){
        AssetReEntity assetReEntity = getAssetReEntityFromForm(form);

        int cnt = reMapper.insertAssetRe(assetReEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return reMapper.selectAssetReDto(assetReEntity.getAssetId());
    }

    public AssetReDto modAssetRe(AssetReForm form){
        AssetReEntity assetReEntity = getAssetReEntityFromForm(form);

        int cnt = reMapper.updateAssetRe(assetReEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return reMapper.selectAssetReDto(assetReEntity.getAssetId());
    }
    public AssetReDto delAssetRe(Long assetId){
        int cnt = reMapper.deleteAssetRe(assetId);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return reMapper.selectAssetReDto(assetId);
    }

    public List<AssetReDto> getAssetReDtoList(StockSearch dom){
        List<AssetReDto> list = reMapper.selectAssetReDtoList(dom);
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
        return reMapper.selectAssetReRentDto(reRentId);
    }

    public AssetReRentEntity getAssetReRent(Long assetId){
        return reMapper.selectAssetReRent(assetId);
    }

    public AssetReRentDto regAssetReRent(AssetReRentForm form){
        AssetReRentEntity assetReRentEntity = getAssetReRentEntityFromForm(form);

        int cnt = reMapper.insertAssetReRent(assetReRentEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return reMapper.selectAssetReRentDto(assetReRentEntity.getReRentId());
    }

    public AssetReRentDto modAssetReRent(AssetReRentForm form){
        AssetReRentEntity assetReRentEntity = getAssetReRentEntityFromForm(form);

        int cnt = reMapper.updateAssetReRent(assetReRentEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return reMapper.selectAssetReRentDto(assetReRentEntity.getReRentId());
    }
    public AssetReRentDto delAssetReRent(Long reRentId){
        int cnt = reMapper.deleteAssetReRent(reRentId);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return reMapper.selectAssetReRentDto(reRentId);
    }

    public List<AssetReRentDto> getAssetReRentDtoList(StockSearch dom){
        List<AssetReRentDto> list = reMapper.selectAssetReRentDtoList(dom);
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
