package org.jeecg.modules.rec.engine.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.annotation.Obsolete;
import org.jeecg.modules.rec.engine.job.timeLine.TimeLineJob;
import org.quartz.DisallowConcurrentExecution;


/**
 * 系统对账-天
 *
 * @Author: zhou x
 * @Date: 2022/2/15 10:54
 */
@Slf4j
@DisallowConcurrentExecution
@Obsolete
public class RecDayTimeJob extends TimeLineJob<RecDayTimeJobWork> {

}
