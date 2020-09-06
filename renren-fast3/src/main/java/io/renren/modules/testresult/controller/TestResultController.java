package io.renren.modules.testresult.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.testresult.entity.TestResultEntity;
import io.renren.modules.testresult.service.TestResultService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 测试结果
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-21 18:22:07
 */
@RestController
@RequestMapping("testresult/testresult")
public class TestResultController {
    @Autowired
    private TestResultService testResultService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("testresult:testresult:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = testResultService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("testresult:testresult:info")
    public R info(@PathVariable("id") String id){
		TestResultEntity testResult = testResultService.getById(id);
        System.out.println("testResult:"+ id);
        return R.ok().put("testResult", testResult);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("testresult:testresult:save")
    public R save(@RequestBody TestResultEntity testResult){
		testResultService.save(testResult);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("testresult:testresult:update")
    public R update(@RequestBody TestResultEntity testResult){
		testResultService.updateById(testResult);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("testresult:testresult:delete")
    public R delete(@RequestBody String[] ids){
		testResultService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
    /**
     * count
     * R.ok() 封装
     */
    @RequestMapping("/count")
    @RequiresPermissions("testresult:testresult:count")
    public  R count(String jobStartTime){
        Map<String, Object> count = testResultService.getCount(jobStartTime);
        // 得到count
        System.out.println(count);
        return R.ok().put("count",count);
    }

}
