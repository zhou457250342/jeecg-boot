package org.jeecg.modules.rec.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.rec.entity.ModRecComparison;
import org.jeecg.modules.rec.entity.ModRecModel;
import org.jeecg.modules.rec.entity.view.ModRecResultView;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: mod_rec_result
 * @Author: jeecg-boot
 * @Date: 2022-02-17
 * @Version: V1.0
 */
public interface IModRecResultService {
    /**
     * 查询对账结果
     *
     * @param page
     * @param modRecResult
     * @param t_name
     * @return
     */
    IPage<ModRecResultView> page(Page page, ModRecResultView modRecResult, String t_name);

    /**
     * 获取对账结果
     *
     * @param id
     * @return
     */
    ModRecComparison getById(String id, String t_name);

    /**
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<ModRecComparison> searchRecResultPage(IPage page, Wrapper<ModRecComparison> queryWrapper, String t_name);

    /**
     * 查找对账记录
     *
     * @param status
     * @param limit
     * @param t_name
     * @return
     */
    List<ModRecComparison> searchRecResult(int status, int limit, String t_name);

    /**
     * 保存对账记录
     *
     * @param modRecComparisons
     * @param t_name
     */
    void saveRecResult(List<ModRecComparison> modRecComparisons, String t_name);

    /**
     * 保存对账记录
     *
     * @param modRecComparison
     * @param t_name
     */
    void saveRecResult(ModRecComparison modRecComparison, String t_name);

    /**
     * 删除对账记录
     *
     * @param id
     * @param t_name
     */
    void removeById(Serializable id, String t_name);

    /**
     * 查询账单
     *
     * @param name
     * @param date
     * @param tradeNo
     * @return
     */
    List<ModRecModel> searchBillList(Date date, String tradeNo, String name);
}
