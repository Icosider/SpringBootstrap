package ru.ckateptb.springbootstrap;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ru.ckateptb.springbootstrap.spring.MinecraftForgeSpringContextInitializer;
import ru.ckateptb.springbootstrap.spring.mod.SpringMod;

@Mod("springbootstrap")
public class SpringBootstrap extends SpringMod {
    public SpringBootstrap() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onCommonSetupEvent);
        bus.addListener(this::onServerStartingEvent);
    }

    public void onCommonSetupEvent(FMLCommonSetupEvent event) {
        MinecraftForgeSpringContextInitializer.initialize();
    }

    public void onServerStartingEvent(FMLServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        getContext().getBeanFactory().registerResolvableDependency(server.getClass(), server);
    }
}
