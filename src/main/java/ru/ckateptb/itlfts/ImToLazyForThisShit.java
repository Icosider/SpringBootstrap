package ru.ckateptb.itlfts;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("itlfts")
public class ImToLazyForThisShit extends SpringMod {
    public ImToLazyForThisShit() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::on);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void on(InterModEnqueueEvent event) {
        this.refresh();
    }
}
