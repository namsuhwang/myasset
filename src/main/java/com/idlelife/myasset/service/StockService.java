package com.idlelife.myasset.service;

import com.idlelife.myasset.common.CommonUtil;
import com.idlelife.myasset.common.exception.MyassetException;
import com.idlelife.myasset.models.asset.dto.AssetDto;
import com.idlelife.myasset.models.asset.entity.AssetEntity;
import com.idlelife.myasset.models.asset.form.AssetForm;
import com.idlelife.myasset.models.stock.StockSearch;
import com.idlelife.myasset.models.stock.dto.*;
import com.idlelife.myasset.models.stock.entity.AssetStockEntity;
import com.idlelife.myasset.models.stock.entity.StockInterestEntity;
import com.idlelife.myasset.models.stock.entity.StockKindEntity;
import com.idlelife.myasset.models.stock.entity.StockTradeEntity;
import com.idlelife.myasset.models.stock.form.AssetStockForm;
import com.idlelife.myasset.models.stock.form.StockInterestForm;
import com.idlelife.myasset.models.stock.form.StockKindForm;
import com.idlelife.myasset.models.stock.form.StockTradeForm;
import com.idlelife.myasset.repository.StockMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.idlelife.myasset.models.common.ErrorCode.*;

@Log4j2
@Service
@Transactional
public class StockService {
    @Autowired
    StockMapper stockMapper;

    @Autowired
    AssetService assetService;

    @Autowired
    ScrapStockService scrapStockService;

    @Autowired
    CommonService commonService;

    public AssetStockDto getAssetStockDto(Long assetId){
        return stockMapper.selectAssetStockDto(assetId);
    }

    public AssetStockEntity getAssetStock(Long assetId){
        return stockMapper.selectAssetStock(assetId);
    }

    public TotalStockAssetDto getTotalStockAssetDto(){
        log.info("TotalStockAssetDto 시작");
        TotalStockAssetDto result = new TotalStockAssetDto();
        StockSearch param = new StockSearch();
        param.setMemberId(CommonUtil.getAuthInfo().getMemberId());
        param.setDeleteYn("N");
        List<StockKindDto> stockKindDtoList = stockMapper.selectStockKindDtoList(param);

        Long totBuyPrice = 0L;
        Long totCurPrice = 0L;
        Long totPnlAmt = 0L;

        for(StockKindDto kindDto : stockKindDtoList){
            kindDto = getCurStockKind(kindDto);
            totBuyPrice =+ kindDto.getBuyTotPrice();
            totCurPrice =+ kindDto.getCurTotPrice();
            totPnlAmt =+ kindDto.getPnlAmt();
        }

        if(stockKindDtoList != null && stockKindDtoList.size() > 0){
            String baseTime = stockKindDtoList.get(0).getBaseTime().replaceAll("코스피", "").replaceAll("코스닥", "");
            result.setBaseTime(baseTime);
        }
        Double totPnlRate = Math.round((double)totPnlAmt / (double)totBuyPrice * 10000.0) / 100.0;

        result.setTotBuyPrice(totBuyPrice);
        result.setTotCurPrice(totCurPrice);
        result.setTotPnlRate(totPnlRate);
        result.setTotPnlAmt(totPnlAmt);
        result.setList(stockKindDtoList);
        log.info("TotalStockAssetDto result : " + result.toString());
        return result;
    }


    public AssetStockDto regAssetStock(AssetStockForm form){
        log.info("Asset 등록");
        AssetForm assetForm = new AssetForm();
        assetForm.setAssetName(form.getAssetName());
        assetForm.setAssetType("STOCK");
        AssetEntity assetEntity = assetService.getAssetEntityFromForm(assetForm);
        AssetDto assetDto = assetService.regAsset(assetEntity);
        if(assetDto == null){
            throw new MyassetException("DB 에러 : 자산 등록 실패", MYASSET_ERROR_1000);
        }

        log.info("AssetStock 등록");
        form.setAssetId(assetDto.getAssetId());
        AssetStockEntity assetStockEntity = getAssetStockEntityFromForm(form);
        int cnt = stockMapper.insertAssetStock(assetStockEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 증권사 등록 실패", MYASSET_ERROR_1000);
        }
        AssetStockDto assetStockDto = stockMapper.selectAssetStockDto(assetStockEntity.getAssetId());
        return assetStockDto;
    }

