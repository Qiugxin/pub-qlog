package com.gxq.pub.qlog.boot.configuration;

import com.gxq.pub.qlog.boot.constant.LoggingConstants;
import com.gxq.pub.qlog.boot.properties.XlogProperties;
import com.gxq.pub.qlog.core.constant.BaseConstants;
import com.gxq.pub.qlog.core.filter.CidFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/***
 * spring boot/cloud项目日志输出
 *
 * @author guixinQiu
 * @since 2021/1/7 13:40
 */
@Configuration
@ConditionalOnProperty(name = LoggingConstants.LOG_ENABLE_KEY, prefix = BaseConstants.PUB_XLOG_FILTER, matchIfMissing = true)
@EnableConfigurationProperties(XlogProperties.class)
public class XlogAutoConfiguration {

    @Autowired
    private XlogProperties xlogProperties;

    @Value("${spring.application.name:localhost}")
    private String applicationName;

    /**
     * 日志过滤器
     *
     * @return
     */
    @Bean
    @ConditionalOnClass({Filter.class})
    public FilterRegistrationBean cidFilterRegistration() {
        CidFilter cidFilter = new CidFilter();
        FilterRegistrationBean registration = new FilterRegistrationBean(cidFilter);
        registration.setName(cidFilter.getClass().getName());
        registration.setAsyncSupported(true);

        String ignoredPatterns = xlogProperties.getIgnoredPatterns();
        if (StringUtils.isNotBlank(ignoredPatterns)) {
            registration.addInitParameter(BaseConstants.IGNORE_RESOURCE_URI_ENV, ignoredPatterns);
        }

        String staticResourcePatterns = xlogProperties.getStaticResourcePatterns();
        if (StringUtils.isNotBlank(staticResourcePatterns)) {
            registration.addInitParameter(BaseConstants.STATIC_RESOURCE_URI_ENV, staticResourcePatterns);
        }

        if (StringUtils.isNotBlank(applicationName)) {
            registration.addInitParameter(BaseConstants.LOG_NAME_KEY, applicationName);
        }

        registration.setOrder(xlogProperties.getOrder());
        return registration;
    }

}