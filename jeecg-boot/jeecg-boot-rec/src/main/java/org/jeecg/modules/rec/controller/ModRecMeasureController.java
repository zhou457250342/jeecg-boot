package org.jeecg.modules.rec.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.rec.entity.ModRecMeasure;
import org.jeecg.modules.rec.service.IModRecMeasureService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: mod_rec_measure
 * @Author: jeecg-boot
 * @Date:   2022-02-18
 * @Version: V1.0
 */
@Api(tags="mod_rec_measure")
@RestController
@RequestMapping("/rec/modRecMeasure")
@Slf4j
public class ModRecMeasureController extends JeecgController<ModRecMeasure, IModRecMeasureService> {
	@Autowired
	private IModRecMeasureService modRecMeasureService;

	/**
	 * 分页列表查询
	 *
	 * @param modRecMeasure
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "mod_rec_measure-分页列表查询")
	@ApiOperation(value="mod_rec_measure-分页列表查询", notes="mod_rec_measure-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ModRecMeasure modRecMeasure,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ModRecMeasure> queryWrapper = QueryGenerator.initQueryWrapper(modRecMeasure, req.getParameterMap());
		Page<ModRecMeasure> page = new Page<ModRecMeasure>(pageNo, pageSize);
		IPage<ModRecMeasure> pageList = modRecMeasureService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param modRecMeasure
	 * @return
	 */
	@AutoLog(value = "mod_rec_measure-添加")
	@ApiOperation(value="mod_rec_measure-添加", notes="mod_rec_measure-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ModRecMeasure modRecMeasure) {
		modRecMeasureService.save(modRecMeasure);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param modRecMeasure
	 * @return
	 */
	@AutoLog(value = "mod_rec_measure-编辑")
	@ApiOperation(value="mod_rec_measure-编辑", notes="mod_rec_measure-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ModRecMeasure modRecMeasure) {
		modRecMeasureService.updateById(modRecMeasure);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "mod_rec_measure-通过id删除")
	@ApiOperation(value="mod_rec_measure-通过id删除", notes="mod_rec_measure-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		modRecMeasureService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "mod_rec_measure-批量删除")
	@ApiOperation(value="mod_rec_measure-批量删除", notes="mod_rec_measure-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.modRecMeasureService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "mod_rec_measure-通过id查询")
	@ApiOperation(value="mod_rec_measure-通过id查询", notes="mod_rec_measure-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ModRecMeasure modRecMeasure = modRecMeasureService.getById(id);
		if(modRecMeasure==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(modRecMeasure);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param modRecMeasure
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ModRecMeasure modRecMeasure) {
        return super.exportXls(request, modRecMeasure, ModRecMeasure.class, "mod_rec_measure");
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
        return super.importExcel(request, response, ModRecMeasure.class);
    }

}
