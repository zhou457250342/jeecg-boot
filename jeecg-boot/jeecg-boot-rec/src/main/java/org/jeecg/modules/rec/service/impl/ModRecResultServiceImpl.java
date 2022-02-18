package org.jeecg.modules.rec.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.rec.engine.checker.CheckResult;
import org.jeecg.modules.rec.engine.model.RecException;
import org.jeecg.modules.rec.engine.service.RecService;
import org.jeecg.modules.rec.entity.ModRecComparison;
import org.jeecg.modules.rec.entity.ModRecModel;
import org.jeecg.modules.rec.entity.view.ModRecResultView;
import org.jeecg.modules.rec.service.IModRecComparisonService;
import org.jeecg.modules.rec.service.IModRecModelService;
import org.jeecg.modules.rec.service.IModRecResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Description: mod_rec_result
 * @Author: jeecg-boot
 * @Date: 2022-02-17
 * @Version: V1.0
 */
@Service
public class ModRecResultServiceImpl implements IModRecResultService {
    @Autowired
    private IModRecModelService modRecModelService;
    @Autowired
    private IModRecComparisonService modRecComparisonService;

    @Override
    public IPage<ModRecResultView> page(Page page, ModRecResultView modRecResult, String t_name) {
        LambdaQueryWrapper<ModRecComparison> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(modRecResult.getTradeNo()))
            wrapper.like(ModRecComparison::getTradeNo, modRecResult.getTradeNo());
        if (modRecResult.getCreateTime() != null)
            wrapper.apply("create_time BETWEEN {0} AND {1}",
                    DateUtils.formatDate(modRecResult.getCreateTime(), "yyyy-MM-dd 00:00:00"), DateUtils.formatDate(modRecResult.getCreateTime(), "yyyy-MM-dd 23:59:59"));
        wrapper.orderByAsc(ModRecComparison::getCreateTime);
        IPage<ModRecComparison> modRecComparisonIPage = this.searchRecResultPage(page, wrapper, t_name);
        //转换
        List<ModRecResultView> viewList = new ArrayList<>();
        modRecComparisonIPage.getRecords().forEach(op -> {
            ModRecResultView view = new ModRecResultView();
            view.setCreateTime(op.getCreateTime());
            view.setTradeNo(op.getTradeNo());
            view.setOrderNo(op.getOrderNo());
            view.setStatus(op.getStatus() == 1 ? "失败" : "成功");
            CheckResult checkResult = op.getResult();
            if (checkResult != null) {
                view.setAmountMain(checkResult.getAmountMain());
                view.setAmountSide(checkResult.getAmountSide());
            }
            viewList.add(view);
        });
        page.setRecords(viewList);
        return page;
    }

    public ModRecComparison getById(String id, String t_name) {
        return this.modRecComparisonSup(() -> this.modRecComparisonService.getById(id), t_name);
    }

    public List<ModRecComparison> searchRecResult(int status, int limit, String t_name) {
        return this.modRecComparisonSup(() -> modRecComparisonService.list(Wrappers.<ModRecComparison>lambdaQuery()
                .eq(ModRecComparison::getStatus, status).orderByAsc(ModRecComparison::getCreateTime)
                .last("limit " + limit)), t_name);
    }

    public IPage<ModRecComparison> searchRecResultPage(IPage page, Wrapper<ModRecComparison> queryWrapper, String t_name) {
        return this.modRecComparisonSup(() -> modRecComparisonService.page(page, queryWrapper), t_name);
    }

    public void removeById(Serializable id, String t_name) {
        this.modRecComparisonCon(op -> MybatisPlusSaasConfig.tableModRecComparison.set("mod_rec_res_" + t_name), "对账记录删除失败", t_name);
    }

    public void saveRecResult(List<ModRecComparison> modRecComparisons, String t_name) {
        this.modRecComparisonCon(op -> modRecComparisonService.insertOrUpdateBatch(modRecComparisons), "对账记录保存失败", t_name);
    }

    public void saveRecResult(ModRecComparison modRecComparison, String t_name) {
        this.modRecComparisonCon(op -> modRecComparisonService.saveOrUpdate(modRecComparison), "对账记录保存失败", t_name);
    }

    public List<ModRecModel> searchBillList(Date date, String tradeNo, String name) {
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
     * 动态表
     *
     * @param consumer
     * @param errMsg
     * @param t_name
     */
    private void modRecComparisonCon(Consumer consumer, String errMsg, String t_name) {
        try {
            MybatisPlusSaasConfig.tableModRecComparison.set("mod_rec_res_" + t_name);
            consumer.accept(null);
        } catch (Exception ex) {
            throw new RecException(errMsg, ex);
        } finally {
            MybatisPlusSaasConfig.tableModRecComparison.remove();
        }
    }

    /**
     * 动态表
     *
     * @param supplier
     * @param t_name
     */
    private <T> T modRecComparisonSup(Supplier<T> supplier, String t_name) {
        try {
            MybatisPlusSaasConfig.tableModRecComparison.set("mod_rec_res_" + t_name);
            return supplier.get();
        } catch (Exception ex) {
            throw new RecException(ex);
        } finally {
            MybatisPlusSaasConfig.tableModRecComparison.remove();
        }
    }
}
