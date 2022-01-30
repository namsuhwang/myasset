package com.idlelife.myasset.component.stock;

import com.idlelife.myasset.models.stock.dto.ScrapKospiStockDto;
import com.idlelife.myasset.models.stock.dto.ScrapStockKindDto;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class JsoupComponent {

    public ScrapStockKindDto getScrapStockKindDto(String kindCode){
        final String scrapStockKind = "https://finance.naver.com/item/main.naver?code=" + kindCode;
        Connection conn = Jsoup.connect(scrapStockKind);
        try {
            Document document = conn.get();
            return parseScrapStockKind(document);
        } catch (IOException ignored) {
        }
        return null;
    }

    private ScrapStockKindDto parseScrapStockKind(Document document){
        ScrapStockKindDto kindDto = new ScrapStockKindDto();

        Elements dl = document.select("dl.blind");
        Element stockElement = dl.get(0);

        String pStr;
        for(Node nodeData : stockElement.childNodes()){
            if(nodeData.getClass().getName().equals(Element.class.getName())){
                pStr = ((Element) nodeData).text();
                if(pStr.contains("종목명")) {
                    kindDto.setStockName(pStr.substring(pStr.lastIndexOf(" ") + 1, pStr.length()));
                }else if(pStr.contains("년") && pStr.contains("월") && pStr.contains("일")) {
                    String basetime = pStr.substring(0, 4) + "-" + pStr.substring(6, 8) + "-" + pStr.substring(10, 12) + " "
                            + pStr.substring(14, 16) + ":" + pStr.substring(18, 20);
                    if(pStr.contains("장마감"))
                        basetime = basetime + " 장마감";
                    kindDto.setBaseTime(basetime);
                }else if(pStr.contains("종목코드")) {
                    kindDto.setKindCode(pStr.substring(pStr.indexOf(" ") + 1, pStr.indexOf(" ") + 7));
                    kindDto.setStockType(pStr.substring(pStr.lastIndexOf(" ") + 1, pStr.length()));
                }else if(pStr.contains("전일가")) {
                    kindDto.setYdPrice(pStr.substring(pStr.lastIndexOf(" ") + 1, pStr.length()));
                }else if(pStr.contains("현재가")) {
                    String[] tmp = pStr.split(" ");
                    kindDto.setPrice(tmp[1]);
                    if(tmp[3].equals("하락")){
                        kindDto.setDiffAmount("▼" + tmp[4]);
                    }else{
                        kindDto.setDiffAmount("▲" + tmp[4]);
                    }

                    if(tmp[5].equals("마이너스")){
                        kindDto.setDayRange("-" + tmp[6]);
                    }else{
                        kindDto.setDayRange("+" + tmp[6]);
                    }
                }else if(pStr.contains("고가")) {
                    kindDto.setHighPrice(pStr.substring(pStr.lastIndexOf(" ") + 1, pStr.length()));
                }else if(pStr.contains("저가")) {
                    kindDto.setLowPrice(pStr.substring(pStr.lastIndexOf(" ") + 1, pStr.length()));
                }
            }
        }

        return kindDto;

    }


    public List<ScrapKospiStockDto> getKosPiStockList() {
        final String stockList = "https://finance.naver.com/sise/sise_market_sum.nhn?&page=1";
        Connection conn = Jsoup.connect(stockList);
        try {
            Document document = conn.get();
            return getKosPiStockList(document);
        } catch (IOException ignored) {
        }
        return null;
    }

    public List<ScrapKospiStockDto> getKosPiStockList(Document document) {
        Elements kosPiTable = document.select("table.type_2 tbody tr");
        List<ScrapKospiStockDto> list = new ArrayList<>();
        for (Element element : kosPiTable) {
            if (element.attr("onmouseover").isEmpty()) {
                continue;
            }
            list.add(createKosPiStockDto(element.select("td")));
        }
        return list;
    }

    public ScrapKospiStockDto createKosPiStockDto(Elements td) {
        ScrapKospiStockDto kospiStockDto = ScrapKospiStockDto.builder().build();
        Class<?> clazz = kospiStockDto.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (int i = 0; i < td.size(); i++) {
            String text;
            if(td.get(i).select(".center a").attr("href").isEmpty()){
                text = td.get(i).text();
            }else{
                text = "https://finance.naver.com" + td.get(i).select(".center a").attr("href");
            }
            fields[i].setAccessible(true);
            try{
                fields[i].set(kospiStockDto,text);
            }catch (Exception ignored){
            }
        }
        return kospiStockDto;
    }
}
