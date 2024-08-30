package com.kardasland.aetherpotions.utility.potions;

import java.util.HashMap;
import java.util.Map;

import com.kardasland.aetherpotions.utility.potions.versions.Wrapper1_16_R3;
import org.bukkit.Bukkit;

/**
 * Matches the server's NMS version to its {@link PotionMetaWrapper}
 * Credits to WesJD for the original implementation
 *
 * @author Wesley Smith
 * @since 1.2.1
 */
public class VersionMatcher {

	/** Maps a Minecraft version string to the corresponding revision string */
	private static final Map<String, String> VERSION_TO_REVISION = new HashMap<>() {
		{
			this.put("1.20", "1_20_R1");
			this.put("1.20.3", "1_20_R3");
			this.put("1.20.5", "1_20_R4");
			this.put("1.20.6", "1_20_R4");
			this.put("1.21", "1_21_R1");
			this.put("1.21.1", "1_21_R1");
		}
	};

	/* This needs to be updated to reflect the newest available version wrapper */
	private static final String FALLBACK_REVISION = "1_21_R1";


	public String getVersion() {
		String craftBukkitPackage = Bukkit.getServer().getClass().getPackage().getName();

		String rVersion;
		if (!craftBukkitPackage.contains(".v")) { // cb package not relocated (i.e. paper 1.20.5+)
			final String version = Bukkit.getBukkitVersion().split("-")[0];
			rVersion = VERSION_TO_REVISION.getOrDefault(version, FALLBACK_REVISION);
		} else {
			rVersion = craftBukkitPackage.split("\\.")[3].substring(1);
		}
		return rVersion;
	}

	/**
	 * Matches the server version to it's {@link PotionMetaWrapper}
	 *
	 * @return The {@link PotionMetaWrapper} for this server
	 * @throws IllegalStateException If the version wrapper failed to be instantiated or is unable to be found
	 */
	public PotionMetaWrapper match() {
		String rVersion = getVersion();
		try {
			return (PotionMetaWrapper) Class.forName(getClass().getPackage().getName() + ".Wrapper" + rVersion)
				.getDeclaredConstructor()
				.newInstance();
		} catch (ClassNotFoundException exception) {
			return new Wrapper1_16_R3();
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException("Failed to instantiate version wrapper for version " + rVersion, exception);
		}
	}
}