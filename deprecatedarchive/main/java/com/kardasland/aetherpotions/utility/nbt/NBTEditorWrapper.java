package com.kardasland.aetherpotions.utility.nbt;

import io.github.bananapuncher714.nbteditor.NBTEditor;

public class NBTEditorWrapper implements NBTHandler {

    @Override
    public boolean contains(Object object, String key) {
        return NBTEditor.contains(object, key);
    }

    @Override
    public <T> T set(T object, Object value, Object... keys) {
        return NBTEditor.set(object, value, keys);
    }

    @Override
    public String getString(Object object, Object... keys) {
        return contains(object, (String) keys[0]) ? NBTEditor.getString(object, keys) : null;
    }
}
