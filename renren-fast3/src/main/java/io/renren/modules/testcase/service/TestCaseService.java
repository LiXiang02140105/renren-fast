package io.renren.modules.testcase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.testcase.entity.TestCaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 测试用例表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-17 17:42:30
 */
public interface TestCaseService extends IService<TestCaseEntity> {

    PageUtils queryPage(Map<String, Object> params);
    R testApiByIds(List ids);
}

