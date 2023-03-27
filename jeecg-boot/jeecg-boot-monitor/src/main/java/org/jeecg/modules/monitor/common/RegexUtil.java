package org.jeecg.modules.monitor.common;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author : nadir
 * @create 2023/3/27 12:23
 */
public class RegexUtil {

    /**
     * 一次匹配
     *
     * @param cts
     * @param reg
     * @param elt
     * @return
     */
    public static String matchAny(String cts, String reg, String elt) {
        Pattern patten = Pattern.compile(reg);
        Matcher matcher = patten.matcher(cts);
        if (matcher.find()) return matcher.group(elt);
        return StringUtils.EMPTY;
    }
}
