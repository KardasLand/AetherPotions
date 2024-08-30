package com.kardasland.aetherpotions.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PotionCache {
	public Map<UUID, String> cache;

	public PotionCache() {
		this.cache = new HashMap<>();
	}

	public void add(UUID uuid, String potionId) {
		cache.put(uuid, potionId);
	}

	public String get(UUID uuid) {
		return cache.getOrDefault(uuid, null);
	}

	public void remove(UUID uuid) {
		cache.remove(uuid);
	}

	public boolean contains(UUID uuid) {
		return cache.containsKey(uuid);
	}

	public void clear() {
		cache.clear();
	}

	public void saveAll() {
		for (Map.Entry<UUID, String> entry : cache.entrySet()) {
			ConfigManager.get("tempdata.yml").set("potioncache." + entry.getKey().toString(), entry.getValue());
		}
		ConfigManager.save("tempdata.yml");
		ConfigManager.reload("tempdata.yml");
	}

	public void loadAll() {
		ConfigManager.load("tempdata.yml");
		for (String key : ConfigManager.get("tempdata.yml").getConfigurationSection("potioncache").getKeys(false)) {
			cache.put(UUID.fromString(key), ConfigManager.get("tempdata.yml").getString("potioncache." + key));
		}
	}
}
