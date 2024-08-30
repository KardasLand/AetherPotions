package com.kardasland.aetherpotions.utility.nbt;

import com.kardasland.aetherpotions.AetherPotions;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PSCEditorWrapper implements NBTHandler {

    @Override
    public boolean contains(Object object, String key) {
        NamespacedKey namespacedKey = new NamespacedKey(AetherPotions.instance, key);
        if (object instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) object;
            ItemMeta itemMeta = itemStack.getItemMeta();
            PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
            return persistentDataContainer.has(namespacedKey, PersistentDataType.STRING);
        } else {
            return false;
        }
    }

    @Override
    public <T> T set(T object, Object value, Object... keys) {
        // In later, we can use other keys, but for now, this is enough
        // we don't need to use keys[i]
        String key = keys[0].toString();
        if (object instanceof ItemStack) {
            NamespacedKey namespacedKey = new NamespacedKey(AetherPotions.instance, key);
            ItemStack itemStack = (ItemStack) object;
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, value.toString());
            itemStack.setItemMeta(itemMeta);
            return (T) itemStack;
            //return setItemTag((ItemStack)object, value, keys);
        } else {
            return object;
        }
    }

    @Override
    public String getString(Object object, Object... keys) {
        if (contains(object, (String) keys[0])){
            NamespacedKey namespacedKey = new NamespacedKey(AetherPotions.instance, (String) keys[0]);
            if (object instanceof ItemStack) {
                ItemStack itemStack = (ItemStack) object;
                ItemMeta itemMeta = itemStack.getItemMeta();
                PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
                return persistentDataContainer.get(namespacedKey, PersistentDataType.STRING);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
