package io.renren.modules.testresult.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.testresult.entity.TestResultEntity;

import java.util.List;
import java.util.Map;

/**
 * 测试结果
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-21 18:22:07
 */
public interface TestResultService extends IService<TestResultEntity> {

    PageUtils queryPage(Map<String, Object> params);
    Map<String, Object> getCount(String jobStartTime);
}

