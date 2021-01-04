package ru.ckateptb.springbootstrap.spring.mod;

import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.management.InstanceAlreadyExistsException;

@Getter
public abstract class AbstractSpringContextHolder implements SpringContextHolder {
    private AnnotationConfigApplicationContext context;

    @SneakyThrows
    public void setContext(AnnotationConfigApplicationContext ctx) {
        if (context == null) context = ctx;
        else throw new InstanceAlreadyExistsException();
    }
}
