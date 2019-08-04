package com.songxin.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TrackingFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
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
        if (isCorrelationIdPresent()) {
            System.out.println(getCorrelationId());
        } else {
            String correlationId = generateCorrelationId();
            System.out.println("设置 " + correlationId);
            settCorrelationId(correlationId);
        }
        RequestContext currentContext = RequestContext.getCurrentContext();
        String requestUri = currentContext.getRequest().getRequestURI();
        System.out.println("requestUri : " + requestUri);
        return null;
    }

    private boolean isCorrelationIdPresent() {
        if (getCorrelationId() != null) {
            return true;
        }
        return false;
    }

    private void settCorrelationId(String correlationId) {
        RequestContext currentContext = RequestContext.getCurrentContext();
        currentContext.addZuulRequestHeader("CORRELATION_ID", correlationId);
    }

    private String generateCorrelationId() {
       return UUID.randomUUID().toString();
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
