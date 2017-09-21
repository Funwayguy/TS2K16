package ts2k16.handlers;

import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ts2k16.core.TSSettings;

public class PlayerGrowthManager
{
	private int sprintTime = 0;
	private boolean wasSprinting = false;
	private int crouchTime = 0;
	private boolean wasCrouched = false;
	
	public void updatePlayer(EntityPlayer player)
	{
		if(player.ticksExisted < sprintTime)
		{
			sprintTime = 0;
		}
		
		if(player.ticksExisted < crouchTime)
		{
			crouchTime = 0;
		}
		
		if(TSSettings.allowSprint && player.isSprinting())
		{
			if(!wasSprinting)
			{
				sprintTime = player.ticksExisted;
			} else if(player.ticksExisted - sprintTime > TSSettings.cooldown)
			{
				pulseGrowth(player.world, player.getPosition(), player);
				sprintTime = player.ticksExisted;
			}
		} else if(player.isSneaking() && !wasCrouched)
		{
			if(player.ticksExisted - crouchTime > TSSettings.cooldown)
			{
				pulseGrowth(player.world, player.getPosition(), player);
				crouchTime = player.ticksExisted;
			}
		}
		
		this.wasSprinting = player.isSprinting();
		this.wasCrouched = player.isSneaking();
	}
	
	public void pulseGrowth(World world, BlockPos pos, EntityPlayer player)
	{
		for(int i = -TSSettings.radius; i <= TSSettings.radius; i++)
		{
			for(int j = -TSSettings.radius; j <= TSSettings.radius; j++)
			{
				for(int k = -TSSettings.radius; k <= TSSettings.radius; k++)
				{
					BlockPos offset = pos.add(i, j, k);
					IBlockState state = world.getBlockState(offset);
					
					if(state.getBlock() instanceof IGrowable)
					{
						String id = state.getBlock().getRegistryName().toString();
						
						if(TSSettings.blacklist.contains(id))
						{
							continue;
						}
						
						boolean flag = false;
						
						if(TSSettings.allPlants)
						{
							flag = true;
						} else if(state.getBlock() instanceof BlockSapling)
						{
							flag = true;
						} else if(TSSettings.whitelist.contains(id))
						{
							flag = true;
						}
						
						if(flag && world.rand.nextInt(100) < TSSettings.chance)
						{
							if(ItemDye.applyBonemeal(new ItemStack(Items.DYE), world, offset, player))
							{
		                        world.playEvent(2005, offset, 0);
							}
						}
					}
				}
			}
		}
	}
}
