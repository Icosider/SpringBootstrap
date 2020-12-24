package ru.ckateptb.itlfts.temporary;

import com.google.common.collect.ArrayListMultimap;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.ckateptb.itlfts.temporary.api.AbstractTemporary;

import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class TemporaryBlock extends AbstractTemporary {
    private static final ArrayListMultimap<String, TemporaryBlock> blocks = ArrayListMultimap.create();
    public static boolean isTemporaryBlock(World world, BlockPos pos) {
        return blocks.containsKey(String.format("|_%s_|_%s_|", world.getProviderName(), pos.toLong()));
    }

    private final World world;
    private final BlockPos pos;
    private final String key;
    private final BlockState state;
    private final BlockState previousState;
    private BlockState originalState;

    public TemporaryBlock(World world, BlockPos pos, BlockState state, long duration) {
        super(System.currentTimeMillis() + duration);
        this.world = world;
        this.pos = pos;
        this.key = String.format("|_%s_|_%s_|", world.getProviderName(), pos.toLong());
        this.state = state;
        this.previousState = world.getBlockState(pos);
        this.originalState = previousState;
        new CopyOnWriteArrayList<>(blocks.get(key)).forEach(block -> {
            this.originalState = block.originalState;
            if (block.getExpireTime() <= getExpireTime()) {
                blocks.remove(key, block);
                service.getTemporaries().remove(block);
            }
        });
        blocks.put(key, this);
        world.setBlockState(pos, state);
    }

    @Override
    public void onProcess() {
    }

    @Override
    public void onRevert() {
        world.setBlockState(pos, blocks.get(key).size() == 1 ? originalState : previousState);
        blocks.remove(key, this);
    }
}
