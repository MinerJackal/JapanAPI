package mods.japanAPI.recipes;

import net.minecraftforge.liquids.LiquidStack;

import java.util.ArrayList;


public class LiquidStackOnlyRecipe implements Comparable<LiquidStackOnlyRecipe>
{
	protected final ArrayList<LiquidStack> ingredientList = new ArrayList<LiquidStack>();

	protected final LiquidStack result;


	public LiquidStackOnlyRecipe(LiquidStack result, LiquidStack... ingredientList)
    {
		this.result = result;

		this.ingredientList.clear();

		for(LiquidStack liquidStack : ingredientList)
			this.ingredientList.add(liquidStack);
	}

	public ArrayList<LiquidStack> getIngredientList() { return this.ingredientList; }
	public LiquidStack getResult() { return this.result; }

	public boolean matches(LiquidStack... liquidStacks)
    {
		if(liquidStacks == null) return false;
		if(liquidStacks.length != this.ingredientList.size()) return false;

		boolean[] checks = new boolean[this.ingredientList.size()];
		LiquidStack[] liquids = liquidStacks.clone();
		for(int i = 0; i < this.ingredientList.size(); i++)
        {
			int j = 0;
			for(LiquidStack liquid : liquids)
            {
				if(liquid != null && this.ingredientList.get(i).isLiquidEqual(liquid) && liquid.amount == this.ingredientList.get(i).amount)
                {
					checks[i] = true;
					liquids[j] = null;
					break;
				}
				j++;
			}
			if(!checks[i]) return false;
		}

		return true;

	}
	@Override
	public int compareTo(LiquidStackOnlyRecipe recipe)
    {
		ArrayList<LiquidStack> recipeLiquids = recipe.getIngredientList();
		if(recipeLiquids.size() != this.ingredientList.size()) return -1;

		boolean[] checks = new boolean[this.ingredientList.size()];
		LiquidStack[] liquids = ((LiquidStack[]) recipeLiquids.toArray()).clone();
		for(int i = 0; i < this.ingredientList.size(); i++)
        {
			int j = 0;
			for(LiquidStack liquid : liquids)
            {
				if(liquid != null && this.ingredientList.get(i).isLiquidEqual(liquid) && liquid.amount == this.ingredientList.get(i).amount)
                {
					checks[i] = true;
					liquids[j] = null;
					break;
				}
				j++;
			}
			if(!checks[i]) return -1;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof LiquidStackOnlyRecipe)
			return this.compareTo((LiquidStackOnlyRecipe) obj) == 0;
		return false;
	}

	@Override
	public int hashCode() {

		int ret = 0;
		for(int i = 0; i < this.ingredientList.size(); i++) {
			if(ret == 0) {
				ret = this.ingredientList.get(i).itemID ^ this.ingredientList.get(i).itemMeta;
			} else {
				ret ^= this.ingredientList.get(i).itemID ^ this.ingredientList.get(i).itemMeta;
			}
		}
		return ret;
	}

}
