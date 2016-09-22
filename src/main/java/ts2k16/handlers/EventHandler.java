package ts2k16.handlers;

import java.util.HashMap;
import java.util.UUID;
import ts2k16.core.TS2K16;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler
{
	HashMap<UUID,PlayerGrowthManager> playerStates = new HashMap<UUID,PlayerGrowthManager>();
	
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event)
	{
		if(event.getEntityLiving().worldObj.isRemote || !(event.getEntityLiving() instanceof EntityPlayer))
		{
			return;
		}
		
		EntityPlayer player = (EntityPlayer)event.getEntityLiving();
		
		PlayerGrowthManager state = playerStates.get(player.getUniqueID());
		state = state != null? state : new PlayerGrowthManager();
		state.updatePlayer(player);
		playerStates.put(player.getUniqueID(), state);
	}
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent event)
	{
		if(event.getModID().equals(TS2K16.MODID))
		{
			ConfigHandler.config.save();
			ConfigHandler.initConfigs();
		}
	}
}
