package org.jeecg.modules.rec.engine.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import org.jeecg.modules.rec.service.IModRecResultService;
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
    private IModRecService modRecService;
    private IModRecResultService modRecResultService;
    private FullChecker fullChecker;
    public String mainName;
    public String sideName;
    public String recName;

    public RecService(String recName) {
        this.recName = recName;
        String[] split = recName.split("_");
        this.mainName = split[0];
        this.sideName = split[1];
        this.modRecService = SpringContextHolder.getBean(IModRecService.class);
        this.modRecResultService = SpringContextHolder.getBean(IModRecResultService.class);
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
        List<ModRecModel> mainList = this.searchBillList(null, modRecComparison.getTradeNo(), mainName);
        List<ModRecModel> sideList = this.searchBillList(null, modRecComparison.getTradeNo(), sideName);
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
     * 查询账单
     *
     * @return
     */
    public List<ModRecModel> searchBillList(Date date, String tradeNo, String name) {
        return modRecResultService.searchBillList(date, tradeNo, name);
    }

    /**
     * 保存对账记录
     *
     * @return
     */
    public void saveRecResult(List<ModRecComparison> modRecComparisons) {
        modRecResultService.saveRecResult(modRecComparisons, recName);
    }

    /**
     * 保存对账记录
     *
     * @return
     */
    public void saveRecResult(ModRecComparison modRecComparison) {
        modRecResultService.saveRecResult(modRecComparison, recName);
    }

    /**
     * 对账记录移除
     *
     * @param id
     */
    public void removeById(Serializable id) {
        modRecResultService.removeById(id, recName);
    }

    /**
     * 查找对账记录
     *
     * @return
     */
    public List<ModRecComparison> searchRecResult(int status, int limit) {
        return modRecResultService.searchRecResult(status, limit, recName);
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
}
