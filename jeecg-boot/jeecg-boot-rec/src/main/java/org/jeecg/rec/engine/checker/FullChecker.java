package org.jeecg.rec.engine.checker;

import org.jeecg.rec.engine.model.TradeData;
import org.jeecg.rec.engine.resource.ResourceLoader;
import org.apache.commons.collections.ListUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 校验对账
 *
 * @Author: zhou x
 * @Date: 2022/1/22 16:02
 */
public class FullChecker {
    /**
     * 校对
     *
     * @param mainLoader
     * @param sideLoader
     * @return
     */
    public List<CheckResult> check(Date date, ResourceLoader mainLoader, ResourceLoader sideLoader) {
        List<TradeData> main = mainLoader.getList(date);
        List<TradeData> side = sideLoader.getList(date);
        return check(main, side);
    }

    /**
     * 校对
     *
     * @param main
     * @param side
     * @return
     */
    public List<CheckResult> check(List<TradeData> main, List<TradeData> side) {
        if (main != null) main.forEach(op -> op.setMain(true));
        List<TradeData> list = ListUtils.union(main, side);
        List<CheckResult> result = list.stream().collect(Collectors.groupingBy(TradeData::getTradeNo))
                .values().stream().map(op -> {
                    CheckResult temp = new CheckResult();
                    temp.setTradeNo(op.get(0).getTradeNo());
                    temp.setOrderNo(op.get(0).getOrderNo());
                    temp.setNote(op.get(0).getNote());
                    temp.setCreateTime(op.get(0).getCreateTime());
                    op.forEach(it -> {
                        if (it.isMain()) temp.getMainInfo().add(it);
                        else temp.getSideInfo().add(it);
                    });
                    temp.setAmountSide(temp.getSideInfo().stream().mapToDouble(mp -> mp.getAmount()).sum());
                    temp.setAmountMain(temp.getMainInfo().stream().mapToDouble(mp -> mp.getAmount()).sum());
                    temp.setType(temp.getAmountMain() == temp.getAmountSide() ? CheckType.success : CheckType.failed);
                    return temp;
                }).collect(Collectors.toList());
        return result;
    }
}
