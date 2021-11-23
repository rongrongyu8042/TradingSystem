package com.mars.counter.cache;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.mars.counter.bean.res.StockInfo;
import com.mars.counter.util.DbUtil;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 股票
 */
@Log4j2
@Component
public class StockCache  {
    private HashMultimap<String, StockInfo> invertIndex = HashMultimap.create();

    public Collection<StockInfo> getStocks(String key){
        return invertIndex.get(key);
    }

    private List<String> splitData(String code){
        List<String> list = Lists.newArrayList();
        for(int i=0;i<code.length();i++){
            int inLength = code.length() + 1;
            for(int j = i+1;j<inLength;j++){
                list.add(code.substring(i,j));
            }
        }
        return list;
    }


    /**
     * 建立股票的倒排索引？？
     * 看不懂哦！！！
     * @return
     */
    @PostConstruct
    private void createInvertIndex(){
        log.info("load stock from db");
        long st = System.currentTimeMillis();

        //1.加载股票数据
        List<Map<String,Object>> res = DbUtil.queryAllStockInfo();
        if(CollectionUtils.isEmpty(res)){
            log.error("no stock find in db");
        }
        //2.建立倒排索引
        for(Map<String,Object> r : res){
            int code = Integer.parseInt(r.get("code").toString());
            String name = r.get("name").toString();
            String abbrname = r.get("abbrname").toString();
            StockInfo stock = new StockInfo(code,name,abbrname);
            List<String> codeMetas = splitData(String.format("%06d",code));
            List<String> abbrNameMetas = splitData(abbrname);
            codeMetas.addAll(abbrNameMetas);

            for(String key:codeMetas){
                Collection<StockInfo> stockInfos = invertIndex.get(key);
                if(!CollectionUtils.isEmpty(stockInfos) && stockInfos.size()>10) {
                    continue;
                }
                invertIndex.put(key , stock);
            }
        }
        log.info("load stock finish,take :" +
                (System.currentTimeMillis() - st) + "ms");
    }
}
