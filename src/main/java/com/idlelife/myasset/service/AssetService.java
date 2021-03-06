package com.idlelife.myasset.service;

import com.idlelife.myasset.models.dto.*;
import com.idlelife.myasset.models.dto.form.AssetForm;
import com.idlelife.myasset.models.entity.AssetEntity;
import com.idlelife.myasset.repository.AssetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AssetService {
    @Autowired
    AssetMapper assetMapper;

    @Autowired
    BankService bankService;

    @Autowired
    StockService stockService;

    @Autowired
    ReService reService;

    public String getAssetTest(){
        return (String) assetMapper.selectAssetTest();
    }

    public AssetDto getAssetDto(Long assetId){
        return assetMapper.selectAssetDto(assetId);
    }

    public AssetEntity getAsset(Long assetId){
        return assetMapper.selectAsset(assetId);
    }

    public AssetDto regAsset(AssetEntity assetEntity){
        int cnt = assetMapper.insertAsset(assetEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetMapper.selectAssetDto(assetEntity.getAssetId());
    }

    public AssetDto modAsset(AssetEntity assetEntity){

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

    public AssetEntity getAssetEntityFromForm(AssetForm form){
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
        assetEntity.setAbleAmt(form.getAbleAmt());
        assetEntity.setEvalAmt(form.getEvalAmt());
        assetEntity.setDeleteYn("N");
        return assetEntity;
    }

    // ???????????? ??????
    public TotalAssetSummaryDto getTotalAssetSummary(Long memberId){
        TotalAssetSummaryDto totalAssetSummary = assetMapper.selectTotalAssetSummary(memberId);
        return totalAssetSummary;
    }

    // ?????? ?????? ?????? ??? ??????
    public TotalAssetDto getTotalAsset(long memberId){
        TotalAssetDto result = new TotalAssetDto();
        TotalAssetSummaryDto summary = getTotalAssetSummary(memberId);
        if(summary == null){
            return result;
        }
        result.setTotalNetAssetAmt(summary.getTotalNetAsset());
        result.setTotalLoanBalAmt(summary.getTotalLoanBalAmt());
        result.setTotalAssetAmt(summary.getTotalNetAsset() + summary.getTotalLoanBalAmt());
        AssetSearch assetSearch = new AssetSearch();
        assetSearch.setMemberId(memberId);
        assetSearch.setDeleteYn("N");

        long totalAssetAmt = 0;
        long totalNetAssetAmt = 0;
        long totalLoanBalAmt = 0;

        // ?????? ??????, ?????? ???????????? ??????
        long bankAssetAmt = 0;
        List<AssetBankDto> bankList = bankService.getAssetBankDtoList(assetSearch);
        if(bankList != null && bankList.size() > 0) {
            for (AssetBankDto bank : bankList) {
                if (bank.getAcnoType().equals("IO") || bank.getAcnoType().equals("ACC")) {
                    bankAssetAmt = bankAssetAmt + bank.getAssetId();
                } else if (bank.getAcnoType().equals("MINUS")) {
                    bankAssetAmt = bankAssetAmt + (bank.getAbleAmt() - bank.getLoanBalAmt());
                } else {
                    bankAssetAmt = bankAssetAmt - bank.getLoanBalAmt();
                }
                totalLoanBalAmt = totalLoanBalAmt + bank.getLoanBalAmt();
            }
        }

        // ?????? ?????????
        long stockKindAssetAmt = 0;
        List<StockKindDto> stockKindList = stockService.getStockKindDtoList(assetSearch);
        if(stockKindList != null && stockKindList.size() > 0) {
            for (StockKindDto stockKind : stockKindList) {
                stockKindAssetAmt = stockKindAssetAmt + stockKind.getCurTotPrice();
            }
        }

        // ????????? ?????? ??????
        List<AssetStockDto> assetStockList = stockService.getAssetStockDtoList(assetSearch);
        if(assetStockList != null && assetStockList.size() > 0) {
            for (AssetStockDto assetStock : assetStockList) {
                totalLoanBalAmt = totalLoanBalAmt + assetStock.getLoanBalAmt();
            }
        }

        long reAssetAmt = 0;
        long deposit = 0;
        List<AssetReDto> reList = reService.getAssetReDtoList(assetSearch);
        if(reList != null && reList.size() > 0) {
            for (AssetReDto assetRe : reList) {
                if(assetRe.getSaleDate() != null && assetRe.getSaleDate().length() == 8)
                    continue;

                // ????????? ?????? = ???????????? - ????????????????????? - ?????????
                reAssetAmt = reAssetAmt + assetRe.getCurExpectAmt() - assetRe.getCurCostAmt() - assetRe.getDeposit();
                totalLoanBalAmt = totalLoanBalAmt + assetRe.getLoanBalAmt() + assetRe.getDeposit();  // ?????? ??????
                deposit = deposit + assetRe.getDeposit();  // ????????? ??????
            }
        }

        // ?????????
        totalNetAssetAmt = bankAssetAmt + stockKindAssetAmt + reAssetAmt;

        // ?????????
        totalAssetAmt = totalNetAssetAmt + totalLoanBalAmt;

        result.setTotalAssetAmt(totalAssetAmt);
        result.setTotalNetAssetAmt(totalNetAssetAmt);
        result.setTotalLoanBalAmt(totalLoanBalAmt);
        result.setBankAbleAmt(bankAssetAmt);
        result.setStockEvalAmt(stockKindAssetAmt);
        result.setReEvalAmt(reAssetAmt);
        result.setBankList(bankList);
        result.setStockList(stockKindList);
        result.setReList(reList);
        return result;
    }
}
