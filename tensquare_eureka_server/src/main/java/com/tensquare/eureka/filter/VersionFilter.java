package com.tensquare.eureka.filter;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

@Component
public class VersionFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        String versionContent = "";
        ClassPathResource resource = new ClassPathResource("version.txt");
        try {
            versionContent = FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            //log
        }
        versionContent = "<html><body>" + versionContent + getJvmMemoryInfo() + "</body></html>";

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.getWriter().println(versionContent);
    }

    @Override
    public void destroy() {

    }


    /**
     * 输出内存信息
     */
    private String getJvmMemoryInfo() {
        Runtime run = Runtime.getRuntime();
        long max = run.maxMemory();
        long total = run.totalMemory();
        long free = run.freeMemory();
        long usable = max - total + free;

        int oneMb = 1024 * 1024;
        return String.format("<br/> Max Memory:  %d MB", max / oneMb) +
                String.format("<br/> Used Memory:  %d MB", total / oneMb) +
                String.format("<br/> Free Memory:  %d MB", free / oneMb) +
                String.format("<br/> Usable Memory:  %d MB", usable / oneMb);
    }

}
