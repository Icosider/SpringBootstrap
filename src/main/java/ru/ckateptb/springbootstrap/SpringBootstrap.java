package ru.ckateptb.springbootstrap;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ru.ckateptb.springbootstrap.spring.MinecraftForgeSpringContextInitializer;
import ru.ckateptb.springbootstrap.spring.mod.SpringMod;

@Mod("springbootstrap")
public class SpringBootstrap extends SpringMod {
    public SpringBootstrap() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::on);
    }

    public void on(FMLCommonSetupEvent event) {
        MinecraftForgeSpringContextInitializer.initialize();
    }
}
