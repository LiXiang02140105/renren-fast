package io.renren.modules.testcase.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 测试用例表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-20 15:58:57
 */
@Data
@TableName("tb_test_case")
public class TestCaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**
	 * 顺序
	 */
	private Integer orderNum;
	/**
	 * 是否开启
	 */
	private Integer run;
	/**
	 * 案例名称
	 */
	private String caseName;
	/**
	 * 测试地址
	 */
	private String url;
	/**
	 * 参数
	 */
	private String params;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 头部信息
	 */
	private String headers;
	/**
	 * 检查点
	 */
	private String checkpoint;
	/**
	 * 关联
	 */
	private String correlation;
	/**
	 * 上次测试结果
	 */
	private String testResult;
	/**
	 * 需要从结果里获取的数据
	 */
	private String varFromResult;

}
