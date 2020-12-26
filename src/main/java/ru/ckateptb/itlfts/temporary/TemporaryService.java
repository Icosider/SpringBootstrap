package ru.ckateptb.itlfts.temporary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.ckateptb.itlfts.listeter.Listener;
import ru.ckateptb.itlfts.temporary.api.Temporary;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Service
@EnableScheduling
@NoArgsConstructor
public class TemporaryService implements Listener {
    public final List<Temporary> temporaries = new CopyOnWriteArrayList<>();

    @Scheduled(initialDelay = 0, fixedRate = 1)
    public void updateAll() {
        temporaries.forEach(Temporary::process);
    }

    /**
     * Вовзращает все временные объекты на исходную позицию
     */
    public void revertAll() {
        temporaries.forEach(Temporary::revert);
    }

    @SubscribeEvent
    public void on(FMLServerStoppingEvent event) {
        this.revertAll();
    }
}
