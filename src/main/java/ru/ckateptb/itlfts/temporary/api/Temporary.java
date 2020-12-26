package ru.ckateptb.itlfts.temporary.api;

/**
 * @see AbstractTemporary
 */
public interface Temporary {
    /**
     * @see AbstractTemporary#process()
     */
    void process();

    void onProcess();

    /**
     * Возвращает исходное положение текущего объекта
     * @see AbstractTemporary#revert()
     */
    void revert();

    void onRevert();

    /**
     * @return Время в мс, когда текущией объект должен вернуться в исходное положение
     * @see AbstractTemporary#getExpireTime()
     */
    long getExpireTime();
}
