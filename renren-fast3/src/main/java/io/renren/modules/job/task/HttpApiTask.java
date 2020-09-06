package io.renren.modules.job.task;

import com.alibaba.fastjson.JSON;
import io.renren.common.apitest.*;
import io.renren.modules.job.entity.ScheduleJobEntity;
import io.renren.modules.testcase.entity.TestCaseEntity;
import io.renren.modules.testcase.service.TestCaseService;
import io.renren.modules.testresult.entity.TestResultEntity;
import io.renren.modules.testresult.service.TestResultService;
import io.renren.modules.testvariable.entity.TestVariableEntity;
import io.renren.modules.testvariable.service.TestVariableService;
import junit.framework.TestCase;
import junit.framework.TestResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component("HttpApiTask")
public class HttpApiTask implements ITask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TestVariableService testVariableService;
    @Autowired
    private TestCaseService testCaseService;
    @Autowired
    private TestResultService testResultService;
    /**
     * job: 需要执行任务的job
     * 根据job里的paramIds 和 caseIds 得到对应的参数
     * */
    @Override
    public void run(ScheduleJobEntity job){
        Date now = new Date(); // 创建一个Date对象，获取当前时间
        // 指定格式化格式
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String jobStartTime = f.format(now); // 将当前时间袼式化为指定的格式

        if(StringUtils.isNotEmpty(job.getCaseIds())){ // 测试用例
            // 得到 测试参数
            String paramIds = job.getParamIds();
//            String[] paramArray = paramIds.split(",");
//            List<String> paramList = Arrays.asList(paramArray);
            String paramValue = testVariableService.getById(paramIds).getParamValue();

            // 得到 测试用例
            String caseIds = job.getCaseIds();
            String[] caseArray = caseIds.split(",");
            List<String> caseList = Arrays.asList(caseArray);
            // 判断是 参数数组、参数MAP、null
            if (JSON.isValidArray(paramValue)){
                // 多组参数
                List<Map> paramsList = JSON.parseArray(paramValue, Map.class);
                for(Map params : paramsList){
                    runTestCase(caseList, params,jobStartTime);
                }
            }else if(JSON.isValidObject(paramValue)){
                Map params = JSON.parseObject(paramValue, Map.class);
                runTestCase(caseList,params,jobStartTime);
            }else{
                runTestCase(caseList,null,jobStartTime);
            }


        }
        logger.debug("HttpApiTask 定时任务正在执行，参数为：{}", job);
    }
    public void runTestCase(List caseList, Map params,String jobStartTime ){
        List<TestCaseEntity> testCaseEntityList = testCaseService.listByIds(caseList);
        // 把参数添加进全局的变量里
        ParamUtils.addFromMap(params);
        List<TestResultEntity> testResults = new ArrayList<>(); // 存储本次全部的测试结果
        TestResultEntity testResult = new TestResultEntity();
        for (TestCaseEntity testCase : testCaseEntityList){
            if (testCase.getRun() == 1){ // 可以测试
                // 更换testCase 需要替换的 ${}
                replaceVariables(testCase);
                System.out.println(testCase);
                // 发送请求
                String result = ""; // 通常是 json
                if ("get".equals(testCase.getType())) {
                    result = HttpClientUtils.doGet(testCase.getUrl(),
                            StringToMapUtils.covertStringToMap2(testCase.getHeaders()),
                            StringToMapUtils.covertStringToMap1(testCase.getParams())
                    );
                } else if ("post".equals(testCase.getType())) {
                    result = HttpClientUtils.doPost(testCase.getUrl(),
                            StringToMapUtils.covertStringToMap2(testCase.getHeaders()),
                            StringToMapUtils.covertStringToMap1(testCase.getParams())
                            );
                } else if ("postjson".equals(testCase.getType())) {
                    result = HttpClientUtils.doPostJson(testCase.getUrl(),
                            StringToMapUtils.covertStringToMap2(testCase.getHeaders()),
                            testCase.getParams()
                            );
                }
                // 验证检查点
                String testReulst_str="";
                if(StringUtils.isNotEmpty(testCase.getCheckpoint())) {

                    JsonCheckResult jsonResult = CheckPointUtils.check(result, testCase.getCheckpoint());
                    if(jsonResult.isResult()) {
                        // 成功
                        testResult.setStatus(1);
                    }else {
                        // 失败
                        testResult.setStatus(0);
                    }
                    testReulst_str = CheckPointUtils.check(result, testCase.getCheckpoint()).getMsg();

                }else {
                    // 没有设置检查点
                    testReulst_str ="没有设置检查点";
                    testResult.setStatus(2);
                }
                // 从返回值里取出需要
                ParamUtils.addFromJson(result, testCase.getCorrelation());

                testResult.setJobStartTime(jobStartTime); // 本次测试时间作为一次识别的id
                testResult.setCaseId(testCase.getId());
                testResult.setCaseName(testCase.getCaseName());
                testResult.setCreateTime(new Date());
                testResult.setTestResult(testReulst_str); // 测试结果 是否正确
                testResult.setTestCase(JSON.toJSONString(testCase)); // 测试结果转成json
                testResult.setTestParams(JSON.toJSONString(params));
                testResults.add(testResult);
            }
        }
        testResultService.saveBatch(testResults); // 批量保存到数据库
        ParamUtils.clear();


    }
    private void replaceVariables(TestCaseEntity testCase){
        // Headers、url、params、checkPoint(检查点)
        testCase.setHeaders(ParamUtils.replace(testCase.getHeaders()));
        testCase.setUrl(ParamUtils.replace(testCase.getUrl()));
        testCase.setParams(ParamUtils.replace(testCase.getParams()));
        testCase.setCheckpoint(ParamUtils.replace(testCase.getCheckpoint()));

    }

}
