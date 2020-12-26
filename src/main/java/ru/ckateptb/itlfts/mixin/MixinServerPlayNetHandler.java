package ru.ckateptb.itlfts.mixin;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CUseEntityPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetHandler.class)
public class MixinServerPlayNetHandler {
    public ServerPlayerEntity player;

    @Inject(method = "processUseEntity", at = @At(value = "INVOKE", target = "Ljava/util/Optional;empty()Ljava/util/Optional;"), cancellable = true)
    public void onProcessUseEntity(CUseEntityPacket packetIn, CallbackInfo callbackInfo) {
        if (this.player == packetIn.getEntityFromWorld(this.player.getServerWorld())) {
            callbackInfo.cancel();
        }
    }
}
