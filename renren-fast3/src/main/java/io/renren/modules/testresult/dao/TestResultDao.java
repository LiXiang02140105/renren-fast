package io.renren.modules.testresult.dao;

import io.renren.modules.testresult.entity.TestResultEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 测试结果
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-21 18:22:07
 */
@Mapper
public interface TestResultDao extends BaseMapper<TestResultEntity> {
	
}
