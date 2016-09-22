package ts2k16.core.proxies;

import ts2k16.client.gui.UpdateNotification;
import ts2k16.handlers.EventHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
	public boolean isClient()
	{
		return false;
	}
	
	public void registerHandlers()
	{
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		MinecraftForge.EVENT_BUS.register(new UpdateNotification());
	}
}
