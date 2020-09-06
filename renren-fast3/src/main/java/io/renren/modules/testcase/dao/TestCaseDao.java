package io.renren.modules.testcase.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import io.renren.modules.testcase.entity.TestCaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.testcase.service.impl.TestCaseServiceImpl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 测试用例表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-17 17:42:30
 */
@Mapper
public interface TestCaseDao extends BaseMapper<TestCaseEntity> {

    @Update("SELECT * FROM tb_test_case ${ew.customSqlSegment} for ")
    List<TestCaseEntity> selectByMyWrapper(@Param(Constants.WRAPPER) Wrapper<TestCaseEntity> userWrapper);

}
