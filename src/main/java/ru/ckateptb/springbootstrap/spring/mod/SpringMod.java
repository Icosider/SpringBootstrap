package ru.ckateptb.springbootstrap.spring.mod;

import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.ckateptb.springbootstrap.spring.MinecraftForgeSpringContextInitializer;

import javax.management.InstanceAlreadyExistsException;

@Data
public abstract class SpringMod implements SpringContextHolder {
    private AnnotationConfigApplicationContext context;

    public SpringMod() {
        MinecraftForgeSpringContextInitializer.register(this);
    }

    @Override
    @SneakyThrows
    public void setContext(AnnotationConfigApplicationContext ctx) {
        if (context == null) context = ctx;
        else throw new InstanceAlreadyExistsException();
    }
}
