package com.motcs.build.mvc.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2024-05-21 星期二
 */
public class CapturingServletOutputStream extends ServletOutputStream {

    private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

    @Override
    public void write(int b) throws IOException {
        bos.write(b);
    } // 实现 ServletOutputStream 的其他抽象方法

    @Override
    public boolean isReady() {
        // 在这个简单的例子中，我们总是返回 true，表示输出流总是准备好了。
        // 根据你的实际需求，这里可以实现更复杂的逻辑。
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }

    public byte[] getCapturedData() {
        return bos.toByteArray();
    }

}