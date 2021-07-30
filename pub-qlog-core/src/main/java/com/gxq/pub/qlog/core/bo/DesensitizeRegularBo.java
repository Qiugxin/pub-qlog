package com.gxq.pub.qlog.core.bo;

import lombok.Data;

/**
 *
 *
 * Description:脱敏规则信息类
 *
 * @author: guixinQiu
 * @since 2021/1/13 20:44
 *
 */
@Data
public class DesensitizeRegularBo {
    /**
     * 脱敏字段名
     */
    private String key;

    /**
     * 目标待脱敏数据的匹配正则表达式
     */
    private String regex;

    /**
     * 替换内容
     */
    private String repl;

}
