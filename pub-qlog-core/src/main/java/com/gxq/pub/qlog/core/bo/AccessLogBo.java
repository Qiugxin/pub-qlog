package com.gxq.pub.qlog.core.bo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/***
 * access日志BO
 * @author guixinQiu
 * @since 2021/1/13 20:44
 */
@Data
@Builder
public class AccessLogBo implements Serializable {

    /**
     * 请求事务全局ID
     */
    private String transactionId;

    /**
     *  对端IP或远程IP
     */
    private String remoteIp;

    /**
     * 本机IP
     */
    private String localIp;

    /**
     * 模块名称（当前系统名）
     */
    private String module;

    /**
     * 接口名称/方法名（当前系统或者下游系统接口名称）,如：OnlineUnifiedTradeApi_publicPay
     */
    private String serviceId;

    /**
     * 开始时间，格式：yyyy-MM-dd hh:MM:ss SSS
     */
    private String startTime;

    /**
     * 结束时间，格式：yyyy-MM-dd hh:MM:ss SSS
     */
    private String endTime;

    /**
     * 接口总耗时
     */
    private String timeDiff;

    /**
     * 接口结果
     */
    private String resultCode;

    /**
     * HTTP接口响应规范参数
     */
    private String code;

    /**
     * 返回错误码
     */
    private String errorCode;

    /**
     * 对内错误描述
     */
    private String errorDesc;

    /**
     * 当前请求，所属服务类型 C：客户端，S：服务端
     */
    private String endPoint;

    /**
     * 当前节点ID,16位小写字符串
     */
    private String spanId;

    /**
     * 当前节点父节点ID,16位小写字符串，如果为空表示根节点
     */
    private String parentId;

    /**
     * 业务等其他属性
     */
    private Map<String, Object> properties;
}