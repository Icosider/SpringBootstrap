package ru.ckateptb.springbootstrap.spring.mod;

import ru.ckateptb.springbootstrap.spring.MinecraftForgeSpringContextInitializer;

public abstract class SpringMod extends AbstractSpringContextHolder {
    public SpringMod() {
        MinecraftForgeSpringContextInitializer.register(this);
    }
}
