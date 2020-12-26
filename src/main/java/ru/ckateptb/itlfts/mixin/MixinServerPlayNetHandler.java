package ru.ckateptb.itlfts.mixin;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CUseEntityPacket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.ckateptb.itlfts.event.PlayerToggleSneakEvent;

@Mixin(ServerPlayNetHandler.class)
public class MixinServerPlayNetHandler {
    public ServerPlayerEntity player;

    @Inject(method = "processUseEntity", at = @At(value = "INVOKE", target = "Ljava/util/Optional;empty()Ljava/util/Optional;"), cancellable = true)
    public void onProcessUseEntity(CUseEntityPacket packetIn, CallbackInfo callbackInfo) {
        if (this.player == packetIn.getEntityFromWorld(this.player.getServerWorld())) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "processEntityAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/IPacket;Lnet/minecraft/network/INetHandler;Lnet/minecraft/world/server/ServerWorld;)V"), cancellable = true)
    public void onProcessEntityAction(CEntityActionPacket packetIn, CallbackInfo callbackInfo) {
        PlayerEvent event = null;
        switch (packetIn.getAction()) {
            case PRESS_SHIFT_KEY: {
                event = new PlayerToggleSneakEvent(player, PlayerToggleSneakEvent.SneakState.PRESS);
                break;
            }
            case RELEASE_SHIFT_KEY: {
                event = new PlayerToggleSneakEvent(player, PlayerToggleSneakEvent.SneakState.RELEASE);
                break;
            }
        }
        if (event == null) return;
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) callbackInfo.cancel();
    }
}
