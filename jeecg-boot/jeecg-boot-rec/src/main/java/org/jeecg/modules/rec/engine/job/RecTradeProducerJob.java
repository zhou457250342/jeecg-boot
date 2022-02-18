package org.jeecg.modules.rec.engine.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.rec.engine.job.timeLine.TimeLineJob;
import org.quartz.DisallowConcurrentExecution;

/**
 * 交易追踪对账
 *
 * @Author: zhou x
 * @Date: 2022/2/16 11:14
 */
@Slf4j
@DisallowConcurrentExecution
public class RecTradeProducerJob extends TimeLineJob<RecTradeProducerWork> {
}
