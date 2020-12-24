package ru.ckateptb.itlfts.temporary.api;

import lombok.Setter;
import ru.ckateptb.itlfts.ImToLazyForThisShit;
import ru.ckateptb.itlfts.temporary.TemporaryService;

@Setter
public abstract class AbstractTemporary implements Temporary {
    protected final TemporaryService service;
    protected long expireTime;

    public AbstractTemporary(long expireTime) {
        this.expireTime = expireTime;
        this.service = ImToLazyForThisShit.getContext().getBean(TemporaryService.class);
        this.service.getTemporaries().add(this);
    }

    @Override
    public void process() {
        if (System.currentTimeMillis() < getExpireTime()) onProcess();
        else revert() ;
    }

    @Override
    public void revert() {
        onRevert();
        service.getTemporaries().remove(this);
    }

    @Override
    public long getExpireTime() {
        return expireTime;
    }
}
