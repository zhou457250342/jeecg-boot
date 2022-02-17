package org.jeecg.modules.rec.engine.model;

import lombok.Data;
import org.jeecg.common.util.SpringContextHolder;
import org.jeecg.modules.rec.engine.resource.ResourceLoader;

import java.util.ArrayList;

/**
 * @Author: zhou x
 * @Date: 2022/2/17 14:39
 */
@Data
public class TradeDataRec extends ArrayList<TradeData> {
    public TradeDataRec(String loaderName, boolean isMain) {
        super();
        this.setLoaderName(loaderName);
        this.setMain(isMain);
//        this.setLoader(SpringContextHolder.getBean(loaderName + "ResourceLoader"));
    }

    private boolean main;
    private String loaderName;
//    private ResourceLoader loader;

    @Override
    public boolean add(TradeData tradeData) {
        tradeData.setMain(isMain());
        return super.add(tradeData);
    }
}
