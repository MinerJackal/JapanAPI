package mods.sample;

import java.util.Random;
import java.util.logging.Level;

import mods.sample.blocks.SampleBlock;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(
		modid = "Sample",
		name = "Sample 1.5.2",
		version = "0.0.2")

@NetworkMod(
		clientSideRequired = true,
		serverSideRequired = true)
public class Sample {

	@Instance("Sample")
	public static Sample instance;

	@SidedProxy(clientSide = "mods.sample.ClientProxy", serverSide = "mods.sample.CommonProxy")
	public static CommonProxy proxy;

	public static final CreativeTabs TABS_sample = new CreativeTab("Sample");

	public static Random RANDOM = new Random(152001);

	public static Block[] BLOCK_Sample = new Block[4];

	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event) {
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		try
		{
			cfg.load();
			SampleBlock.blockID[0] = cfg.getBlock("Sample 0", 4040).getInt();
			SampleBlock.blockID[1] = cfg.getBlock("Sample 1", 4041).getInt();
			SampleBlock.blockID[2] = cfg.getBlock("Sample 2", 4042).getInt();
			SampleBlock.blockID[3] = cfg.getBlock("Sample 3", 4043).getInt();

		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e, "Error Message");

		} finally {
			cfg.save();
		}
	}

	@Mod.Init
	public void Init(FMLInitializationEvent event) {
		for(int i = 0; i < 4; i++) {
			BLOCK_Sample[i] = new SampleBlock(i);
			GameRegistry.registerBlock(BLOCK_Sample[i], BLOCK_Sample[i].getUnlocalizedName());
			LanguageRegistry.addName(BLOCK_Sample[i], "Sample " + i);
			OreDictionary.registerOre("oreIron", BLOCK_Sample[i]);
		}

		GameRegistry.addShapedRecipe(new ItemStack(Item.appleRed, 32),
				new Object[] { "AAA", "B  ", "CDE",
			Character.valueOf('A'), BLOCK_Sample[0],
			Character.valueOf('B'), Item.potato,
			Character.valueOf('C'), Item.appleRed,
			Character.valueOf('D'), Item.stick,
			Character.valueOf('E'), Item.bone
		});

//		JapanAPI.CraftRecipeConversion();
	}

	@Mod.PostInit
	public void postInit(FMLPostInitializationEvent event) {

	}

}
