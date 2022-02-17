package org.jeecg.modules.rec.engine.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang.StringUtils;
import org.apache.http.annotation.Obsolete;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.SpringContextHolder;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.rec.engine.model.RecParameter;
import org.jeecg.modules.rec.engine.checker.CheckResult;
import org.jeecg.modules.rec.engine.checker.FullChecker;
import org.jeecg.modules.rec.engine.model.RecException;
import org.jeecg.modules.rec.engine.model.RecExecType;
import org.jeecg.modules.rec.engine.model.TradeData;
import org.jeecg.modules.rec.engine.model.TradeDataRec;
import org.jeecg.modules.rec.entity.ModRec;
import org.jeecg.modules.rec.entity.ModRecComparison;
import org.jeecg.modules.rec.entity.ModRecModel;
import org.jeecg.modules.rec.service.IModRecComparisonService;
import org.jeecg.modules.rec.service.IModRecModelService;
import org.jeecg.modules.rec.service.IModRecService;

import java.io.Serializable;
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
    private FullChecker fullChecker;

    public RecService(String mainName, String sideName) {
        this.mainName = mainName;
        this.sideName = sideName;
        this.modRecService = SpringContextHolder.getBean(IModRecService.class);
        this.modRecComparisonService = SpringContextHolder.getBean(IModRecComparisonService.class);
        this.modRecModelService = SpringContextHolder.getBean(IModRecModelService.class);
        this.fullChecker = SpringContextHolder.getBean(FullChecker.class);
    }

    /**
     * 系统天对账
     *
     * @param date
     */
    @Obsolete
    public void recDay(Date date) {
        if (validateCanRec()) throw new RecException("目前不可进行系统对账");
//        List<TradeData> mainList = repairData(searchBillList(mainName, date, null), true);
//        List<TradeData> sideList = repairData(searchBillList(sideName, date, null), false);
//        FullChecker.check(mainList, sideList);
    }

    /**
     * 系统交易追踪对账
     */
    public CheckResult recTrade(ModRecComparison modRecComparison, RecParameter parameter) {
        List<ModRecModel> mainList = this.searchBillList(mainName, null, modRecComparison.getTradeNo());
        List<ModRecModel> sideList = this.searchBillList(sideName, null, modRecComparison.getTradeNo());
        List<CheckResult> result = fullChecker.check(repairData(mainName, mainList, true), repairData(sideName, sideList, false), parameter);
        return CollectionUtils.isNotEmpty(result) ? result.get(0) : null;
    }

    /**
     * 是否可以进行系统对账
     *
     * @return
     */
    public boolean validateCanRec() {
        Date mainBill = excDate(mainName, RecExecType.billingDown);
        Date sideBill = excDate(sideName, RecExecType.billingDown);
        Date mainPro = excDate(mainName, RecExecType.recTradeProducer);
        Date sidePro = excDate(sideName, RecExecType.recTradeProducer);
        if (mainBill == null || sideBill == null || mainPro == null || sidePro == null) return false;
        Date downDate = DateUtils.addDays(new Date(), -1);
        return DateUtils.isSameDay(mainBill, downDate) && DateUtils.isSameDay(sideBill, downDate) &&
                DateUtils.isSameDay(mainPro, downDate) && DateUtils.isSameDay(sidePro, downDate);
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
    public TradeDataRec repairData(String name, List<ModRecModel> models, boolean isMain) {
        TradeDataRec tradeDataRec = new TradeDataRec(name, isMain);
        if (CollectionUtils.isNotEmpty(models)) {
            models.forEach(op -> {
                TradeData tradeData = new TradeData();
                tradeData.setOperationNo(op.getBussNo());
                tradeData.setAmount(op.getAmount());
                tradeData.setCreateTime(op.getCreateTime());
                tradeData.setNote(op.getNote());
                tradeData.setTradeNo(op.getTradeNo());
                tradeData.setUniqueNo(op.getUniqueNo());
                tradeData.setOrderNo(op.getOrderNo());
                tradeDataRec.add(tradeData);
            });
        }
        return tradeDataRec;
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
     * 对账记录
     *
     * @return
     */
    public void saveRecResult(ModRecComparison modRecComparison) {
        try {
            MybatisPlusSaasConfig.tableModRecComparison.set("mod_rec_res_" + mainName + "_" + sideName);
            modRecComparisonService.saveOrUpdate(modRecComparison);
        } catch (Exception ex) {
            throw new RecException("对账记录保存失败", ex);
        } finally {
            MybatisPlusSaasConfig.tableModRecComparison.remove();
        }
    }

    /**
     * 对账记录移除
     *
     * @param id
     */
    public void removeById(Serializable id) {
        try {
            MybatisPlusSaasConfig.tableModRecComparison.set("mod_rec_res_" + mainName + "_" + sideName);
            modRecComparisonService.removeById(id);
        } catch (Exception ex) {
            throw new RecException("对账记录删除失败", ex);
        } finally {
            MybatisPlusSaasConfig.tableModRecComparison.remove();
        }
    }

    /**
     * 查找对账记录
     *
     * @return
     */
    public List<ModRecComparison> searchRecResult(int status, int limit) {
        try {
            MybatisPlusSaasConfig.tableModRecComparison.set("mod_rec_res_" + mainName + "_" + sideName);
            return modRecComparisonService.list(Wrappers.<ModRecComparison>lambdaQuery()
                    .eq(ModRecComparison::getStatus, status).orderByAsc(ModRecComparison::getCreateTime)
                    .last("limit " + limit));
        } catch (Exception ex) {
            throw ex;
        } finally {
            MybatisPlusSaasConfig.tableModRecComparison.remove();
        }
    }

    /**
     * 获取进度阶段时间
     *
     * @param name
     * @return
     */
    public Date excDate(String name, RecExecType type) {
        ModRec modRec = modRecService.getOne(Wrappers.<ModRec>lambdaQuery()
                .eq(ModRec::getName, name)
                .eq(ModRec::getType, type.toString()));
        if (modRec == null || modRec.getContent() == null) return null;
        return DateUtils.parseDateDef(modRec.getContent().getString("excDate"), "yyyy-MM-dd");
    }
}
