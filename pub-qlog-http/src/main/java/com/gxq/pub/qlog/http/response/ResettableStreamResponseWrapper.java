package com.gxq.pub.qlog.http.response;

import com.gxq.pub.qlog.http.constant.HttpMediaConstants;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

/***
 * http response stream wrapper
 * @author guixinQiu
 * @date 2021/1/7 15:57
 */
public class ResettableStreamResponseWrapper extends HttpServletResponseWrapper {

    private ResettableByteArrayServletOutputStream outputStream;
    private StringBuilder body;

    public ResettableStreamResponseWrapper(HttpServletResponse response, StringBuilder body) {
        super(response);
        this.body = body;
    }

    @Override
    public void setContentType(String type) {
        super.setContentType(type);
    }

    @Override
    public void addHeader(String name, String value) {
        super.addHeader(name, value);
    }

    @Override
    public void setHeader(String name, String value) {
        super.setHeader(name, value);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return outputStream = new ResettableByteArrayServletOutputStream(super.getOutputStream(), body);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(new ResettableStreamWriter(super.getWriter(), body));
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public String toString() {
        return (outputStream != null) ? outputStream.toString() : HttpMediaConstants.EMPTY_STR;
    }

}
