package ru.ckateptb.itlfts.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.world.server.ServerWorld;
import ru.ckateptb.itlfts.location.Location;

public class TeleportUtils {
    public static void teleport(LivingEntity source, LivingEntity target) {
        Vector2f rotation = target.getPitchYaw();
        teleport(source, new Location(target.getEntityWorld(), target.getPosX(), target.getPosY(), target.getPosZ(), rotation.y, rotation.x));
    }

    public static void teleport(LivingEntity source, BlockPos target) {
        teleport(source, new Location(source.getEntityWorld(), target.getX(), target.getY(), target.getZ()));
    }

    public static void teleport(LivingEntity source, Location location) {
        ServerWorld world = location.getServerWorld();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        if (source instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) source).teleport(world, x, y, z, yaw, pitch);
        } else {
            if (!source.world.equals(world)) source.changeDimension((ServerWorld) location.getWorld());
            source.setLocationAndAngles(location.getX(), location.getY(), location.getZ(), yaw, location.getPitch());
            source.setRotationYawHead(yaw);
        }
    }
}
