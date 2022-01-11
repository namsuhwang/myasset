package com.idlelife.myasset.service;

import com.idlelife.myasset.common.exception.MyassetException;
import com.idlelife.myasset.models.dto.AssetBankDto;
import com.idlelife.myasset.models.dto.AssetDto;
import com.idlelife.myasset.models.dto.AssetSearch;
import com.idlelife.myasset.models.dto.form.AssetBankForm;
import com.idlelife.myasset.models.dto.form.AssetForm;
import com.idlelife.myasset.models.entity.AssetBankEntity;
import com.idlelife.myasset.models.entity.AssetEntity;
import com.idlelife.myasset.repository.AssetBankMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.idlelife.myasset.models.common.ErrorCode.MYASSET_ERROR_1000;

@Log4j2
@Service
@Transactional
public class BankService {
    @Autowired
    AssetBankMapper assetBankMapper;

    @Autowired
    AssetService assetService;
 
    public AssetBankDto getAssetBankDto(Long assetId){
        return assetBankMapper.selectAssetBankDto(assetId);
    }

    public AssetBankEntity getAssetBank(Long assetId){
        return assetBankMapper.selectAssetBank(assetId);
    }

    public AssetBankDto regAssetBank(AssetBankForm form){
        log.info("Asset 등록");
        AssetForm assetForm = new AssetForm();
        assetForm.setAssetName(form.getAssetName());
        assetForm.setAssetType("BANK");
        assetForm.setMemberId(form.getMemberId());
        long evalAmt = 0;
        long loanBalAmt = 0;
        switch(form.getAcnoType()){
            case "IO":
            case "ACC": evalAmt = form.getAbleAmt(); loanBalAmt = 0; break;
            case "MINUS": evalAmt = form.getAbleAmt() - form.getLoanBalAmt(); loanBalAmt = form.getLoanBalAmt(); break;
            case "LOAN": evalAmt = 0; loanBalAmt = form.getLoanBalAmt(); break;
        }
        assetForm.setEvalAmt(evalAmt);
        AssetEntity assetEntity = assetService.getAssetEntityFromForm(assetForm);
        assetEntity.setAbleAmt(form.getAbleAmt());
        AssetDto assetDto = assetService.regAsset(assetEntity);
        if(assetDto == null){
            throw new MyassetException("DB 에러 : 자산 등록 실패", MYASSET_ERROR_1000);
        }

        log.info("AssetBank 등록");
        form.setAssetId(assetDto.getAssetId());
        AssetBankEntity assetBankEntity = getAssetBankEntityFromForm(form);
        int cnt = assetBankMapper.insertAssetBank(assetBankEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 은행 자산 등록 실패", MYASSET_ERROR_1000);
        }

        AssetBankDto assetBankDto = assetBankMapper.selectAssetBankDto(assetBankEntity.getAssetId());
        return assetBankDto;
    }

    public AssetBankDto modAssetBank(AssetBankForm form){
        log.info("Asset 수정");
        log.info("기존 Asset 조회");
        AssetEntity assetEntity = assetService.getAsset(form.getAssetId());
        assetEntity.setAssetName(form.getAssetName());
        long evalAmt = 0;
        long loanBalAmt = 0;
        switch(form.getAcnoType()){
            case "IO":
            case "ACC": evalAmt = form.getAbleAmt(); loanBalAmt = 0; break;
            case "MINUS": evalAmt = form.getAbleAmt() - form.getLoanBalAmt(); loanBalAmt = form.getLoanBalAmt(); break;
            case "LOAN": evalAmt = 0; loanBalAmt = form.getLoanBalAmt(); break;
        }
        assetEntity.setEvalAmt(evalAmt);
        AssetDto assetDto = assetService.modAsset(assetEntity);
        if(assetDto == null){
            throw new MyassetException("DB 에러 : 자산 수정 실패", MYASSET_ERROR_1000);
        }

        log.info("AssetBank 수정");
        AssetBankEntity assetBankEntity = getAssetBankEntityFromForm(form);
        int cnt = assetBankMapper.updateAssetBank(assetBankEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 은행 자산 수정 실패", MYASSET_ERROR_1000);
        }

        AssetBankDto assetBankDto = assetBankMapper.selectAssetBankDto(assetBankEntity.getAssetId());
        return assetBankDto;
    }

    public AssetBankDto delAssetBank(Long assetId){
        log.info("Asset 삭제");
        assetService.delAsset(assetId);

        log.info("AssetBank 삭제");
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
        assetBankEntity.setMemberId(form.getMemberId());
        assetBankEntity.setAcnoType(form.getAcnoType());
        assetBankEntity.setOrgCd(form.getOrgCd());
        assetBankEntity.setOrgName(form.getOrgName());
        assetBankEntity.setAcnoType(form.getAcnoType());
        assetBankEntity.setAcno(form.getAcno());
        assetBankEntity.setAbleAmt(form.getAbleAmt());
        assetBankEntity.setLoanBalAmt(form.getLoanBalAmt());
        assetBankEntity.setIntRate(form.getIntRate());
        assetBankEntity.setLoanRate(form.getLoanRate());
        assetBankEntity.setDeleteYn("N");
        return assetBankEntity;
    }

}
