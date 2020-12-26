package ru.ckateptb.itlfts.temporary.paralyze;


import lombok.Getter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.world.GameType;
import ru.ckateptb.itlfts.ImTooLazyForThisShit;
import ru.ckateptb.itlfts.temporary.api.AbstractTemporary;
import ru.ckateptb.itlfts.utils.FutureUtils;
import ru.ckateptb.itlfts.utils.TeleportUtils;

@Getter
public class TemporaryParalyze extends AbstractTemporary {
    private final LivingEntity livingEntity;

    private GameType originalGameMode;
    private ArmorStandEntity armorStandEntity;

    private final TemporaryParalyzeService temporaryParalyzeService = ImTooLazyForThisShit.getContext().getBean(TemporaryParalyzeService.class);

    public TemporaryParalyze(LivingEntity livingEntity, long duration) {
        super(System.currentTimeMillis() + duration);
        this.livingEntity = livingEntity;
        temporaryParalyzeService.paralyzedEntities.add(livingEntity);
        FutureUtils.cast(livingEntity, MobEntity.class).ifPresent(mobEntity -> mobEntity.setNoAI(true));
        FutureUtils.cast(livingEntity, ServerPlayerEntity.class).ifPresent(playerEntity -> {
            originalGameMode = playerEntity.interactionManager.getGameType();
            this.armorStandEntity = (ArmorStandEntity) EntityType.ARMOR_STAND.spawn(playerEntity.getServerWorld(), null, null, playerEntity.getPosition(), SpawnReason.COMMAND, false, false);
            if (armorStandEntity == null) return;
            armorStandEntity.setInvisible(true);
            TeleportUtils.teleport(armorStandEntity, playerEntity);
            playerEntity.setSneaking(false);
            playerEntity.connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.field_241767_d_, (float) GameType.SPECTATOR.getID()));
            playerEntity.setSpectatingEntity(armorStandEntity);
        });
    }

    @Override
    public void onRevert() {
        temporaryParalyzeService.paralyzedEntities.remove(livingEntity);
        FutureUtils.cast(livingEntity, MobEntity.class).ifPresent(mobEntity -> mobEntity.setNoAI(false));
        FutureUtils.cast(livingEntity, ServerPlayerEntity.class).ifPresent(playerEntity -> {
            armorStandEntity.remove();
            playerEntity.connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.field_241767_d_, (float) originalGameMode.getID()));
            playerEntity.setSneaking(true);
            playerEntity.setSneaking(false);
        });
    }

    @Override
    public void onProcess() {

    }

}