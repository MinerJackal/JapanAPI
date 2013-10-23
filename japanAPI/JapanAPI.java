package mods.japanAPI;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import mods.japanAPI.events.EntityItemPickupEventHook;
import mods.japanAPI.items.AutoConversionSymbolItem;
import mods.japanAPI.recipes.CommonRecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(
		modid = "JapanAPI",
		name = "JapanAPI 1.5.2",
		version = "0.0.2-alpha"
		)
@NetworkMod(
		clientSideRequired = true,
		serverSideRequired = true
		)
public class JapanAPI {

	private static boolean CONFIG_itemConversion;

	public static Item ITEM_autoConversionSymbol;

	public static void DeleteRecipe(ItemStack... itemStacks) {
		List recipes = CraftingManager.getInstance().getRecipeList();

		for(ItemStack itemStack : itemStacks) {
			if(itemStack == null) continue;
			for(Iterator i = recipes.listIterator(); i.hasNext();) {
				IRecipe recipe = (IRecipe)i.next();
				ItemStack is = recipe.getRecipeOutput();

				if(is != null && is.isItemEqual(itemStack)) {
					i.remove();
				}
			}
		}
	}

	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event) {
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		try
		{
			cfg.load();
//			CONFIG_itemConversion = cfg.get("Conversion", "Ores", true).getBoolean(true);	//鉱石変換

			AutoConversionSymbolItem.itemID = cfg.getItem("AutoConversion Symbol", 12000).getInt();

		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e, "Error Message");

		} finally {
			cfg.save();
		}

		MinecraftForge.EVENT_BUS.register(new EntityItemPickupEventHook());

	}

	@Mod.Init
	public void Init(FMLInitializationEvent event) {
		ITEM_autoConversionSymbol = new AutoConversionSymbolItem(AutoConversionSymbolItem.itemID);
		GameRegistry.registerItem(ITEM_autoConversionSymbol, "AutoConversionSymbol");
		LanguageRegistry.addName(ITEM_autoConversionSymbol, "AutoConversion Symbol");
		LanguageRegistry.instance().addNameForObject(ITEM_autoConversionSymbol, "ja_JP",
				"\u81EA\u52D5\u5909\u63DB\u30B7\u30F3\u30DC\u30EB");

		GameRegistry.addRecipe(new ItemStack(Block.blocksList[Block.furnaceIdle.blockID], 1),
		new Object[] { "ADA", " C ", "B B",
				Character.valueOf('A'), Block.blocksList[Block.oreIron.blockID],
				Character.valueOf('B'), Block.blocksList[Block.oreGold.blockID],
				Character.valueOf('C'), Item.stick,
				Character.valueOf('C'), Item.appleRed});
		ShapedRecipes recipe = (ShapedRecipes) CraftingManager.getInstance().getRecipeList().get(CraftingManager.getInstance().getRecipeList().size() - 1);
		ShapedOreRecipe recipe2 = CommonRecipeHandler.ConversionShapedRecipe(recipe);
		GameRegistry.addRecipe(recipe2);

	}


}
