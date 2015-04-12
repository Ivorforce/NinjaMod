package net.ivorius.ninjamod.client;

import net.ivorius.ninjamod.CommonProxy;
import net.ivorius.ninjamod.EventHandlerSound;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerSounds()
	{
		MinecraftForge.EVENT_BUS.register(new EventHandlerSound());		
	}
}
