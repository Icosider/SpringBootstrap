package ru.ckateptb.itlfts.location;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.springframework.util.Assert;

@Getter
@AllArgsConstructor
public class Location {
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public Location(final World world, final BlockPos blockPos) {
        this(world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public Location(final World world, final double x, final double y, final double z) {
        this(world, x, y, z, 0, 0);
    }

    public Location withWorld(World world) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public World getWorld() {
        return world;
    }

    public Chunk getChunk() {
        return world.getChunk((int) x >> 4, (int) z >> 4);
    }

    public BlockPos getBlockPos() {
        return new BlockPos(x, y, z);
    }

    public void setBlockState(BlockState blockState) {
        world.setBlockState(new BlockPos(x, y, z), blockState);
    }

    public BlockState getBlockState() {
        return world.getBlockState(new BlockPos(x, y, z));
    }

    public Location withX(double x) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public double getX() {
        return x;
    }

    public Location withY(double y) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public double getY() {
        return y;
    }

    public Location withZ(double z) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public double getZ() {
        return z;
    }

    public Location withYaw(float yaw) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public float getYaw() {
        return yaw;
    }

    public Location withPitch(float pitch) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public float getPitch() {
        return pitch;
    }

    public Location withCoords(double x, double y, double z) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public Location add(Location location) {
        Assert.isTrue(location.getWorld().equals(getWorld()), "Cannot add Locations of differing worlds");
        return this.add(location.getX(), location.getY(), location.getZ());
    }

    public Location add(Vector3d vec) {
        return this.add(vec.getX(), vec.getY(), vec.getZ());
    }

    public Location add(double x, double y, double z) {
        return withCoords(getX() + x, getY() + y, getZ() + z);
    }

    public Location subtract(Location location) {
        Assert.isTrue(location.getWorld().equals(getWorld()), "Cannot subtract Locations of differing worlds");
        return this.subtract(location.getX(), location.getY(), location.getZ());
    }

    public Location subtract(Vector3d vec) {
        return this.subtract(vec.getX(), vec.getY(), vec.getZ());
    }

    public Location subtract(double x, double y, double z) {
        return withCoords(getX() - x, getY() - y, getZ() - z);
    }

    public double distance(Location o) {
        Assert.isTrue(world.equals(o.world), "Cannot measure distance between different world");
        double xDistance = x - o.x;
        double yDistance = y - o.y;
        double zDistance = z - o.z;
        return Math.sqrt(xDistance * xDistance + yDistance * yDistance + zDistance * zDistance);
    }

    public Location multiply(double m) {
        return withCoords(x * m, y * m, z * m);
    }

    public Location zero() {
        return withCoords(0, 0, 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Location)) return false;
        final Location l = (Location) obj;
        return world.equals(l.world) && x == l.x && y == l.y && z == l.z && yaw == l.yaw && pitch == l.pitch;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(world, x, y, z, yaw, pitch);
    }

    @Override
    public String toString() {
        return String.format("Location{world=%s,x=%s,y=%s,z=%s,pitch=%s,yaw=%s}", world, x, y, z, pitch, yaw);
    }
}
