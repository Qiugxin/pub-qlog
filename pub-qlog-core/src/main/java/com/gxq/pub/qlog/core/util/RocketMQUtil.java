package com.gxq.pub.qlog.core.util;

/**
 * @author guixinQiu
 * @date 2021/1/7 15:57
 */
public class RocketMQUtil {
  public static void setLogRoot(String logRoot) {
    String rmqLogRoot = System.getProperty("rocketmq.client.logRoot");
    if (StringUtils.isEmpty(rmqLogRoot)) {
      System.setProperty("rocketmq.client.logRoot", logRoot + "/other/rocketmqlogs");
    }

    String onsLogRoot = System.getProperty("ons.client.logRoot");
    if (StringUtils.isEmpty(onsLogRoot)) {
      System.setProperty("ons.client.logRoot", logRoot + "/other/rocketmqlogs");
    }
  }
}
