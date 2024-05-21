package com.motcs.build.mvc.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.IOException;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2024-05-21 星期二
 */
public class CapturingResponseWrapper extends HttpServletResponseWrapper {

    private final CapturingServletOutputStream cos;

    public CapturingResponseWrapper(HttpServletResponse response) {
        super(response);
        cos = new CapturingServletOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return cos;
    }

    // 如果需要，也可以重写getWriter()方法
    // 确保不要同时使用getOutputStream()和getWriter()，因为它们共享同一个输出流
}

