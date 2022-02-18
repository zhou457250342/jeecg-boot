package org.jeecg.modules.rec.engine.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.rec.engine.job.timeLine.TimeLineJob;
import org.quartz.DisallowConcurrentExecution;

/**
 * 账单同步
 *
 * @Author: zhou x
 * @Date: 2022/2/9 15:34
 */
@DisallowConcurrentExecution
@Slf4j
public class BillingSyncJob extends TimeLineJob<BillingSyncJobWork> {

}
