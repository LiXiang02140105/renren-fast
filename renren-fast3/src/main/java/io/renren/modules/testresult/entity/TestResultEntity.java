package io.renren.modules.testresult.entity;

import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 测试结果
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-21 18:22:07
 */
@Data
@TableName("tb_test_result")
public class TestResultEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**
	 * 案例id
	 */
	private String caseId;
	/**
	 * 案例名称
	 */
	private String caseName;
	/**
	 * 测试案例
	 */
	private String testCase;
	/**
	 * 测试参数
	 */
	private String testParams;
	/**
	 * 测试结果
	 */
	private String testResult;
	/**
	 * 1 成功 0 失败 2 未检查
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 测试任务创建时间
	 */
	private String jobStartTime;

}
