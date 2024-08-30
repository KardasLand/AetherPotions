package com.kardasland.aetherpotions.utility.nbt;

public interface NBTHandler {

    boolean contains(Object object, String key);

    <T> T set(T object, Object value, Object... keys);

    String getString(Object object, Object... keys);
}
