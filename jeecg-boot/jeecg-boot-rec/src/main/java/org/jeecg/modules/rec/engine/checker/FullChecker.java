package org.jeecg.modules.rec.engine.checker;

import org.apache.commons.collections.ListUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.rec.engine.model.RecParameter;
import org.jeecg.modules.rec.engine.model.TradeData;
import org.jeecg.modules.rec.engine.model.TradeDataRec;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 校验对账
 *
 * @Author: zhou x
 * @Date: 2022/1/22 16:02
 */
@Component
public class FullChecker {

    /**
     * 校对
     *
     * @param main
     * @param side
     * @return
     */
    public List<CheckResult> check(TradeDataRec main, TradeDataRec side, RecParameter parameter) {
        List<TradeData> list = ListUtils.union(main, side);
        List<CheckResult> result = list.stream().collect(Collectors.groupingBy(TradeData::getTradeNo))
                .values().stream().map(op -> {
                    CheckResult check = new CheckResult();
                    op.forEach(it -> {
                        //未出账单判定,账单日期出账临界点
                        if (it.getCreateTime().after(DateUtils.addHours(parameter.getDownDate(), 23)))
                            check.setType(CheckType.notClearByUnOut);
                        if (it.isMain()) check.getMainInfo().add(it);
                        else check.getSideInfo().add(it);
                    });
                    //不确定交易判定
                    if (!(check.getSideInfo().stream().filter(mp -> mp.getAmount() > 0 && mp.getCreateTime().after(DateUtils.addHours(parameter.getStartDate(), 1))).findAny().isPresent() ||
                            check.getMainInfo().stream().filter(mp -> mp.getAmount() > 0 && mp.getCreateTime().after(DateUtils.addHours(parameter.getStartDate(), 1))).findAny().isPresent())) {
                        check.setType(CheckType.notClearByUnKnown);
                    }
                    check.setTradeNo(op.get(0).getTradeNo());
                    check.setOrderNo(op.get(0).getOrderNo());
                    check.setNote(op.get(0).getNote());
                    check.setCreateTime(op.get(0).getCreateTime());
                    check.setAmountSide((float) check.getSideInfo().stream().mapToDouble(mp -> mp.getAmount()).sum());
                    check.setAmountMain((float) check.getMainInfo().stream().mapToDouble(mp -> mp.getAmount()).sum());
                    check.setStatus(check.getAmountMain() == check.getAmountSide() ? CheckStatus.success : CheckStatus.failed);
                    //比对产出结果
                    checkTrade(op.stream().sorted(Comparator.comparing(TradeData::getCreateTime)).collect(Collectors.toList()),
                            check.getCheckItems(), main.getLoaderName(), side.getLoaderName());
                    return check;
                }).collect(Collectors.toList());
        return result;
    }

    /**
     * 单笔交易比对出结果
     *
     * @param list
     * @param checks
     */
    private void checkTrade(List<TradeData> list, List<CheckTradeItem> checks, String mainName, String sideName) {
        TradeData i_data = null;
        TradeData j_data = null;
        for (int i = 0; i < list.size(); i++) {
            CheckTradeItem item = new CheckTradeItem();
            i_data = list.get(i);
            for (int j = 1; j < list.size(); j++) {
                if (i_data.isMain() != list.get(j).isMain() && i_data.getAmount() == list.get(j).getAmount()) {
                    j_data = list.get(j);
                    list.remove(j);
                    break;
                }
            }
            list.remove(i);
            if (j_data != null) break;
            if (i_data.isMain()) {
                item.setAmountMain(i_data.getAmount());
                item.setOpName(sideName);
                if (i_data.getAmount() > 0)
                    item.setOperation(CheckTradeOperation.none);
                else
                    item.setOperation(CheckTradeOperation.refund);
            } else {
                item.setAmountSide(i_data.getAmount());
                item.setOpName(sideName);
                if (i_data.getAmount() > 0)
                    item.setOperation(CheckTradeOperation.refund);
                else
                    item.setOperation(CheckTradeOperation.none);
            }
            checks.add(item);
            break;
        }
        if (list.size() > 0) checkTrade(list, checks, mainName, sideName);
    }
}
