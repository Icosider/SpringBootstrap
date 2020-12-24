package ru.ckateptb.itlfts.utils;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Реализаци нескольких возможностей из Java14 в Java8
 */
public class FutureUtils {
    /**
     * Возврящает Optional объекта, который применен к классу если это возможно
     * иначе возвращает пустой Optional
     */
    public static <T> Optional<T> cast(Object object, Class<T> tClass) {
        return Optional.ofNullable(tClass.isInstance(object) ? tClass.cast(object) : null);
    }

    /**
     * Возвращает передаваемый объект, применяя к нему все, что указанно в consumer.
     */
    public static <T> T consume(T obj, Consumer<T> consumer) {
        if (obj != null) consumer.accept(obj);
        return obj;
    }
}
