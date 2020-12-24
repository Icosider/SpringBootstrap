package ru.ckateptb.itlfts;

import lombok.Getter;
import net.minecraftforge.fml.common.Mod;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.ckateptb.itlfts.spring.MinecraftForgeSpringContextInitializer;

@Mod("itlfts")
public class ImTooLazyForThisShit {
    @Getter
    private static AnnotationConfigApplicationContext context;

    public ImTooLazyForThisShit() {
        context = MinecraftForgeSpringContextInitializer.initializeContext(this);
    }
}
