package io.renren.modules.testresult.service.impl;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.testresult.dao.TestResultDao;
import io.renren.modules.testresult.entity.TestResultEntity;
import io.renren.modules.testresult.service.TestResultService;


@Service("testResultService")
public class TestResultServiceImpl extends ServiceImpl<TestResultDao, TestResultEntity> implements TestResultService {
    @Autowired
    private TestResultService testResultService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<TestResultEntity> queryWrapper = new QueryWrapper<TestResultEntity>();
        //queryWrapper.orderByDesc("create_time");
        queryWrapper.lambda().orderByDesc(TestResultEntity::getCreateTime);
                //(TestResultEntity::getCreateTime);
        IPage<TestResultEntity> page = this.page(
                new Query<TestResultEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 根据 jobStartTime 区分每一个任务, 默认是全部任务
     * @param jobStartTime
     * @return
     */
    @Override
    public Map<String, Object> getCount(String jobStartTime){
        List<Map<String, Object>> resultList = null;
        if("".equals(jobStartTime)){
            resultList = testResultService.listMaps();
        }else{
            QueryWrapper<TestResultEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("job_start_time",jobStartTime);
            resultList = testResultService.listMaps(queryWrapper);
        }
        // 得到result
        //{test_result=没有设置检查点, create_time=2020-08-22 18:28:57.0, case_name=接口测试, case_id=1240813164455530497, test_params={"method":"loginMobile","loginname":"abc","loginpass":"abc"}, id=1297118554289729538, job_start_time=20200822_182857, test_case={"caseName":"接口测试","checkpoint":"code=1","correlation":"test","headers":"test","id":"1240813164455530497","orderNum":1,"params":"","run":1,"testResult":"检查点检查成功","type":"get","url":"http://118.24.13.38:8080/goods/UserServlet?method=${method}&loginname=${loginname}&loginpass=${loginname}","varFromResult":"code=code；"}, status=0}
        int success = 0; // 1
        int fail = 0; // 0
        int skip = 0;  // 2
        for (Map result : resultList){
            System.out.println(result);
            if(1 == (int) result.get("status")){
                success += 1;
            }else if(0 == (int) result.get("status")){
                fail += 1;
            }else{
                skip += 1;
            }
        }
        Map<String, Object> count = new HashedMap();
        count.put("success",success);
        count.put("fail",fail);
        count.put("skip",skip);
        return count;
    }



}