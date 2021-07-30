package com.gxq.pub.qlog.http.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/***
 *
 * @author guixinQiu
 * @date 2021/1/7 15:57
 */
public final class StreamUtils {

    /**
     * 从流中获取数据
     * @param in
     * @return
     * @throws Exception
     */
    public static byte[] readBytes(InputStream in) throws IOException {
        // 使用字节数组输出流，接收请求数据
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        for (; ; ) {
            int len = in.read(buf);
            if (len == -1) {
                break;
            }
            if (len > 0) {
                baos.write(buf, 0, len);
            }

        }

        return baos.toByteArray();
    }
}
