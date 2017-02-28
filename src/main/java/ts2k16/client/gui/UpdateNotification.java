package ts2k16.client.gui;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.logging.log4j.Level;
import ts2k16.core.TS2K16;
import ts2k16.core.TSSettings;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class UpdateNotification
{
	boolean hasChecked = false;
	@SuppressWarnings("unused")
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		if(!TS2K16.proxy.isClient() || hasChecked)
		{
			return;
		}
		
		hasChecked = true;
		
		if(TS2K16.HASH.equals("CI_MOD_" + "HASH"))
		{
			event.player.sendMessage(new TextComponentString(TextFormatting.RED + "THIS COPY OF " + TS2K16.NAME.toUpperCase() + " IS NOT FOR PUBLIC USE!"));
			return;
		}
		
		try
		{
			String[] data = getNotification("http://bit.ly/2cSJQ7U", true);
			
			if(TSSettings.hideUpdates)
			{
				return;
			}
			
			ArrayList<String> changelog = new ArrayList<String>();
			boolean hasLog = false;
			
			for(String s : data)
			{
				if(s.equalsIgnoreCase("git_branch:" + TS2K16.BRANCH))
				{
					if(!hasLog)
					{
						hasLog = true;
						changelog.add(s);
					} else
					{
						break;
					}
				} else if(s.toLowerCase().startsWith("git_branch:"))
				{
					if(hasLog)
					{
						break;
					}
				} else if(hasLog)
				{
					changelog.add(s);
				}
			}
			
			if(!hasLog || data.length < 2)
			{
				event.player.sendMessage(new TextComponentString(TextFormatting.RED + "An error has occured while checking " + TS2K16.NAME + " version!"));
				TS2K16.logger.log(Level.ERROR, "An error has occured while checking " + TS2K16.NAME + " version! (hasLog: " + hasLog + ", data: " + data.length + ")");
				return;
			} else
			{
				// Only the relevant portion of the changelog is preserved
				data = changelog.toArray(new String[0]);
			}
			
			String hash = data[1].trim();
			
			boolean hasUpdate = !TS2K16.HASH.equalsIgnoreCase(hash);
			
			if(hasUpdate)
			{
				event.player.sendMessage(new TextComponentString(TextFormatting.RED + "Update for " + TS2K16.NAME + " available!"));
				event.player.sendMessage(new TextComponentString("Download: http://minecraft.curseforge.com/projects/twerk-sim-2k16"));
				
				for(int i = 2; i < data.length; i++)
				{
					if(i > 5)
					{
						event.player.sendMessage(new TextComponentString("and " + (data.length - 5) + " more..."));
						break;
					} else
					{
						event.player.sendMessage(new TextComponentString("- " + data[i].trim()));
					}
				}
			}
			
		} catch(Exception e)
		{
			event.player.sendMessage(new TextComponentString(TextFormatting.RED + "An error has occured while checking " + TS2K16.NAME + " version!"));
			TS2K16.logger.log(Level.ERROR, "An error has occured while checking " + TS2K16.NAME + " version!", e);
		}
	}
	
	public static String[] getNotification(String link, boolean doRedirect) throws Exception
	{
		URL url = new URL(link);
		HttpURLConnection.setFollowRedirects(false);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setDoOutput(false);
		con.setReadTimeout(20000);
		con.setRequestProperty("Connection", "keep-alive");
		
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:16.0) Gecko/20100101 Firefox/16.0");
		((HttpURLConnection)con).setRequestMethod("GET");
		con.setConnectTimeout(5000);
		BufferedInputStream in = new BufferedInputStream(con.getInputStream());
		int responseCode = con.getResponseCode();
		HttpURLConnection.setFollowRedirects(true);
		if(responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_MOVED_PERM)
		{
			System.out.println("Update request returned response code: " + responseCode + " " + con.getResponseMessage());
		} else if(responseCode == HttpURLConnection.HTTP_MOVED_PERM)
		{
			if(doRedirect)
			{
				return getNotification(con.getHeaderField("location"), false);
			} else
			{
				throw new Exception();
			}
		}
		StringBuilder buffer = new StringBuilder();
		int chars_read;
		//	int total = 0;
		while((chars_read = in.read()) != -1)
		{
			char g = (char)chars_read;
			buffer.append(g);
		}
		final String page = buffer.toString();
		
		return page.split("\\n");
	}
}
