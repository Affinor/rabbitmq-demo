package com.jin.demo.rabbitmq.rabbitmqdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author wangjin
 */
@Configuration
public class MyWebMvcConfigurer extends WebMvcConfigurationSupport {
    /**
     * 1.这个为解决中文乱码
     * @return
     */
    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }

    /**
     * 2.1：解决中文乱码后，返回json时可能会出现No converter found for return value of type: xxxx
     * 或这个：Could not find acceptable representation
     * 解决此问题如下
     * @return
     */
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    /**
     * 2.2：解决No converter found for return value of type: xxxx
     * @return
     */
    public MappingJackson2HttpMessageConverter messageConverter() {
        MappingJackson2HttpMessageConverter converter=new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(getObjectMapper());
        return converter;
    }



    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        //解决中文乱码
        converters.add(responseBodyConverter());

        //解决： 添加解决中文乱码后的配置之后，返回json数据直接报错 500：no convertter for return value of type
        //或这个：Could not find acceptable representation
        converters.add(messageConverter());
    }

    /**
     * 设置默认访问的首页
     * @param registry
     */
    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/static/index.html");
        super.addViewControllers(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 这里之所以多了一"/",是为了解决打war时访问不到问题
        registry.addResourceHandler("/**").addResourceLocations("/","classpath:/");
    }
}
