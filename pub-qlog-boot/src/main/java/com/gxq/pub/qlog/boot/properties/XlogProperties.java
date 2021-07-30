package com.gxq.pub.qlog.boot.properties;

import com.gxq.pub.qlog.core.constant.BaseConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;


/**
 * 外部扩展配置
 *
 * @author guixinQiu
 * @date 2021/1/7 15:57
 */

@Data
@ConfigurationProperties(BaseConstants.PUB_XLOG_FILTER)
public class XlogProperties {

    private int order = Ordered.HIGHEST_PRECEDENCE + 1;

    private String ignoredPatterns;

    private String staticResourcePatterns;

    private String source;

}
