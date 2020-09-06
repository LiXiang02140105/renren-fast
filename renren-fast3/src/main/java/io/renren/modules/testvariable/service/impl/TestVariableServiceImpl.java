package io.renren.modules.testvariable.service.impl;

import io.renren.modules.sys.entity.SysConfigEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.testvariable.dao.TestVariableDao;
import io.renren.modules.testvariable.entity.TestVariableEntity;
import io.renren.modules.testvariable.service.TestVariableService;


@Service("testVariableService")
public class TestVariableServiceImpl extends ServiceImpl<TestVariableDao, TestVariableEntity> implements TestVariableService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String paramKey = (String)params.get("paramKey");
        QueryWrapper<TestVariableEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.like(StringUtils.isNotBlank(paramKey),"param_key", paramKey)
                .eq("status",1);//状态   0：隐藏   1：显示

        IPage<TestVariableEntity> page = this.page(
                new Query<TestVariableEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}