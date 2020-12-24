package ru.ckateptb.itlfts;

import lombok.Getter;
import net.minecraftforge.fml.common.Mod;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.ckateptb.itlfts.spring.MinecraftForgeSpringContextInitializer;

@Mod("itlfts")
public class ImToLazyForThisShit {
    @Getter
    private static AnnotationConfigApplicationContext context;

    public ImToLazyForThisShit() {
        context = MinecraftForgeSpringContextInitializer.initializeContext(this);
    }
}