    public AssetStockDto modAssetStock(AssetStockForm form){
        log.info("Asset 수정");
        log.info("기존 Asset 조회");
        AssetEntity assetEntity = assetService.getAsset(form.getAssetId());
        assetEntity.setAssetName(form.getAssetName());
        AssetDto assetDto = assetService.modAsset(assetEntity);
        if(assetDto == null){
            throw new MyassetException("DB 에러 : 자산 수정 실패", MYASSET_ERROR_1000);
        }

        AssetStockEntity assetStockEntity = getAssetStockEntityFromForm(form);
        int cnt = stockMapper.updateAssetStock(assetStockEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }

        AssetStockDto assetStockDto = stockMapper.selectAssetStockDto(assetStockEntity.getAssetId());
        return assetStockDto;
    }

    public AssetStockDto delAssetStock(Long assetId){
        int cnt = stockMapper.deleteAssetStock(assetId);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 증권사 삭제 실패", MYASSET_ERROR_1000);
        }
        return stockMapper.selectAssetStockDto(assetId);
    }

    public List<AssetStockDto> getAssetStockDtoList(StockSearch dom){
        List<AssetStockDto> list = stockMapper.selectAssetStockDtoList(dom);
        return list;
    }

    private AssetStockEntity getAssetStockEntityFromForm(AssetStockForm form){
        AssetStockEntity assetStockEntity = new AssetStockEntity();
        assetStockEntity.setAssetId(form.getAssetId());
        assetStockEntity.setMemberId(CommonUtil.getAuthInfo().getMemberId());
        assetStockEntity.setOrgCd(form.getOrgCd());
        assetStockEntity.setOrgName(form.getOrgName());
        assetStockEntity.setStockAcno(form.getStockAcno());
        assetStockEntity.setAbleAmt(form.getAbleAmt());
        assetStockEntity.setLoanBalAmt(form.getLoanBalAmt());
        assetStockEntity.setLoanRate(form.getLoanRate());
        assetStockEntity.setDeleteYn("N");
        return assetStockEntity;
    }






    public StockKindDto getStockKindDto(Long stockKindId){
        return stockMapper.selectStockKindDto(stockKindId);
    }

    public StockKindEntity getStockKind(Long stockKindId){
        return stockMapper.selectStockKind(stockKindId);
    }

    public StockKindDto regStockKind(StockKindForm form){
        log.info("주식 종목(StockKind) 등록");
        if(form.getAssetId() == null){
            log.error("Asset 먼저 등록");
            throw new MyassetException(MYASSET_ERROR_1001);
        }

        AssetStockEntity assetStockEntity = stockMapper.selectAssetStock(form.getAssetId());
        if(assetStockEntity == null){
            log.error("증권사(AssetStock) 먼저 등록");
            throw new MyassetException(MYASSET_ERROR_1002);
        }

        log.info("주식 종목 코드 등록/수정(StockKindCode)");
        setStockKindCode(form.getStockKindCd(), form.getStockKindName());

        log.info("자산 종목(StockKind) 등록");
        StockKindEntity stockKindEntity = getStockKindEntityFromForm(form);
        long pnlAmt = stockKindEntity.getCurTotPrice() - stockKindEntity.getBuyTotPrice();
        double pnlRate = Math.round((double)pnlAmt / (double)stockKindEntity.getBuyTotPrice() * 10000.0) / 100.0;
        stockKindEntity.setPnlAmt(pnlAmt);
        stockKindEntity.setPnlRate(pnlRate);
        int cnt = stockMapper.insertStockKind(stockKindEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 주식 종목 등록 실패", MYASSET_ERROR_1000);
        }
        StockKindDto stockKindDto = stockMapper.selectStockKindDto(stockKindEntity.getStockKindId());
        return stockKindDto;
    }


    /*  stock_kind 테이블에서 현재가격 및 손익정보 관리 하지 않기로 함에 따라 주석처리
    // 현재 주가 정보 조회 및 반영
    private StockKindDto updateCurStockKind(StockKindDto kindDto){
        ScrapStockKindDto curStockInfo = scrapStockService.getScrapStockKind(kindDto.getStockKindCd());
        long curPrice = Long.valueOf(curStockInfo.getPrice().replaceAll(",", ""));
        long curTotPrice = curPrice * kindDto.getQuantity();
        long pnlAmt = curTotPrice - kindDto.getBuyTotPrice();
        double pnlRate = Math.round((double)pnlAmt / (double)kindDto.getBuyTotPrice() * 10000.0) / 100.0;

        StockKindEntity stockKindEntity = new StockKindEntity();
        stockKindEntity.setStockKindId(kindDto.getStockKindId());
        stockKindEntity.setCurUnitPrice(curPrice);
        stockKindEntity.setCurTotPrice(curTotPrice);
        stockKindEntity.setPnlAmt(pnlAmt);
        stockKindEntity.setPnlRate(pnlRate);
        int cnt = assetStockMapper.updateStockKindCurrentStatus(stockKindEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : updateCurStockKind 주식 종목 수정 실패", MYASSET_ERROR_1000);
        }

        kindDto.setBaseTime(curStockInfo.getBaseTime());
        kindDto.setCurUnitPrice(curPrice);
        kindDto.setCurTotPrice(stockKindEntity.getCurTotPrice());
        kindDto.setPnlAmt(pnlAmt);
        kindDto.setPnlRate(pnlRate);
        kindDto.setDiffAmount(curStockInfo.getDiffAmount());
        kindDto.setDayRange(curStockInfo.getDayRange());
        kindDto.setHighPrice(curStockInfo.getHighPrice());
        kindDto.setLowPrice(curStockInfo.getLowPrice());
        return kindDto;
    }
        */

    // 현재 주가 정보 조회 및 반영
    private StockKindDto getCurStockKind(StockKindDto kindDto){
        ScrapStockKindDto curStockInfo = scrapStockService.getScrapStockKind(kindDto.getStockKindCd());
        long curPrice = Long.valueOf(curStockInfo.getPrice().replaceAll(",", ""));
        long curTotPrice = curPrice * kindDto.getQuantity();
        long pnlAmt = curTotPrice - kindDto.getBuyTotPrice();
        double pnlRate = Math.round((double)pnlAmt / (double)kindDto.getBuyTotPrice() * 10000.0) / 100.0;

        StockKindEntity stockKindEntity = new StockKindEntity();
        stockKindEntity.setStockKindId(kindDto.getStockKindId());
        stockKindEntity.setCurUnitPrice(curPrice);
        stockKindEntity.setCurTotPrice(curTotPrice);
        stockKindEntity.setPnlAmt(pnlAmt);
        stockKindEntity.setPnlRate(pnlRate);
        /*
        int cnt = assetStockMapper.updateStockKindCurrentStatus(stockKindEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : updateCurStockKind 주식 종목 수정 실패", MYASSET_ERROR_1000);
        }
        */

        kindDto.setBaseTime(curStockInfo.getBaseTime());
        kindDto.setCurUnitPrice(curPrice);
        kindDto.setCurTotPrice(stockKindEntity.getCurTotPrice());
        kindDto.setPnlAmt(pnlAmt);
        kindDto.setPnlRate(pnlRate);
        kindDto.setDiffAmount(curStockInfo.getDiffAmount());
        kindDto.setDayRange(curStockInfo.getDayRange());
        kindDto.setHighPrice(curStockInfo.getHighPrice());
        kindDto.setLowPrice(curStockInfo.getLowPrice());
        return kindDto;
    }

    public StockKindDto modStockKind(StockKindForm form){
        log.info("주식 종목(StockKind) 수정");
        if(form.getAssetId() == null){
            log.error("Asset 먼저 등록");
            throw new MyassetException(MYASSET_ERROR_1001);
        }

        // 증권사(AssetStock) 는 예수금, 대출잔액관 관리하므로 종목(StockKind) 관리에서는 업데이트 하지 않음
        AssetStockEntity assetStockEntity = stockMapper.selectAssetStock(form.getAssetId());
        if(assetStockEntity == null){
            log.error("증권사(AssetStock) 먼저 등록");
            throw new MyassetException(MYASSET_ERROR_1002);
        }

        log.info("주식 종목 코드 등록/수정(StockKindCode)");
        setStockKindCode(form.getStockKindCd(), form.getStockKindName());

        log.info("주식 종목(StockKind) 수정");
        StockKindEntity stockKindEntity = getStockKindEntityFromForm(form);

        int cnt = stockMapper.updateStockKind(stockKindEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : modStockKind 주식 종목 수정 실패", MYASSET_ERROR_1000);
        }

        StockKindDto stockKindDto = stockMapper.selectStockKindDto(stockKindEntity.getStockKindId());
        return stockKindDto;
    }

    public StockKindDto delStockKind(Long stockKindId){
        int cnt = stockMapper.deleteStockKind(stockKindId);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 주식 종목 삭제 실패", MYASSET_ERROR_1000);
        }
        return stockMapper.selectStockKindDto(stockKindId);
    }

    public List<StockKindDto> getStockKindDtoList(StockSearch dom){
        List<StockKindDto> list = stockMapper.selectStockKindDtoList(dom);
        return list;
    }

    public List<StockKindCodeDto> getStockKindCodeDtoList(StockKindForm dom){
        List<StockKindCodeDto> list = stockMapper.selectStockKindCodeDtoList(dom);
        return list;
    }

    public StockKindDto updateStockKindCurrentStatus(StockKindForm form){
        log.info("주식 종목 현재가 수정");
        if(form.getAssetId() == null){
            log.error("Asset 먼저 등록");
            throw new MyassetException(MYASSET_ERROR_1001);
        }

        // 증권사(AssetStock) 는 예수금, 대출잔액관 관리하므로 종목(StockKind) 관리에서는 업데이트 하지 않음
        AssetStockEntity assetStockEntity = stockMapper.selectAssetStock(form.getAssetId());
        if(assetStockEntity == null){
            log.error("증권사(AssetStock) 먼저 등록");
            throw new MyassetException(MYASSET_ERROR_1002);
        }

        log.info("주식 종목(StockKind) 수정");
        StockKindEntity stockKindEntity = getStockKindEntityFromForm(form);

        int cnt = stockMapper.updateStockKind(stockKindEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 주식 종목 수정 실패", MYASSET_ERROR_1000);
        }

        StockKindDto stockKindDto = stockMapper.selectStockKindDto(stockKindEntity.getStockKindId());
        return stockKindDto;
    }




    private StockKindEntity getStockKindEntityFromForm(StockKindForm form){
        StockKindEntity stockKindEntity = new StockKindEntity();
        stockKindEntity.setStockKindId(form.getStockKindId() == null ? stockMapper.createStockKindId() : form.getStockKindId());
        stockKindEntity.setAssetId(form.getAssetId());
        stockKindEntity.setMemberId(CommonUtil.getAuthInfo().getMemberId());
        stockKindEntity.setStockKindCd(form.getStockKindCd());
        stockKindEntity.setStockKindName(form.getStockKindName());
        stockKindEntity.setQuantity(form.getQuantity());
        stockKindEntity.setBuyAvgPrice(form.getBuyAvgPrice());
        stockKindEntity.setBuyTotPrice(form.getBuyTotPrice());
        stockKindEntity.setCurUnitPrice(form.getCurUnitPrice());
        stockKindEntity.setCurTotPrice(form.getCurTotPrice());
        stockKindEntity.setPnlRate(form.getPnlRate());
        stockKindEntity.setPnlAmt(form.getPnlAmt());
        stockKindEntity.setDeleteYn("N");
        return stockKindEntity;
    }










    public StockTradeDto getStockTradeDto(Long stockTradeId){
        return stockMapper.selectStockTradeDto(stockTradeId);
    }

    public StockTradeEntity getStockTrade(Long stockTradeId){
        return stockMapper.selectStockTrade(stockTradeId);
    }

    public StockTradeDto regStockTrade(StockTradeForm form){
        log.info("주식 거래 등록");

        long buyTotPrice = 0;
        long avgPrice = 0;

        StockKindEntity stockKindEntity = stockMapper.selectStockKind(form.getStockKindId());
        if(stockKindEntity == null){
            log.error("주식 종목(StockKind) 먼저 등록");
            throw new MyassetException(MYASSET_ERROR_1003);
        }

        log.info("주식 종목 정보 업데이트");
        StockTradeEntity stockTradeEntity = getStockTradeEntityFromForm(form);

        if(stockKindEntity != null) {
            stockTradeEntity.setBefQuantity(stockKindEntity.getQuantity());
            stockTradeEntity.setBefBuyAvgPrice(stockKindEntity.getBuyAvgPrice());
            stockTradeEntity.setBefBuyTotPrice(stockKindEntity.getBuyTotPrice());
        }else{
            stockTradeEntity.setBefQuantity(0L);
            stockTradeEntity.setBefBuyAvgPrice(0L);
            stockTradeEntity.setBefBuyTotPrice(0L);
        }

        log.info("주식 평단가 계산");
        log.info("보유수량, 평단가 등 계산");

        if(form.getTrType().equalsIgnoreCase("BUY")) {
            log.info("매수");
            stockTradeEntity.setTradeTypeName("매수");
            stockTradeEntity.setPnlAmt(0L);
            stockKindEntity.setQuantity(stockKindEntity.getQuantity() + stockTradeEntity.getTradeQuantity());
            buyTotPrice = stockKindEntity.getBuyTotPrice() + stockTradeEntity.getTradeAmt();
        }else{
            log.info("매도");
            stockTradeEntity.setTradeTypeName("매도");
            if((stockKindEntity.getQuantity() - stockTradeEntity.getTradeQuantity()) > 0){
                throw new MyassetException(MYASSET_ERROR_1003);
            }
            // 손익금액 = (거래단가 - 직전 평단가) X 거래수량
            // 손익율 = 손익금액 / (직전 평단가 X 거래수량)
            long pnlAmt = (stockTradeEntity.getTradeUnitPrice() - stockKindEntity.getBuyAvgPrice()) * stockTradeEntity.getTradeQuantity();
            Double pnlRate = Math.round((double)pnlAmt / (stockKindEntity.getBuyAvgPrice() * stockTradeEntity.getTradeQuantity() * 10000.0)) / 100.0;
            stockTradeEntity.setPnlAmt(pnlAmt);
            stockTradeEntity.setPnlRate(pnlRate);

            stockKindEntity.setQuantity(stockKindEntity.getQuantity() - stockTradeEntity.getTradeQuantity());
            buyTotPrice = stockKindEntity.getBuyTotPrice() - stockTradeEntity.getTradeAmt();
        }
        avgPrice = buyTotPrice / stockKindEntity.getQuantity();
        stockKindEntity.setBuyTotPrice(buyTotPrice);
        stockKindEntity.setBuyAvgPrice(avgPrice);
        if(stockMapper.updateStockKind(stockKindEntity) < 1){
            throw new MyassetException("DB 에러 : 주식 종목 정보 업데이트 실패", MYASSET_ERROR_1000);
        }

        log.info("주식 거래내역 등록");
        stockTradeEntity.setAftQuantity(stockKindEntity.getQuantity());
        stockTradeEntity.setAftBuyAvgPrice(stockKindEntity.getBuyAvgPrice());
        stockTradeEntity.setAftBuyTotPrice(stockKindEntity.getBuyTotPrice());

        int cnt = stockMapper.insertStockTrade(stockTradeEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 주식 거래 내역 등록 실패", MYASSET_ERROR_1000);
        }

        StockTradeDto resultDto = stockMapper.selectStockTradeDto(stockTradeEntity.getStockTradeId());
        return resultDto;
    }

    public StockTradeDto modStockTrade(StockTradeForm form){
        StockTradeEntity stockTradeEntity = getStockTradeEntityFromForm(form);

        int cnt = stockMapper.updateStockTrade(stockTradeEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 주식 거래 내역 조회 실패", MYASSET_ERROR_1000);
        }
        return stockMapper.selectStockTradeDto(stockTradeEntity.getStockTradeId());
    }
    public StockTradeDto delStockTrade(Long stockTradeId){
        int cnt = stockMapper.deleteStockTrade(stockTradeId);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return stockMapper.selectStockTradeDto(stockTradeId);
    }

    public StockTradeHistoryDto getStockTradeHistory(StockSearch dom){
        Long totalBuyAmt = 0L;
        Long totalSaleAmt = 0L;
        List<StockTradeDto> list = stockMapper.selectStockTradeDtoList(dom);

        for(StockTradeDto dto : list){
            if(dto.getTradeType().equalsIgnoreCase("SALE")){
                totalBuyAmt = totalBuyAmt + dto.getBefQuantity() * dto.getBefBuyAvgPrice();
                totalSaleAmt = totalSaleAmt + dto.getTradeQuantity() * dto.getTradeUnitPrice();
            }
        }

        Long realPnlAmt = 0L;
        Double realPnlRate = 0.0;
        if(totalBuyAmt != null && totalBuyAmt > 0) {
            realPnlAmt = totalSaleAmt - totalBuyAmt;
            realPnlRate = Math.round(realPnlAmt / totalBuyAmt * 10000.0) / 100.0;
        }
        StockTradeHistoryDto result = new StockTradeHistoryDto();
        result.setRealPnlAmt(realPnlAmt);
        result.setRealPnlRate(realPnlRate);
        result.setList(list);
        return result;
    }

    private StockTradeEntity getStockTradeEntityFromForm(StockTradeForm form){
        if(CommonUtil.isNullEmpty(form.getStockKindCd())
            || CommonUtil.isNullEmpty(form.getQuantity().toString()) || form.getQuantity() <= 0
            || CommonUtil.isNullEmpty(form.getUnitPrice().toString()) || form.getUnitPrice() <= 0){
            throw new MyassetException(MYASSET_ERROR_1004);
        }
        Long trAmt = form.getQuantity() * form.getUnitPrice();
        StockTradeEntity stockTradeEntity = new StockTradeEntity();
        stockTradeEntity.setStockTradeId(form.getStockTradeId() == null ? stockMapper.createStockTradeId() : form.getStockTradeId());
        stockTradeEntity.setTradeType(form.getTrType());
        stockTradeEntity.setStockKindId(form.getStockKindId());
        LocalDateTime tradeDate = LocalDateTime.parse(form.getTrDate().replaceAll("-", "") + form.getTrTime().replaceAll(":", "") + "00", DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        stockTradeEntity.setTradeDatetime(tradeDate);
        stockTradeEntity.setTradeTypeName(form.getTrTypeName());
        stockTradeEntity.setTradeQuantity(form.getQuantity());
        stockTradeEntity.setTradeUnitPrice(form.getUnitPrice());
        stockTradeEntity.setTradeAmt(trAmt);
        stockTradeEntity.setTaxAmt(form.getTaxAmt());
        stockTradeEntity.setFeeAmt(form.getFeeAmt());
        stockTradeEntity.setDeleteYn("N");
        return stockTradeEntity;
    }



    private void setStockKindCode(String code, String kindName){
        StockKindCodeDto stockKindCodeDto;
        stockKindCodeDto = commonService.getStockKindCode(code);
        if(stockKindCodeDto == null){
            stockKindCodeDto = new StockKindCodeDto();
            stockKindCodeDto.setCode(code);
            stockKindCodeDto.setKindName(kindName);
            commonService.insertStockKindCode(stockKindCodeDto);
        }else if(!stockKindCodeDto.getKindName().equals(kindName)){
            stockKindCodeDto.setKindName(kindName);
            commonService.updateStockKindCode(stockKindCodeDto);
        }
    }


    // 관심종목 실시간 조회
    public StockInterestListDto getStockInterestListDto(Long memberId){
        log.info("getStockInterestListDto 시작 memberId : " + memberId);
        StockInterestListDto result = new StockInterestListDto();

        String baseTime = "";
        List<StockInterestDto> stockInterestDtoList = stockMapper.selectStockInterestDtoList(memberId);

        for(StockInterestDto siDto : stockInterestDtoList){
            ScrapStockKindDto stockInfo = scrapStockService.getScrapStockKind(siDto.getStockKindCd());
            siDto.setDayRange(stockInfo.getDayRange());
            siDto.setDiffAmount(stockInfo.getDiffAmount());
            siDto.setHighPrice(stockInfo.getHighPrice());
            siDto.setLowPrice(stockInfo.getLowPrice());
            siDto.setCurUnitPrice(stockInfo.getPrice());
            baseTime = stockInfo.getBaseTime().replaceAll("코스피", "").replaceAll("코스닥", "");
        }

        result.setBaseTime(baseTime);

        log.info("getStockInterestListDto result : " + result.toString());
        return result;
    }


    public StockInterestListDto regStockInterest(StockInterestForm form){
        log.info("관심주식 (regStockInterest) 등록");

        StockInterestEntity stockInterestEntity = getStockInterestEntityFromForm(form);
        int cnt = stockMapper.insertStockInterest(stockInterestEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 관심주식 등록 실패", MYASSET_ERROR_1000);
        }
        StockInterestListDto result = getStockInterestListDto(stockInterestEntity.getMemberId());
        return result;
    }

    public StockInterestListDto modStockInterest(StockInterestForm form){
        log.info("관심주식 (regStockInterest) 수정");

        StockInterestEntity stockInterestEntity = getStockInterestEntityFromForm(form);
        int cnt = stockMapper.updateStockInterest(stockInterestEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 관심주식 수정 실패", MYASSET_ERROR_1000);
        }
        StockInterestListDto result = getStockInterestListDto(stockInterestEntity.getMemberId());
        return result;
    }

    public StockInterestListDto delStockInterest(long stockInterestId){
        log.info("관심주식 (delStockInterest) 삭제");
        Map<String, String> resultMap = new HashMap<>();
        StockInterestEntity ety = getStockInterest(stockInterestId);

        int cnt = stockMapper.deleteStockInterest(stockInterestId);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 관심주식 삭제 실패", MYASSET_ERROR_1000);
        }

        StockSearch sqlParam = new StockSearch();
        sqlParam.setMemberId(ety.getMemberId());
        sqlParam.setOrderNo(ety.getOrderNo());
        cnt = stockMapper.chgStockInterestOrderNoDel(sqlParam);

        StockInterestListDto result = getStockInterestListDto(ety.getMemberId());
        return result;
    }


    public StockInterestListDto chgOrderNoStockInterest(StockInterestForm dom){
        log.info("관심주식 순서 변경");
        Map<String, String> resultMap = new HashMap<>();
        StockInterestEntity ety = getStockInterest(dom.getStockInterestId());
        long befOrderNo = ety.getOrderNo();
        long aftOrderNo = dom.getOrderNo();

        StockSearch sqlParam = new StockSearch();
        sqlParam.setMemberId(dom.getMemberId());
        if(aftOrderNo > befOrderNo){
            sqlParam.setStartOrderNo(befOrderNo + 1);
            sqlParam.setEndOrderNo(aftOrderNo);
            stockMapper.chgStockInterestOrderNoMinus(sqlParam);
        }else{
            sqlParam.setStartOrderNo(aftOrderNo);
            sqlParam.setEndOrderNo(befOrderNo - 1);
            stockMapper.chgStockInterestOrderNoPlus(sqlParam);
        }

        StockSearch orderParam = new StockSearch();
        orderParam.setMemberId(dom.getMemberId());
        orderParam.setOrderNo(dom.getOrderNo());
        int cnt = stockMapper.updateStockInterestOrder(orderParam);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 관심주식 정렬 수정 실패", MYASSET_ERROR_1000);
        }
        StockInterestListDto result = getStockInterestListDto(dom.getMemberId());
        return result;
    }

    public StockInterestDto getStockInterestDto(long stockInterestId){
        log.info("관심주식 (regStockInterest) 조회");

        StockInterestDto dto = stockMapper.selectStockInterestDto(stockInterestId);
        if(dto == null){
            throw new MyassetException("DB 에러 : 관심주식 조회 실패", MYASSET_ERROR_1000);
        }
        return dto;
    }

    public StockInterestEntity getStockInterest(long stockInterestId){
        log.info("관심주식 (regStockInterest) 조회");

        StockInterestEntity ety = stockMapper.selectStockInterest(stockInterestId);
        if(ety == null){
            throw new MyassetException("DB 에러 : 관심주식 조회 실패", MYASSET_ERROR_1000);
        }
        return ety;
    }

    private StockInterestEntity getStockInterestEntityFromForm(StockInterestForm form){
        long orderNo = stockMapper.createStockInterestOrderNo(form.getMemberId());
        StockInterestEntity stockInterestEntity = new StockInterestEntity();
        stockInterestEntity.setStockInterestId(form.getStockInterestId() == null ? stockMapper.createStockInterestId() : form.getStockInterestId());
        stockInterestEntity.setMemberId(form.getMemberId());
        stockInterestEntity.setStockKindCd(form.getStockKindCd());
        stockInterestEntity.setStockKindName(form.getStockKindName());
        stockInterestEntity.setOrderNo(orderNo);
        return stockInterestEntity;
    }

}
