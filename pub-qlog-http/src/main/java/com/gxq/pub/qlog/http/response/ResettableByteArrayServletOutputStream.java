package com.gxq.pub.qlog.http.response;

import com.gxq.pub.qlog.http.constant.HttpMediaConstants;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/***
 * output tream
 *
 * @author guixinQiu
 * @date 2021/1/7 15:57
 */
public class ResettableByteArrayServletOutputStream extends ServletOutputStream {

    private ServletOutputStream targetStream;
    private StringBuilder data;

    public ResettableByteArrayServletOutputStream(ServletOutputStream outputStream, StringBuilder body) {
        this.targetStream = outputStream;
        this.data = body;
    }

    @Override
    public void write(int b) throws IOException {
        targetStream.write(b);
        data.append(new String(new byte[] {(byte)b}, StandardCharsets.UTF_8));
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        targetStream.write(b, off, len);
        data.append(new String(b, off, len, StandardCharsets.UTF_8));
    }

    @Override
    public String toString() {
        return (null != data && data.toString().length() > 0) ? data.toString() : HttpMediaConstants.EMPTY_STR;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {}
}
