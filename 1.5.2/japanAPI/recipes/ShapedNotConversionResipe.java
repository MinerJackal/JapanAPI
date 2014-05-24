package mods.japanAPI.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

/**
 * ShapedRecipesを鉱石辞書使用に自動変換しないクラス
 * @author ArabikiTouhu
 * @version 0.0.1
 */
public class ShapedNotConversionResipe extends ShapedRecipes {

	public ShapedNotConversionResipe(int width, int height, ItemStack[] recipeItems, ItemStack recipeOutput) {
		super(width, height, recipeItems, recipeOutput);
	}

}
