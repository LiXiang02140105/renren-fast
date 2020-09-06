package io.renren.modules.testvariable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.testvariable.entity.TestVariableEntity;

import java.util.Map;

/**
 * 参数
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-21 16:43:25
 */
public interface TestVariableService extends IService<TestVariableEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

