package mods.japanAPI.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 共通レシピハンドラ
 * @author ArabikiTouhu
 * @version 0.0.1
 */
public class CommonRecipeHandler
{

	private CommonRecipeHandler() { }

	/**
	 * ShapedRecipe変換
	 * @param recipeFrom ShapedRecipe
	 * @return ShapedOreRecipe
	 */
	public static ShapedOreRecipe ConversionShapedRecipe(ShapedRecipes recipeFrom)
    {
		if(recipeFrom instanceof ShapedNotConversionResipe)
            return null;

		boolean retFlag = false;
		int width = recipeFrom.recipeWidth;
		int height = recipeFrom.recipeHeight;

		ItemStack[] input = recipeFrom.recipeItems;
		ItemStack output = recipeFrom.getRecipeOutput();

		String line = "";	//レシピ位置
		Map<ItemStack, String> array1 = new HashMap<ItemStack, String>();
		Map<ItemStack, String> array2 = new HashMap<ItemStack, String>();
		ArrayList<Object> arrayTmp = new ArrayList<Object>();
		int i = 0;
		for(ItemStack inputItem : input)
        {
			//空白の場合
			if(inputItem == null)
            {
				line += " ";
			} else
            {
				boolean flag = false;
				for(Entry<ItemStack, String> arg : array1.entrySet())
                {
					if(arg.getKey().isItemEqual(inputItem))
                    {
						flag = true;
						line += arg.getValue();
						String name = null;
						for(Entry<ItemStack, String> entry : array2.entrySet())
                        {
							if(entry.getKey().isItemEqual(inputItem))
                            {
								name = entry.getValue();
								break;
							}
						}
						if(name == null || name.matches("Unknown"))
                        {
							arrayTmp.add(arg.getValue().charAt(0));
							arrayTmp.add(inputItem.copy());
						}
						break;
					}
				}
				if(!flag) {
					line += String.valueOf(i);
					array1.put(inputItem.copy(), String.valueOf(i));
					String name = OreDictionary.getOreName(OreDictionary.getOreID(new ItemStack(inputItem.itemID, 1, inputItem.getItemDamage())));
					arrayTmp.add(String.valueOf(i).charAt(0));
					if(name.matches("Unknown"))
                    {
						arrayTmp.add(inputItem.copy());
					} else
                    {
						arrayTmp.add(name);
						array2.put(inputItem.copy(), name);
						retFlag = true;
					}
				}
			}
			i++;
		}
		arrayTmp.add(0, line);

		if(!retFlag) return null;

		ShapedOreRecipe ret = new ShapedOreRecipe(output, arrayTmp.toArray(new Object[arrayTmp.size()]));

		return ret;
	}

	public static ShapedOreRecipe ConversionShapedRecipeV2(ShapedRecipes recipeFrom)
    {
		if(recipeFrom instanceof ShapedNotConversionResipe) return null;

		boolean retFlag = false;
		int width = recipeFrom.recipeWidth;
		int height = recipeFrom.recipeHeight;

		ItemStack[] input = recipeFrom.recipeItems;
		boolean[] flags = new boolean[recipeFrom.getRecipeSize()];
		ItemStack output = recipeFrom.getRecipeOutput();

		String line = "0123456789";	//レシピ位置
		ArrayList<Object> arrayTmp = new ArrayList<Object>();
		int i = 0;
		char replaceChar = 'a';
		char targetChar = '0';

		for(ItemStack inputItem : input)
        {
			if(inputItem == null)
            {
				line = line.replace(targetChar, ' ');
			} else if(!flags[i]) {	//空白以外でまだ探査していない部分
				flags[i] = true;
				char targetChar2 = targetChar;
				line = line.replace(targetChar2, replaceChar);

				if(inputItem.getItemDamage() == 32767)
                {
					inputItem.setItemDamage(-1);
					retFlag = true;
				}

				String name = OreDictionary.getOreName(OreDictionary.getOreID(inputItem));

				arrayTmp.add(replaceChar);

				if(name.matches("Unknown")) {	//該当なし
					arrayTmp.add(inputItem.copy());		//アイテムそのまま
				} else {	//該当あり
					arrayTmp.add(name);					//鉱石辞書登録名
				}

				targetChar2++;
				//反復処理
				for(int j = i + 1; j < input.length; j++, targetChar2++)
                {
					if(flags[j] || input[j] == null) continue;
					if(input[j].isItemEqual(inputItem))
                    {
						line = line.replace(targetChar2, replaceChar);
						flags[j] = true;
					} else if(!name.matches("Unknown"))
                    {
						if(input[j].getItemDamage() == 32767) input[j].setItemDamage(-1);
						if(name.matches(OreDictionary.getOreName(OreDictionary.getOreID(input[j])))) {
							line = line.replace(targetChar2, replaceChar);
							flags[j] = true;
						}
					}
				}
				replaceChar++;
			}
			i++;
			targetChar++;
		}

		arrayTmp.add(0, line.substring(0, 3));
		arrayTmp.add(1, line.substring(3, 6));
		arrayTmp.add(2, line.substring(6, 9));

		if(!retFlag) return null;

		ShapedOreRecipe ret = new ShapedOreRecipe(output, arrayTmp.toArray(new Object[arrayTmp.size()]));

		return ret;
	}
}
