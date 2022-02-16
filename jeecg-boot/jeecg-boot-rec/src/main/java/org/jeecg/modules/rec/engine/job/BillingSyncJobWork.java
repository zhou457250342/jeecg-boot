package org.jeecg.modules.rec.engine.job;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.SpringContextHolder;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.rec.engine.model.RecException;
import org.jeecg.modules.rec.engine.model.TradeData;
import org.jeecg.modules.rec.engine.resource.ResourceLoader;
import org.jeecg.modules.rec.entity.ModRec;
import org.jeecg.modules.rec.entity.ModRecModel;
import org.jeecg.modules.rec.service.IModRecModelService;
import org.jeecg.modules.rec.service.IModRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 账单同步
 *
 * @Author: zhou x
 * @Date: 2022/2/10 15:55
 */
@Component
public class BillingSyncJobWork extends TimeLineJobWork {

    @Override
    protected void execute(Date syncDate, ModRec modRec, String name) {
        try {
            MybatisPlusSaasConfig.tableModRecModel.set("mod_rec_" + name);
            String beanName = name + "ResourceLoader";
            ResourceLoader resourceLoader = SpringContextHolder.getBean(beanName);
            if (resourceLoader == null) throw new RecException("ResourceLoader is null");
            List<TradeData> tradeDataList = resourceLoader.getList(syncDate);
            List<ModRecModel> models = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(tradeDataList)) {
                tradeDataList.forEach(op -> {
                    ModRecModel model = new ModRecModel();
                    model.setTradeNo(op.getTradeNo());
                    model.setOrderNo(op.getOrderNo());
                    model.setBussNo(op.getOperationNo());
                    model.setCreateTime(op.getCreateTime());
                    model.setUniqueNo(op.getUniqueNo());
                    model.setNote(op.getNote());
                    model.setAmount(op.getAmount());
                    models.add(model);
                });
            }
            modRecModelService.remove(Wrappers.<ModRecModel>lambdaQuery().apply("create_time BETWEEN {0} AND {1}",
                    DateUtils.formatDate(syncDate, "yyyy-MM-dd 00:00:00"), DateUtils.formatDate(syncDate, "yyyy-MM-dd 23:59:59")));
            if (CollectionUtils.isNotEmpty(models)) {
                int flag = modRecModelService.insertBatchSomeColumn(models);
                if (flag != models.size()) throw new RecException("数据插入异常");
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            MybatisPlusSaasConfig.tableModRecModel.remove();
        }
    }

    @Override
    public Date startDate() {
        return recConfig.recBillDownDate();
    }

    @Override
    public String thresholdName() {
        return "billingDown";
    }
}
