package mods.japanAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

import mods.japanAPI.events.EntityItemPickupEventHook;
import mods.japanAPI.items.AutoConversionSymbolItem;
import mods.japanAPI.recipes.CommonRecipeHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(
		modid = "JapanAPI",
		name = "JapanAPI 1.5.2",
		version = "0.0.3-alpha"
		)
@NetworkMod(
		clientSideRequired = true,
		serverSideRequired = true
		)
public class JapanAPI {

	public static Random RANDOM;

	private static boolean CONFIG_itemConversion;

	public static Item ITEM_autoConversionSymbol;

	public static EntityItemPickupEventHook EVENT_entityItemPickupEventHook;

	/**
	 * レシピ（クラフト）の削除
	 * @param itemStacks リザルトアイテムリスト
	 */
	public static void DeleteCraftingRecipe(ItemStack... itemStacks) {
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

	/**
	 * レシピ（精錬）の削除
	 * @param itemStacks リザルトアイテムリスト
	 */
	public static void DeleteSmeltingRecipe(ItemStack... itemStacks) {
		Map<List<Integer>, ItemStack> recipesMeta = FurnaceRecipes.smelting().getMetaSmeltingList();
		Map<Integer, ItemStack> recipes = FurnaceRecipes.smelting().getSmeltingList();

		for(ItemStack itemStack : itemStacks) {
			if(itemStack == null) continue;
			if(itemStack.isItemDamaged() && recipesMeta.containsKey(Arrays.asList(itemStack.itemID, itemStack.getItemDamage()))) {
					recipesMeta.remove(Arrays.asList(itemStack.itemID, itemStack.getItemDamage()));
			}
			if(!itemStack.isItemDamaged() && recipes.containsKey(itemStack.itemID)) {
				recipes.remove(itemStack.itemID);
			}
		}
	}

	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event) {
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		try
		{
			cfg.load();
			AutoConversionSymbolItem.itemID = cfg.getItem("AutoConversion Symbol", 12000).getInt();

		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e, "Error Message");

		} finally {
			cfg.save();
		}

		RANDOM = new Random(1154145);

		EVENT_entityItemPickupEventHook = new EntityItemPickupEventHook();
		MinecraftForge.EVENT_BUS.register(EVENT_entityItemPickupEventHook);

	}

	@Mod.Init
	public void Init(FMLInitializationEvent event) {
		ITEM_autoConversionSymbol = new AutoConversionSymbolItem(AutoConversionSymbolItem.itemID);
		GameRegistry.registerItem(ITEM_autoConversionSymbol, "AutoConversionSymbol");
		LanguageRegistry.addName(ITEM_autoConversionSymbol, "AutoConversion Symbol");
		LanguageRegistry.instance().addNameForObject(ITEM_autoConversionSymbol, "ja_JP",
				"\u81EA\u52D5\u5909\u63DB\u30B7\u30F3\u30DC\u30EB");

	}

	@Mod.PostInit
	public void postInit(FMLPostInitializationEvent event) {
		CraftRecipeConversion();



	}

	public static void CraftRecipeConversion() {
		ArrayList<ShapedOreRecipe> oreRecipe = new ArrayList<ShapedOreRecipe>();
		ArrayList<ItemStack> remItemStack = new ArrayList<ItemStack>();

		List recipes = CraftingManager.getInstance().getRecipeList();
		for(Iterator i = recipes.listIterator(); i.hasNext();) {
			IRecipe recipe = (IRecipe)i.next();
			if(recipe instanceof ShapedRecipes) {
				ShapedOreRecipe convRecipe = CommonRecipeHandler.ConversionShapedRecipeV2((ShapedRecipes)recipe);
				if(convRecipe != null) {
					oreRecipe.add(convRecipe);
					remItemStack.add(recipe.getRecipeOutput());
				}
			}
		}

//		DeleteCraftingRecipe(remItemStack.toArray(new ItemStack[remItemStack.size()]));

		for(ShapedOreRecipe recipe : oreRecipe.toArray(new ShapedOreRecipe[oreRecipe.size()])) {
			GameRegistry.addRecipe(recipe);
		}
	}
}
