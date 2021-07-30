package com.gxq.pub.qlog.http.response;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.PrintWriter;

/***
 * http response stream writer
 *
 * @author guixinQiu
 * @date 2021/1/7 15:57
 */
public class ResettableStreamWriter extends FilterWriter {

    /**
     * 输出的字符串
     */
    private StringBuilder data;

    public ResettableStreamWriter(PrintWriter writer, StringBuilder data) {
        super(writer);
        this.data = data;
    }

    @Override
    public void write(char cbuf[], int off, int len) throws IOException {
        char[] dest = new char[len];
        System.arraycopy(cbuf, off, dest, 0, len);
        data.append(dest);
        super.write(cbuf, off, len);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        data.append(str.substring(off, off + len));
        super.write(str, off, len);
    }

}