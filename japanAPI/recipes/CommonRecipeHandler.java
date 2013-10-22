package mods.japanAPI.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;


public class CommonRecipeHandler {

	public static ShapedOreRecipe ConversionShapedRecipe(ShapedRecipes recipeFrom) {
		int width = recipeFrom.recipeWidth;
		int height = recipeFrom.recipeHeight;
		
		ItemStack[] input = recipeFrom.recipeItems;
		ItemStack output = recipeFrom.getRecipeOutput();
		
		
		
		return null;
	}

}
