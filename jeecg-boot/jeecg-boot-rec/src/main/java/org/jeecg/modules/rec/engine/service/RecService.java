package org.jeecg.modules.rec.engine.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang.StringUtils;
import org.apache.http.annotation.Obsolete;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.SpringContextHolder;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.rec.engine.checker.CheckResult;
import org.jeecg.modules.rec.engine.checker.FullChecker;
import org.jeecg.modules.rec.engine.model.RecException;
import org.jeecg.modules.rec.engine.model.TradeData;
import org.jeecg.modules.rec.entity.ModRec;
import org.jeecg.modules.rec.entity.ModRecComparison;
import org.jeecg.modules.rec.entity.ModRecModel;
import org.jeecg.modules.rec.service.IModRecComparisonService;
import org.jeecg.modules.rec.service.IModRecModelService;
import org.jeecg.modules.rec.service.IModRecService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 对账服务
 *
 * @Author: zhou x
 * @Date: 2022/2/15 16:17
 */
public class RecService {
    private String mainName;
    private String sideName;
    private IModRecService modRecService;
    private IModRecModelService modRecModelService;
    private IModRecComparisonService modRecComparisonService;

    public RecService(String mainName, String sideName) {
        this.mainName = mainName;
        this.sideName = sideName;
        this.modRecService = SpringContextHolder.getBean(IModRecService.class);
        this.modRecComparisonService = SpringContextHolder.getBean(IModRecComparisonService.class);
        this.modRecModelService = SpringContextHolder.getBean(IModRecModelService.class);
    }

    public RecService() {
        this(null, null);
    }

    /**
     * 系统天对账
     *
     * @param date
     */
    @Obsolete
    public void recDay(Date date) {
//        if (validateCanRec()) throw new RecException("账单未下载完毕，无法对账");
//        List<TradeData> mainList = repairData(searchBillList(mainName, date, null), true);
//        List<TradeData> sideList = repairData(searchBillList(sideName, date, null), false);
//        FullChecker.check(mainList, sideList);
    }

    /**
     * 是否可以进行系统对账
     *
     * @return
     */
    public boolean validateCanRec() {
        ModRec modRec = modRecService.getOne(Wrappers.<ModRec>lambdaQuery().eq(ModRec::getName, mainName));
        ModRec modRecSide = modRecService.getOne(Wrappers.<ModRec>lambdaQuery().eq(ModRec::getName, sideName));
        if (modRec == null || modRecSide == null) return false;
        String date = DateUtils.formatDate(DateUtils.addDays(new Date(), -1), "yyyy-MM-dd");
        return date.equals(modRec.getContent().getString("billingDownDate")) && date.equals(modRecSide.getContent().getString("billingDownDate"));
    }

    /**
     * 获取业务库账单
     *
     * @return
     */
    public List<ModRecModel> searchBillList(String name, Date date, String tradeNo) {
        if (date == null && StringUtils.isBlank(tradeNo)) return null;
        try {
            MybatisPlusSaasConfig.tableModRecModel.set("mod_rec_" + name);
            LambdaQueryWrapper<ModRecModel> wrapper = Wrappers.lambdaQuery();
            if (StringUtils.isNotBlank(tradeNo))
                wrapper.eq(ModRecModel::getTradeNo, tradeNo);
            if (date != null)
                wrapper.apply("create_time BETWEEN {0} AND {1}",
                        DateUtils.formatDate(date, "yyyy-MM-dd 00:00:00"), DateUtils.formatDate(date, "yyyy-MM-dd 23:59:59"));
            return modRecModelService.list(wrapper);
        } catch (Exception ex) {
            throw new RecException("账单获取失败", ex);
        } finally {
            MybatisPlusSaasConfig.tableModRecModel.remove();
        }
    }

    /**
     * 转换
     *
     * @param models
     * @return
     */
    public List<TradeData> repairData(List<ModRecModel> models, boolean isMain) {
        if (CollectionUtils.isEmpty(models)) return null;
        List<TradeData> dataList = new ArrayList<>();
        models.forEach(op -> {
            TradeData tradeData = new TradeData();
            tradeData.setOperationNo(op.getBussNo());
            tradeData.setAmount(op.getAmount());
            tradeData.setCreateTime(op.getCreateTime());
            tradeData.setNote(op.getNote());
            tradeData.setTradeNo(op.getTradeNo());
            tradeData.setUniqueNo(op.getUniqueNo());
            tradeData.setOrderNo(op.getOrderNo());
            tradeData.setMain(isMain);
            dataList.add(tradeData);
        });
        return dataList;
    }

    /**
     * 对账记录
     *
     * @return
     */
    public void saveRecResult(List<ModRecComparison> modRecComparisons) {
        if (CollectionUtils.isEmpty(modRecComparisons)) return;
        try {
            MybatisPlusSaasConfig.tableModRecComparison.set("mod_rec_res_" + mainName + "_" + sideName);
            modRecComparisonService.insertOrUpdateBatch(modRecComparisons);
        } catch (Exception ex) {
            throw new RecException("对账记录保存失败", ex);
        } finally {
            MybatisPlusSaasConfig.tableModRecComparison.remove();
        }
    }

    /**
     * 获取账单下载日期
     *
     * @param name
     * @return
     */
    public Date billingDownExcDate(String name) {
        ModRec modRec = modRecService.getOne(Wrappers.<ModRec>lambdaQuery()
                .eq(ModRec::getName, name)
                .eq(ModRec::getType, "billingDown"));
        if (modRec == null || modRec.getContent() == null) return null;
        return DateUtils.parseDateDef(modRec.getContent().getString("excDate"), "yyyy-MM-dd");
    }
}
