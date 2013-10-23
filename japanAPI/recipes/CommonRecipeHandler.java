package mods.japanAPI.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * 共通レシピハンドラ
 * @author ArabikiTouhu
 * @version 0.0.1
 */
public class CommonRecipeHandler {

	private CommonRecipeHandler() { }

	/**
	 * ShapedRecipe変換
	 * @param recipeFrom ShapedRecipe
	 * @return ShapedOreRecipe
	 */
	public static ShapedOreRecipe ConversionShapedRecipe(ShapedRecipes recipeFrom) {
		if(recipeFrom instanceof ShapedNotConversionResipe) return null;

		int width = recipeFrom.recipeWidth;
		int height = recipeFrom.recipeHeight;

		ItemStack[] input = recipeFrom.recipeItems;
		ItemStack output = recipeFrom.getRecipeOutput();

		String line = "";	//レシピ位置
		Map<ItemStack, String> array1 = new HashMap<ItemStack, String>();
		Map<ItemStack, String> array2 = new HashMap<ItemStack, String>();
		ArrayList<Object> arrayTmp = new ArrayList<Object>();
		int i = 0;
		for(ItemStack inputItem : input) {
			//空白の場合
			if(inputItem == null) {
				line += " ";
			} else {
				boolean flag = false;
				for(Entry<ItemStack, String> arg : array1.entrySet()) {
					if(arg.getKey().isItemEqual(inputItem)) {
						flag = true;
						line += arg.getValue();
						String name = null;
						for(Entry<ItemStack, String> entry : array2.entrySet()) {
							if(entry.getKey().isItemEqual(inputItem)) {
								name = entry.getValue();
								break;
							}
						}
						if(name == null || name.matches("Unknown")) {
							arrayTmp.add(arg.getValue().charAt(0));
							arrayTmp.add(inputItem.copy());
						}
						break;
					}
				}
				if(!flag) {
					line += String.valueOf(i);
					array1.put(inputItem.copy(), String.valueOf(i));
					String name = OreDictionary.getOreName(OreDictionary.getOreID(new ItemStack(inputItem.itemID, 1, 0)));
					arrayTmp.add(String.valueOf(i).charAt(0));
					if(name.matches("Unknown")) {
						arrayTmp.add(inputItem.copy());
					} else {
						arrayTmp.add(name);
						array2.put(inputItem.copy(), name);
					}
				}
			}
			i++;
		}
		arrayTmp.add(0, line);


		ShapedOreRecipe ret = new ShapedOreRecipe(output, arrayTmp.toArray(new Object[arrayTmp.size()]));

		return ret;
	}

}
