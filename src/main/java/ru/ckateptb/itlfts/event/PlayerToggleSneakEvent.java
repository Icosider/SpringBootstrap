package ru.ckateptb.itlfts.event;

import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Getter
@Cancelable
public class PlayerToggleSneakEvent extends PlayerEvent {
    private final SneakState state;

    public PlayerToggleSneakEvent(PlayerEntity player, SneakState state) {
        super(player);
        this.state = state;
    }

    public enum SneakState {
        PRESS, RELEASE
    }
}
