package ru.ckateptb.itlfts.temporary.api;

public interface Temporary {
    void process();
    void onProcess();
    void revert();
    void onRevert();
    long getExpireTime();
}
