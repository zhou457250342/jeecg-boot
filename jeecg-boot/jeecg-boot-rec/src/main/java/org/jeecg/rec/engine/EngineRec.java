package org.jeecg.rec.engine;

import org.jeecg.rec.engine.checker.CheckResult;
import org.jeecg.rec.engine.checker.FullChecker;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @Author: zhou x
 * @Date: 2022/1/22 18:57
 */
public class EngineRec {
    @Autowired
    private FullChecker fullChecker;

    /**
     * zlHis->支付宝
     *
     * @return
     */
    public static List<CheckResult> zlHis2alipay(Date date) {
        return null;
    }
}
