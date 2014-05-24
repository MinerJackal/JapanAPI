package mods.japanAPI.recipes;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;


public class ItemStackOnlyRecipe implements Comparable<ItemStackOnlyRecipe>{

	protected final ArrayList<ItemStack> ingredientList = new ArrayList<ItemStack>();

	protected final ItemStack result;


	public ItemStackOnlyRecipe(ItemStack result, ItemStack... ingredientList) {
		this.result = result;

		this.ingredientList.clear();

		for(ItemStack itemStack : ingredientList)
			this.ingredientList.add(itemStack);
	}

	public ArrayList<ItemStack> getIngredientList() { return this.ingredientList; }
	public ItemStack getResult() { return this.result; }

	public boolean matches(ItemStack... itemStacks) {
		if(itemStacks == null) return false;
		if(itemStacks.length != this.ingredientList.size()) return false;

		boolean[] checks = new boolean[this.ingredientList.size()];
		ItemStack[] items = itemStacks.clone();
		for(int i = 0; i < this.ingredientList.size(); i++) {
			int j = 0;
			for(ItemStack item : items) {
				if(item != null && this.ingredientList.get(i).isItemEqual(item) && item.stackSize >= this.ingredientList.get(i).stackSize) {
					checks[i] = true;
					items[j] = null;
					break;
				}
				j++;
			}
			if(!checks[i]) return false;
		}

		return true;

	}
	@Override
	public int compareTo(ItemStackOnlyRecipe recipe) {
		ArrayList<ItemStack> recipeItems = recipe.getIngredientList();
		if(recipeItems.size() != this.ingredientList.size()) return -1;

		boolean[] checks = new boolean[this.ingredientList.size()];
		ItemStack[] items = ((ItemStack[]) recipeItems.toArray()).clone();
		for(int i = 0; i < this.ingredientList.size(); i++) {
			int j = 0;
			for(ItemStack item : items) {
				if(item != null && this.ingredientList.get(i).isItemEqual(item) && item.stackSize == this.ingredientList.get(i).stackSize) {
					checks[i] = true;
					items[j] = null;
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
		if (obj != null && obj instanceof ItemStackOnlyRecipe)
			return this.compareTo((ItemStackOnlyRecipe) obj) == 0;
		return false;
	}

	@Override
	public int hashCode() {

		int ret = 0;
		for(int i = 0; i < this.ingredientList.size(); i++) {
			if(ret == 0) {
				ret = this.ingredientList.get(i).itemID;
				if(this.ingredientList.get(i).isItemDamaged()) ret ^= this.ingredientList.get(i).getItemDamage();
			} else {
				ret ^= this.ingredientList.get(i).itemID;
				if(this.ingredientList.get(i).isItemDamaged()) ret ^= this.ingredientList.get(i).getItemDamage();
			}
		}
		return ret;
	}

}
