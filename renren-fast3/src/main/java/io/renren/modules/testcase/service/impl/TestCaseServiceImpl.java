package io.renren.modules.testcase.service.impl;

import com.alibaba.fastjson.JSON;
import io.renren.common.apitest.CheckPointUtils;
import io.renren.common.apitest.HttpClientUtils;
import io.renren.common.apitest.JsonCheckResult;
import io.renren.common.apitest.StringToMapUtils;
import io.renren.common.utils.R;
import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.testcase.dao.TestCaseDao;
import io.renren.modules.testcase.entity.TestCaseEntity;
import io.renren.modules.testcase.service.TestCaseService;


@Service("testCaseService")
public class TestCaseServiceImpl extends ServiceImpl<TestCaseDao, TestCaseEntity> implements TestCaseService {
    @Autowired
    TestCaseService testCaseService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TestCaseEntity> page = this.page(
                new Query<TestCaseEntity>().getPage(params),
                new QueryWrapper<TestCaseEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public  R testApiByIds(List caseIds) {
        // 得到需要进行 Apitest() 的测试用例信息 testCaseEntity
        List<TestCaseEntity> testCaseEntities = testCaseService.listByIds(caseIds);
        String result = "";
        // 从数据库里获取的 params： & = ; headers：; =
        for (TestCaseEntity testCaseEntity : testCaseEntities) {
            result = HttpClientUtils.doGet(testCaseEntity.getUrl(),
                    StringToMapUtils.covertStringToMap2(testCaseEntity.getHeaders()),
                    StringToMapUtils.covertStringToMap1(testCaseEntity.getParams())
            );
            if ("get".equals(testCaseEntity.getType())) {
            } else if ("post".equals(testCaseEntity.getType())) {
                result = HttpClientUtils.doPost(testCaseEntity.getUrl(),
                        StringToMapUtils.covertStringToMap2(testCaseEntity.getHeaders()),
                        StringToMapUtils.covertStringToMap1(testCaseEntity.getParams())
                );
            }
            // 验证返回结果
            String checkResult = "没有设置检查点";
            if (!JSON.isValid(result)){
                checkResult = "校验失败, 接口返回值不为json";
            }else if(StringUtils.isNotBlank(testCaseEntity.getCheckpoint())) {
                JsonCheckResult jsoncheckResult = CheckPointUtils.check(result, testCaseEntity.getCheckpoint());
                checkResult = jsoncheckResult.getMsg();
            }
            testCaseEntity.setTestResult(checkResult);
        }
        //修改
        testCaseService.saveOrUpdateBatch(testCaseEntities);
        return R.ok();
    }


    public static void main(String[] args) {
        String result = "{\"msg\":\"登录成功\",\"uid\":\"9CC972DFA2D4481F89841A46FD1B3E7B\",\"code\":\"1\"}";
        System.out.println(JSON.isValid(result));
    }

}
