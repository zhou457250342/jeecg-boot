package org.jeecg.modules.rec.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.rec.entity.ModRecTest;
import org.jeecg.modules.rec.service.IModRecTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @Description: mod_rec_test
* @Author: jeecg-boot
* @Date:   2022-02-17
* @Version: V1.0
*/
@Api(tags="mod_rec_test")
@RestController
@RequestMapping("/aaaa/modRecTest")
@Slf4j
public class ModRecTestController extends JeecgController<ModRecTest, IModRecTestService> {
   @Autowired
   private IModRecTestService modRecTestService;

   /**
    * 分页列表查询
    *
    * @param modRecTest
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "mod_rec_test-分页列表查询")
   @ApiOperation(value="mod_rec_test-分页列表查询", notes="mod_rec_test-分页列表查询")
   @GetMapping(value = "/list")
   public Result<?> queryPageList(ModRecTest modRecTest,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<ModRecTest> queryWrapper = QueryGenerator.initQueryWrapper(modRecTest, req.getParameterMap());
       Page<ModRecTest> page = new Page<ModRecTest>(pageNo, pageSize);
       IPage<ModRecTest> pageList = modRecTestService.page(page, queryWrapper);
       return Result.OK(pageList);
   }

   /**
    *   添加
    *
    * @param modRecTest
    * @return
    */
   @AutoLog(value = "mod_rec_test-添加")
   @ApiOperation(value="mod_rec_test-添加", notes="mod_rec_test-添加")
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody ModRecTest modRecTest) {
       modRecTestService.save(modRecTest);
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    *
    * @param modRecTest
    * @return
    */
   @AutoLog(value = "mod_rec_test-编辑")
   @ApiOperation(value="mod_rec_test-编辑", notes="mod_rec_test-编辑")
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody ModRecTest modRecTest) {
       modRecTestService.updateById(modRecTest);
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "mod_rec_test-通过id删除")
   @ApiOperation(value="mod_rec_test-通过id删除", notes="mod_rec_test-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       modRecTestService.removeById(id);
       return Result.OK("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "mod_rec_test-批量删除")
   @ApiOperation(value="mod_rec_test-批量删除", notes="mod_rec_test-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.modRecTestService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @AutoLog(value = "mod_rec_test-通过id查询")
   @ApiOperation(value="mod_rec_test-通过id查询", notes="mod_rec_test-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       ModRecTest modRecTest = modRecTestService.getById(id);
       if(modRecTest==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(modRecTest);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param modRecTest
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, ModRecTest modRecTest) {
       return super.exportXls(request, modRecTest, ModRecTest.class, "mod_rec_test");
   }

   /**
     * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
   @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
   public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
       return super.importExcel(request, response, ModRecTest.class);
   }

}
