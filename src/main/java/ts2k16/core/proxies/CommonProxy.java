package ts2k16.core.proxies;

import net.minecraftforge.common.MinecraftForge;
import ts2k16.handlers.EventHandler;

public class CommonProxy
{
	public boolean isClient()
	{
		return false;
	}
	
	public void registerHandlers()
	{
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}
}
