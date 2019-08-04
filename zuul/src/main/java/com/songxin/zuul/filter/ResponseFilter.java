package com.songxin.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ResponseFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext currentContext = RequestContext.getCurrentContext();

        currentContext.getResponse().setHeader("CORRELATION_ID",getCorrelationId());

        String requestUri = currentContext.getRequest().getRequestURI();
        System.out.println("responet correlationId : " + getCorrelationId());
        System.out.println("responetUri : " + requestUri);
        return null;
    }

    private boolean isCorrelationIdPresent() {
        if (getCorrelationId() != null) {
            return true;
        }
        return false;
    }

    private String getCorrelationId() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        if (currentContext.getRequest().getHeader("CORRELATION_ID") != null) {
            return currentContext.getRequest().getHeader("CORRELATION_ID");
        } else {
            return currentContext.getZuulRequestHeaders().get("CORRELATION_ID");
        }
    }
}
