package com.ruoyi.common.filter;


import com.ruoyi.common.utils.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UrlFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String originalPath = request.getRequestURI();
        String modifiedPath = modifyPath(originalPath);

        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request) {
            @Override
            public String getRequestURI() {
                return modifiedPath;
            }

        };

        filterChain.doFilter(wrappedRequest, response);
    }

    private String modifyPath(String path) {
        if((path.contains("api") || path.contains("coinApi")) && path.contains("hl32zd")){
            String result = "";
            String[] uris = path.split("/");
            for (String str : uris) {
                if (StringUtils.isNotEmpty(str)) {
                    if (str.indexOf("hl32zd") > 0) {
                        result += "/" + str.substring(0, str.indexOf("hl32zd"));
                    } else {
                        result += "/" + str;
                    }
                }
            }
            path = result;
        }
        return path;
    }

}
