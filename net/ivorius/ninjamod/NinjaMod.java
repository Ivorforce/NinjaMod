package net.ivorius.ninjamod;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "MiniIvNinjaMod", name = "Mini: Shinobi Mod", version = "1.0")
@NetworkMod(clientSideRequired = true)
public class NinjaMod
{
	@Instance(value = "MiniIvNinjaMod")
	public static NinjaMod instance;

	public static Item itemNinjaSword;

	public static Item itemNinjaCloth;

	public static Item itemNinjaHood;
	public static Item itemNinjaTunic;
	public static Item itemNinjaLeggings;
	public static Item itemNinjaBoots;

	@SidedProxy(clientSide = "net.ivorius.ninjamod.client.ClientProxy", serverSide = "net.ivorius.ninjamod.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit( FMLPreInitializationEvent event )
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		
		config.load();
		
		int ninjaSwordID = config.getItem("ninjaSwordID", 12100).getInt();
		int ninjaHoodID = config.getItem("ninjaHoodID", 12101).getInt();
		int ninjaTunicID = config.getItem("ninjaTunicID", 12102).getInt();
		int ninjaLeggingsID = config.getItem("ninjaLeggingsID", 12103).getInt();
		int ninjaBootsID = config.getItem("ninjaBootsID", 12104).getInt();
		int ninjaClothID = config.getItem("ninjaClothID", 12105).getInt();
		
		itemNinjaSword = (new ItemNinjaSword(ninjaSwordID, EnumToolMaterial.IRON)).setUnlocalizedName("swordNinjaIron").setTextureName("ninjamod:swordNinjaIron");
		
		itemNinjaCloth = new Item(ninjaClothID).setUnlocalizedName("ninjaCloth").setTextureName("ninjamod:ninjaCloth").setCreativeTab(CreativeTabs.tabMaterials);
		
		itemNinjaHood = (ItemArmor)(new ItemNinjaArmor(ninjaHoodID, EnumArmorMaterial.CHAIN, 0, 0)).setUnlocalizedName("hoodNinja").setTextureName("ninjamod:hoodNinja");
		itemNinjaTunic= (ItemArmor)(new ItemNinjaArmor(ninjaTunicID, EnumArmorMaterial.CHAIN, 0, 1)).setUnlocalizedName("tunicNinja").setTextureName("ninjamod:tunicNinja");
		itemNinjaLeggings = (ItemArmor)(new ItemNinjaArmor(ninjaLeggingsID, EnumArmorMaterial.CHAIN, 0, 2)).setUnlocalizedName("leggingsNinja").setTextureName("ninjamod:leggingsNinja");
		itemNinjaBoots = (ItemArmor)(new ItemNinjaArmor(ninjaBootsID, EnumArmorMaterial.CHAIN, 0, 3)).setUnlocalizedName("shoesNinja").setTextureName("ninjamod:bootsNinja");
				
		config.save();
	}

	@EventHandler
	public void load( FMLInitializationEvent event )
	{
		proxy.registerSounds();
		
        LanguageRegistry.addName(itemNinjaSword, "Uchigatana");
        
        LanguageRegistry.addName(itemNinjaCloth, "Sentiomorph Cloth");
        
        LanguageRegistry.addName(itemNinjaHood, "Sentiomorph Hood");
        LanguageRegistry.addName(itemNinjaTunic, "Sentiomorph Tunic");
        LanguageRegistry.addName(itemNinjaLeggings, "Sentiomorph Leggings");
        LanguageRegistry.addName(itemNinjaBoots, "Sentiomorph Boots");
        
        GameRegistry.addShapelessRecipe(new ItemStack(itemNinjaCloth, 6), new Object[] {
        	new ItemStack(Block.cloth, 1, 15), new ItemStack(Block.cloth, 1, 15), new ItemStack(Block.cloth, 1, 15), new ItemStack(Block.cloth, 1, 15), new ItemStack(Block.cloth, 1, 15), new ItemStack(Block.cloth, 1, 15), Item.fermentedSpiderEye, Item.goldenCarrot, Item.netherStalkSeeds
        });
        
        GameRegistry.addRecipe(new ItemStack(itemNinjaSword), new Object[] {
        	" IC",
        	"EIW",
        	"SiF",
        	'I', Block.blockIron, 'C', Item.goldenCarrot, 'E', Item.enderPearl, 'W', Item.netherStalkSeeds, 'S', itemNinjaCloth, 'i', Item.stick, 'F', Item.fermentedSpiderEye
        });
        GameRegistry.addRecipe(new ItemStack(itemNinjaHood), new Object[] {
        	"#I#",
        	"# #",
        	"   ",
        	'#', itemNinjaCloth, 'I', Item.ingotIron
        });
        GameRegistry.addRecipe(new ItemStack(itemNinjaTunic), new Object[] {
        	"# #",
        	"I#I",
        	"###",
        	'#', itemNinjaCloth, 'I', Item.ingotIron
        });
        GameRegistry.addRecipe(new ItemStack(itemNinjaLeggings), new Object[] {
        	"###",
        	"I I",
        	"# #",
        	'#', itemNinjaCloth, 'I', Item.ingotIron
        });
        GameRegistry.addRecipe(new ItemStack(itemNinjaBoots), new Object[] {
        	"   ",
        	"# #",
        	"# #",
        	'#', itemNinjaCloth, 'I', Item.ingotIron
        });
	}

	@EventHandler
	public void postInit( FMLPostInitializationEvent event )
	{
		
	}
}
