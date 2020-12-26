package ru.ckateptb.itlfts.temporary.paralyze;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.springframework.stereotype.Service;
import ru.ckateptb.itlfts.event.PlayerToggleSneakEvent;
import ru.ckateptb.itlfts.listeter.Listener;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class TemporaryParalyzeService implements Listener {
    final Set<LivingEntity> paralyzedEntities = new HashSet<>();

    public boolean isParalyzed(LivingEntity livingEntity) {
        return paralyzedEntities.contains(livingEntity);
    }

    public Set<LivingEntity> getAll() {
        return Collections.unmodifiableSet(paralyzedEntities);
    }

    @SubscribeEvent
    public void on(PlayerToggleSneakEvent event) {
        if (isParalyzed(event.getPlayer()) && event.getState() == PlayerToggleSneakEvent.SneakState.PRESS) {
            event.setCanceled(true);
        }
    }
}
