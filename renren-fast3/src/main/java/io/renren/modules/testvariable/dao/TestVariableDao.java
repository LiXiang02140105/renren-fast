package io.renren.modules.testvariable.dao;

import io.renren.modules.testvariable.entity.TestVariableEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 参数
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-21 16:43:25
 */
@Mapper
public interface TestVariableDao extends BaseMapper<TestVariableEntity> {
	
}
