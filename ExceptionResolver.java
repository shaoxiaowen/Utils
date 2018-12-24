package com.mmall.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: TODO
 * @author: xiaowen
 * @create: 2018-12-24 09:17
 **/
@Slf4j
@Component
public class ExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.error("{} Exception",request.getRequestURI(),ex);
        ModelAndView modelAndView=new ModelAndView(new MappingJackson2JsonView());

        //当使用jackson1.x的时候使用MappingJacksonJsonView
        //当使用jackson2.x的时候使用MappingJackson2JsonView
        modelAndView.addObject("status",ResponseCode.ERROR.getCode());
        modelAndView.addObject("msg","接口异常,详情请查看服务端日志信息");
        modelAndView.addObject("data",ex.toString());
        return modelAndView;
    }
}
