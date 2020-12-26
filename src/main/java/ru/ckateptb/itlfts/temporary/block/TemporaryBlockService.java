package ru.ckateptb.itlfts.temporary.block;

import com.google.common.collect.ArrayListMultimap;
import org.springframework.stereotype.Service;
import ru.ckateptb.itlfts.location.Location;

import java.util.Collections;
import java.util.List;

@Service
public class TemporaryBlockService {
    final ArrayListMultimap<Location, TemporaryBlock> temporaryBlocks = ArrayListMultimap.create();

    public boolean isTemporaryBlock(Location location) {
        return temporaryBlocks.containsKey(location);
    }

    public List<TemporaryBlock> get(Location location) {
        return Collections.unmodifiableList(temporaryBlocks.get(location));
    }
}
