package io.renren.modules.testcase.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.testcase.entity.TestCaseEntity;
import io.renren.modules.testcase.service.TestCaseService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 测试用例表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-17 17:42:30
 */
@RestController
@RequestMapping("testcase/testcase")
public class TestCaseController {
    @Autowired
    private TestCaseService testCaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("testcase:testcase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = testCaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("testcase:testcase:info")
    public R info(@PathVariable("id") String id){
		TestCaseEntity testCase = testCaseService.getById(id);

        return R.ok().put("testCase", testCase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("testcase:testcase:save")
    public R save(@RequestBody TestCaseEntity testCase){
		testCaseService.save(testCase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("testcase:testcase:update")
    public R update(@RequestBody TestCaseEntity testCase){
		testCaseService.updateById(testCase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("testcase:testcase:delete")
    public R delete(@RequestBody String[] ids){
		testCaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
    /**
     * 执行 testcase页面的 接口测试, 结果将直接写入TestCase的testResult（最近一次测试结果）中
     */
    @RequestMapping("/testapi")
    @RequiresPermissions("testcase:testcase:testapi")
    public R testApi(@RequestBody String[] ids){
        testCaseService.testApiByIds(Arrays.asList(ids));

        return R.ok();
    }

}
