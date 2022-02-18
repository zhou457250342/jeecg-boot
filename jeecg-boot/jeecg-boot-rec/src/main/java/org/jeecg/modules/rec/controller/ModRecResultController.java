package org.jeecg.modules.rec.controller;

import javax.servlet.http.HttpServletRequest;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.rec.entity.view.ModRecResultView;
import org.jeecg.modules.rec.service.IModRecResultService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: mod_rec_result
 * @Author: jeecg-boot
 * @Date: 2022-02-17
 * @Version: V1.0
 */
@Api(tags = "mod_rec_result")
@RestController
@RequestMapping("/rec/modRecResult")
@Slf4j
public class ModRecResultController {
    @Autowired
    private IModRecResultService modRecResultService;

    /**
     * 分页列表查询
     *
     * @param modRecResult
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    final String fn_name = "zlhis_alipay"; //todo 特定对账通道,后续调整为菜单参数

    @AutoLog(value = "mod_rec_result-分页列表查询")
    @ApiOperation(value = "mod_rec_result-分页列表查询", notes = "mod_rec_result-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ModRecResultView modRecResult,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        Page page = new Page(pageNo, pageSize);
        IPage<ModRecResultView> pageList = modRecResultService.page(page, modRecResult, fn_name);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param modRecResult
     * @return
     */
    @AutoLog(value = "mod_rec_result-添加")
    @ApiOperation(value = "mod_rec_result-添加", notes = "mod_rec_result-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ModRecResultView modRecResult) {
        //modRecResultService.save(modRecResult);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param modRecResult
     * @return
     */
    @AutoLog(value = "mod_rec_result-编辑")
    @ApiOperation(value = "mod_rec_result-编辑", notes = "mod_rec_result-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ModRecResultView modRecResult) {
        // modRecResultService.updateById(modRecResult);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "mod_rec_result-通过id删除")
    @ApiOperation(value = "mod_rec_result-通过id删除", notes = "mod_rec_result-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        //modRecResultService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "mod_rec_result-批量删除")
    @ApiOperation(value = "mod_rec_result-批量删除", notes = "mod_rec_result-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        //this.modRecResultService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "mod_rec_result-通过id查询")
    @ApiOperation(value = "mod_rec_result-通过id查询", notes = "mod_rec_result-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
//        ModRecResult modRecResult = modRecResultService.getById(id);
//        if (modRecResult == null) {
//            return Result.error("未找到对应数据");
//        }
//        return Result.OK(modRecResult);
        return null;
    }
}
