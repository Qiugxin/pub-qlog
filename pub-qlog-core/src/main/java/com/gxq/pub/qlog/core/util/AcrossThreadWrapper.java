package com.gxq.pub.qlog.core.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;

/**
 * 跨程传递mdc变量包装类
 *
 * @author guixinQiu
 * @date 2021/1/7 15:57
 */
@Slf4j
public class AcrossThreadWrapper implements Runnable {
  final Runnable runnable;
  Map<String, String> mdc;

  public AcrossThreadWrapper(Runnable runnable) {
    this.runnable = runnable;
    this.mdc = MDC.getCopyOfContextMap();
  }

  public static AcrossThreadWrapper of(Runnable r) {
    return new AcrossThreadWrapper(r);
  }

  @Override
  public void run() {
    MdcUtils.setCurrentLocalMDC(mdc, null);
    this.runnable.run();
  }
}
