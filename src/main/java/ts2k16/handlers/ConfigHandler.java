package ts2k16.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import ts2k16.core.TSSettings;
import ts2k16.core.TS2K16;

public class ConfigHandler
{
	public static Configuration config;
	
	public static void initConfigs()
	{
		if(config == null)
		{
			TS2K16.logger.log(Level.ERROR, "Config attempted to be loaded before it was initialised!");
			return;
		}
		
		config.load();
		
		TSSettings.allowSprint = config.getBoolean("Allow Sprinting", Configuration.CATEGORY_GENERAL, true, "Allows users to sprint instead of sneaking");
		TSSettings.cooldown = config.getInt("Cooldown", Configuration.CATEGORY_GENERAL, 10, 0, Integer.MAX_VALUE, "Minimum number of ticks between each growth");
		TSSettings.radius = config.getInt("Radius", Configuration.CATEGORY_GENERAL, 3, 1, 16, "Growth effect radius around the player");
		TSSettings.chance = config.getInt("Chance", Configuration.CATEGORY_GENERAL, 25, 1, 100, "Percent chance that growth will occur");
		TSSettings.allPlants = config.getBoolean("All Plants", Configuration.CATEGORY_GENERAL, false, "Make growth effect all plants, not just saplings");
		String[] wl = config.getStringList("Whitelist", Configuration.CATEGORY_GENERAL, new String[0], "Additional plants that can be affected (Case Sensitive)");
		String[] bl = config.getStringList("Blacklist", Configuration.CATEGORY_GENERAL, new String[]{"minecraft:grass"}, "Plants blacklisted from the effect (Case Sensitive)");
		
		TSSettings.whitelist = new ArrayList<String>(Arrays.asList(wl));
		TSSettings.blacklist = new ArrayList<String>(Arrays.asList(bl));
		
		config.save();
	}
}
