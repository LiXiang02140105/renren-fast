package io.renren.modules.testvariable.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.testvariable.entity.TestVariableEntity;
import io.renren.modules.testvariable.service.TestVariableService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 参数
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-21 16:43:25
 */
@RestController
@RequestMapping("testvariable/testvariable")
public class TestVariableController {
    @Autowired
    private TestVariableService testVariableService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("testvariable:testvariable:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = testVariableService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("testvariable:testvariable:info")
    public R info(@PathVariable("id") Long id){
		TestVariableEntity testVariable = testVariableService.getById(id);

        return R.ok().put("testVariable", testVariable);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("testvariable:testvariable:save")
    public R save(@RequestBody TestVariableEntity testVariable){
		testVariableService.save(testVariable);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("testvariable:testvariable:update")
    public R update(@RequestBody TestVariableEntity testVariable){
		testVariableService.updateById(testVariable);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("testvariable:testvariable:delete")
    public R delete(@RequestBody Long[] ids){
		testVariableService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
