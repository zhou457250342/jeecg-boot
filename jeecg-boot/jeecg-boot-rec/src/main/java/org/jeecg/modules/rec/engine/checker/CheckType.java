package org.jeecg.modules.rec.engine.checker;

/**
 * @Author: zhou x
 * @Date: 2022/1/22 16:23
 */
public enum CheckType {
    none,
    /**
     * 非明确结论，可能来源于未出账单
     */
    notClearByUnOut,
    /**
     * 非明确结论，可能来源于未知账单
     */
    notClearByUnKnown
}
