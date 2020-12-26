package ru.ckateptb.itlfts.temporary.block;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import ru.ckateptb.itlfts.ImTooLazyForThisShit;
import ru.ckateptb.itlfts.location.Location;
import ru.ckateptb.itlfts.temporary.api.AbstractTemporary;

import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class TemporaryBlock extends AbstractTemporary {

    private final Location location;
    private final BlockState state;
    private final BlockState previousState;
    private BlockState originalState;
    private final TemporaryBlockService temporaryBlockService = ImTooLazyForThisShit.getContext().getBean(TemporaryBlockService.class);

    public TemporaryBlock(Location location, Block block, long duration) {
        this(location, block.getDefaultState(), duration);
    }

    //TODO Disable physics and keep attachable blocks nearby && save containers
    public TemporaryBlock(Location location, BlockState state, long duration) {
        super(System.currentTimeMillis() + duration);
        this.location = location;
        this.state = state;
        this.previousState = location.getBlockState();
        this.originalState = previousState;
        new CopyOnWriteArrayList<>(temporaryBlockService.temporaryBlocks.get(location)).forEach(block -> {
            this.originalState = block.originalState;
            if (block.getExpireTime() <= getExpireTime()) {
                temporaryBlockService.temporaryBlocks.remove(location, block);
                service.getTemporaries().remove(block);
            }
        });
        temporaryBlockService.temporaryBlocks.put(location, this);
        location.setBlockState(state);
    }

    @Override
    public void onProcess() {
    }

    @Override
    public void onRevert() {
        location.setBlockState(temporaryBlockService.temporaryBlocks.get(location).size() == 1 ? originalState : previousState);
        temporaryBlockService.temporaryBlocks.remove(location, this);
    }
}
