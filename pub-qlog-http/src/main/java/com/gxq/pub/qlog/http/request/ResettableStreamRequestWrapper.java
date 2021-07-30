package com.gxq.pub.qlog.http.request;

import com.gxq.pub.qlog.http.constant.HttpMediaConstants;
import com.gxq.pub.qlog.http.util.StreamUtils;
import lombok.val;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/***
 * http request stream wrapper
 * @author guixinQiu
 * @date 2021/1/7 15:57
 */
public class ResettableStreamRequestWrapper extends HttpServletRequestWrapper {

    private byte[] mBodyBuffer;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request the {@link HttpServletRequest} to be wrapped.
     * @throws IllegalArgumentException if the request is null
     */
    public ResettableStreamRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        //如果是form表单或者文件上传，不处理请求数据
        String contentType = request.getContentType();
        if (contentType != null && contentType.trim().length() != 0
                && !contentType.contains(HttpMediaConstants.FORM_URLENCODED)
                && !contentType.contains(HttpMediaConstants.OCTET_STREAM)
                && !contentType.contains(HttpMediaConstants.MULITPART_FILE)
                && !contentType.contains(HttpMediaConstants.FORM_DATA)) {
            this.mBodyBuffer = StreamUtils.readBytes(request.getInputStream());
        }
    }

    public String getRequestBody() {
        return isNotEmpty() ? new String(mBodyBuffer, StandardCharsets.UTF_8) :  HttpMediaConstants.EMPTY_STR;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if ( isNotEmpty()) {
            val in = new ByteArrayInputStream(mBodyBuffer);
            return new BufferedServletInputStream(in, super.getInputStream());
        } else {
            return super.getInputStream();
        }

    }

    private static final class BufferedServletInputStream extends ServletInputStream {
        private final ServletInputStream inputStream;
        private ByteArrayInputStream bais;

        public BufferedServletInputStream(ByteArrayInputStream bais, ServletInputStream inputStream) {
            this.bais = bais;
            this.inputStream = inputStream;
        }

        @Override
        public int available() {
            return this.bais.available();
        }

        @Override
        public int read() {
            return this.bais.read();
        }

        @Override
        public int read(byte[] buf, int off, int len) {
            return this.bais.read(buf, off, len);
        }

        @Override
        public boolean isFinished() {
            return inputStream.isFinished();
        }

        @Override
        public boolean isReady() {
            return inputStream.isReady();
        }

        @Override
        public void close() throws IOException {
            inputStream.close();
            bais.close();
        }

        @Override
        public void setReadListener(ReadListener listener) {
            inputStream.setReadListener(listener);
        }
    }

    private boolean isNotEmpty() {
        return null != mBodyBuffer && mBodyBuffer.length > 0;
    }
}