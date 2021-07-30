package com.gxq.pub.qlog.core.bo;

import lombok.Builder;
import lombok.Data;

/***
 * 事务BO
 * @author guixinQiu
 * @since 2021/1/13 20:44
 */
@Data
@Builder
public class TransactionBo {

    /**
     * 请求事务全局ID
     */
    private String transactionId;

    /**
     * 请求事务协议
     */
    private String protocol;

    /**
     * 请求事务当前路径/接口方法名称
     */
    private String serviceId;

    /**
     * 节点id
     */
    private String spanId;

    /**
     * 父节点id
     */
    private String parentId;
}
