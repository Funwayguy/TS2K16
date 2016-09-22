package ts2k16.core;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.Logger;
import ts2k16.core.proxies.CommonProxy;
import ts2k16.handlers.ConfigHandler;

@Mod(modid = TS2K16.MODID, version = TS2K16.VERSION, name = TS2K16.NAME, guiFactory = TS2K16.MODID + ".handlers.ConfigGuiFactory")
public class TS2K16
{
    public static final String MODID = "ts2k16";
    public static final String VERSION = "CI_MOD_VERSION";
    public static final String BRANCH = "CI_MOD_BRANCH";
    public static final String HASH = "CI_MOD_HASH";
    public static final String NAME = "Twerk-Sim 2K16";
    public static final String PROXY = MODID + ".core.proxies";
    public static final String CHANNEL = "TS_SIM_CHAN";
	
	@Instance(MODID)
	public static TS2K16 instance;
	
	@SidedProxy(clientSide = PROXY + ".ClientProxy", serverSide = PROXY + ".CommonProxy")
	public static CommonProxy proxy;
	public SimpleNetworkWrapper network;
	public static Logger logger;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger = event.getModLog();
    	network = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL);
    	
    	ConfigHandler.config = new Configuration(event.getSuggestedConfigurationFile(), true);
    	ConfigHandler.initConfigs();
    	
    	proxy.registerHandlers();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    }
}
