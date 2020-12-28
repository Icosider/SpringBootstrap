package ru.ckateptb.springbootstrap.spring.mod;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public interface SpringContextHolder {
    AnnotationConfigApplicationContext getContext();
    void setContext(AnnotationConfigApplicationContext context);
}
