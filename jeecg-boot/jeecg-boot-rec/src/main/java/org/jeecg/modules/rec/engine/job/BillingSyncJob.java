package org.jeecg.modules.rec.engine.job;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.utils.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.SpringContextHolder;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.rec.engine.config.RecConfig;
import org.jeecg.modules.rec.engine.model.RecException;
import org.jeecg.modules.rec.engine.model.TradeData;
import org.jeecg.modules.rec.engine.resource.ResourceLoader;
import org.jeecg.modules.rec.entity.ModRec;
import org.jeecg.modules.rec.entity.ModRecModel;
import org.jeecg.modules.rec.mapper.ModRecModelMapper;
import org.jeecg.modules.rec.service.IModRecModelService;
import org.jeecg.modules.rec.service.IModRecService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 账单同步任务
 *
 * @Author: zhou x
 * @Date: 2022/2/9 15:34
 */
@DisallowConcurrentExecution
@Slf4j
public class BillingSyncJob implements Job {

    private String parameter;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Autowired
    private RecConfig recConfig;
    @Autowired
    private IModRecService modRecService;
    @Autowired
    private BillingSyncJobWork billingSyncJobWork;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            MybatisPlusSaasConfig.tableModRecModel.set("mod_rec_" + parameter);
            Date recBillDownDate = recConfig.recBillDownDate();
            if (recBillDownDate == null) throw new JobExecutionException("账单开始下载时间未配置");
            recBillDownDate = DateUtils.addDays(recBillDownDate, -1);
            ModRec modRec = modRecService.getOne(Wrappers.<ModRec>lambdaQuery().eq(ModRec::getName, parameter));
            if (modRec == null) {
                modRec = new ModRec();
                modRec.setName(parameter);
                modRec.setContent(new JSONObject());
            }
            String recDate = modRec.getContent().getString("billDownDate");
            if (StringUtils.isNotEmpty(recDate)) {
                Date date = DateUtils.parseDateDef(recDate, "yyyy-MM-dd");
                if (date.after(recBillDownDate)) recBillDownDate = date;
            }
            while (true) {
                recBillDownDate = DateUtils.addDays(recBillDownDate, 1);
                if (DateUtils.isSameDay(new Date(), recBillDownDate)) break;
                billingSyncJobWork.syncData(recBillDownDate, modRec, parameter);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            MybatisPlusSaasConfig.tableModRecModel.remove();
        }
    }
}
