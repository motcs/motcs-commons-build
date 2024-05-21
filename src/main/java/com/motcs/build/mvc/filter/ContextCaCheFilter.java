package com.motcs.build.mvc.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 接口响应内容缓存
 * 1. GET请求缓存参数
 * 2. 非GET请求[POST,DELETE,PUT] 将删除同一个controller下的缓存
 * 3. 不是四种常用请求时，接口直接放行
 *
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2024-05-21 星期二
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class ContextCaCheFilter implements Filter {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void init(FilterConfig filterConfig) {
        // 初始化Filter，如果有需要的话
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (Objects.equals(httpRequest.getMethod(), HttpMethod.GET.name())) {
            String cacheKey = generateCacheKey(httpRequest);
            log.debug("GET CACHE KEY：{}", cacheKey);
            // 尝试从Redis获取缓存
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String cachedResponse = valueOperations.get(cacheKey);
            if (cachedResponse != null) {
                // 如果缓存存在，直接返回缓存内容
                writeResponse((HttpServletResponse) response, cachedResponse);
            } else {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                // 替换原始的ServletOutputStream
                CapturingResponseWrapper wrappedResponse = new CapturingResponseWrapper(httpResponse);
                // 否则，继续执行请求
                chain.doFilter(request, wrappedResponse);
                // 请求完成后，获取响应内容并缓存
                byte[] responseContent = getContentFromResponse(wrappedResponse);
                valueOperations.set(cacheKey, new String(responseContent, StandardCharsets.UTF_8));
                // 设置缓存过期时间，例如2小时
                redisTemplate.expire(cacheKey, 600, TimeUnit.SECONDS);
                response.getOutputStream().write(responseContent);
            }
        } else if (Objects.equals(httpRequest.getMethod(), HttpMethod.PUT.name()) ||
                Objects.equals(httpRequest.getMethod(), HttpMethod.POST.name()) ||
                Objects.equals(httpRequest.getMethod(), HttpMethod.DELETE.name())) {
            String cacheKey = generateDeleteCacheKey(httpRequest);
            log.debug("NOT GET CACHE KEY:{}", cacheKey);
            this.deleteKeysWithPrefix(cacheKey);
        }
        chain.doFilter(request, response);
    }

    public void deleteKeysWithPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    private byte[] getContentFromResponse(CapturingResponseWrapper response) throws IOException {
        return ((CapturingServletOutputStream) response.getOutputStream()).getCapturedData();
    }

    private void writeResponse(HttpServletResponse httpResponse, String content) throws IOException {
        // 设置状态码，这里假设是200 OK
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        // 设置响应头信息，例如内容类型
        // httpResponse.setContentType("text/plain; charset=utf-8");
        // 使用StringWriter和PrintWriter来处理字符串内容
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        // 将内容写入PrintWriter
        printWriter.write(content);
        printWriter.flush(); // 确保内容被写入StringWriter
        // 从StringWriter获取最终的字符串
        String finalContent = stringWriter.toString();
        // 将内容写入响应的输出流
        httpResponse.getWriter().write(finalContent);
        // 刷新响应，确保内容被发送出去
        httpResponse.flushBuffer();
    }

    public String generateCacheKey(HttpServletRequest request) {
        // 开始构建缓存key
        StringBuilder cacheKey = new StringBuilder();
        // 添加请求方法
        cacheKey.append(request.getMethod()).append(":");
        // 添加请求路径
        cacheKey.append(request.getRequestURI()).append(":");
        // 添加请求参数，排序以保证顺序一致性
        Map<String, String[]> parameterMap = request.getParameterMap();
        List<String> paramNames = new ArrayList<>(parameterMap.keySet());
        Collections.sort(paramNames); // 确保参数顺序一致
        for (String paramName : paramNames) {
            String[] paramValues = parameterMap.get(paramName);
            if (paramValues.length == 1) {
                cacheKey.append(paramName).append(":").append(paramValues[0]).append(":");
            } else {
                cacheKey.append(paramName).append(":");
                for (String paramValue : paramValues) {
                    cacheKey.append(paramValue).append("-");
                }
                if (cacheKey.charAt(cacheKey.length() - 1) == '-') {
                    // 移除最后一个'-'
                    cacheKey.deleteCharAt(cacheKey.length() - 1);
                }
            }
        }
        if (cacheKey.charAt(cacheKey.length() - 1) == ':') {
            // 移除最后一个':'
            cacheKey.deleteCharAt(cacheKey.length() - 1);
        }
        // 返回构建好的缓存key
        return cacheKey.toString();
    }

    public String generateDeleteCacheKey(HttpServletRequest request) {
        // 开始构建缓存key
        StringBuilder cacheKey = new StringBuilder("GET:");
        // 添加请求路径
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        if (split.length > 3) {
            for (int i = 0; i < 3; i++) {
                cacheKey.append(split[i]).append(":");
            }
        } else {
            cacheKey.append(requestURI).append(":");
        }
        if (cacheKey.charAt(cacheKey.length() - 1) == ':') {
            // 移除最后一个':'
            cacheKey.deleteCharAt(cacheKey.length() - 1);
        }
        // 返回构建好的缓存key
        return cacheKey.toString();
    }

    @Override
    public void destroy() {
        // 销毁Filter，如果有需要的话
    }

}