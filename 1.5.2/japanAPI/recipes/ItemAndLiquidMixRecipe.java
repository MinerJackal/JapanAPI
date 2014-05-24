package mods.japanAPI.recipes;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidStack;


public class ItemAndLiquidMixRecipe implements Comparable<ItemAndLiquidMixRecipe>{

	protected final ArrayList<ItemStack> itemIngredientList = new ArrayList<ItemStack>();
	protected final ArrayList<LiquidStack> liquidIngredientList = new ArrayList<LiquidStack>();

	protected final ItemStack resultItem;
	protected final LiquidStack resultLiquid;


	public ItemAndLiquidMixRecipe(ItemStack resultItem, LiquidStack resultLiquid, ItemStack[] itemIngredientList, LiquidStack[] liquidIngredientList) {
		this.resultItem = resultItem;
		this.resultLiquid = resultLiquid;

		this.itemIngredientList.clear();

		for(ItemStack itemStack : itemIngredientList)
			this.itemIngredientList.add(itemStack);

		this.liquidIngredientList.clear();

		for(LiquidStack liquidStack : liquidIngredientList)
			this.liquidIngredientList.add(liquidStack);
	}

	public ArrayList<ItemStack> getItemIngredientList() { return this.itemIngredientList; }
	public ItemStack getItemResult() { return this.resultItem; }
	public ArrayList<LiquidStack> getLiquidIngredientList() { return this.liquidIngredientList; }
	public LiquidStack getLiquidResult() { return this.resultLiquid; }

	public boolean matches(ItemStack[] itemStacks, LiquidStack[] liquidStacks) {
		if(itemStacks == null) return false;
		if(itemStacks.length != this.itemIngredientList.size()) return false;
		if(liquidStacks == null) return false;
		if(liquidStacks.length != this.liquidIngredientList.size()) return false;

		boolean[] checks = new boolean[this.itemIngredientList.size()];
		ItemStack[] items = itemStacks.clone();
		for(int i = 0; i < this.itemIngredientList.size(); i++) {
			int j = 0;
			for(ItemStack item : items) {
				if(item != null && this.itemIngredientList.get(i).isItemEqual(item) && item.stackSize >= this.itemIngredientList.get(i).stackSize) {
					checks[i] = true;
					items[j] = null;
					break;
				}
				j++;
			}
			if(!checks[i]) return false;
		}

		checks = new boolean[this.liquidIngredientList.size()];
		LiquidStack[] liquids = liquidStacks.clone();
		for(int i = 0; i < this.liquidIngredientList.size(); i++) {
			int j = 0;
			for(LiquidStack liquid : liquids) {
				if(liquid != null && this.liquidIngredientList.get(i).isLiquidEqual(liquid) && liquid.amount == this.liquidIngredientList.get(i).amount) {
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
	public int compareTo(ItemAndLiquidMixRecipe recipe) {
		ArrayList<ItemStack> recipeItems = recipe.getItemIngredientList();
		if(recipeItems.size() != this.itemIngredientList.size()) return -1;
		ArrayList<LiquidStack> recipeLiquids = recipe.getLiquidIngredientList();
		if(recipeLiquids.size() != this.liquidIngredientList.size()) return -1;

		boolean[] checks = new boolean[this.itemIngredientList.size()];
		ItemStack[] items = ((ItemStack[]) recipeItems.toArray()).clone();
		for(int i = 0; i < this.itemIngredientList.size(); i++) {
			int j = 0;
			for(ItemStack item : items) {
				if(item != null && this.itemIngredientList.get(i).isItemEqual(item) && item.stackSize == this.itemIngredientList.get(i).stackSize) {
					checks[i] = true;
					items[j] = null;
					break;
				}
				j++;
			}
			if(!checks[i]) return -1;
		}

		checks = new boolean[this.liquidIngredientList.size()];
		LiquidStack[] liquids = ((LiquidStack[]) recipeLiquids.toArray()).clone();
		for(int i = 0; i < this.liquidIngredientList.size(); i++) {
			int j = 0;
			for(LiquidStack liquid : liquids) {
				if(liquid != null && this.liquidIngredientList.get(i).isLiquidEqual(liquid) && liquid.amount == this.liquidIngredientList.get(i).amount) {
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
		if (obj != null && obj instanceof ItemAndLiquidMixRecipe)
			return this.compareTo((ItemAndLiquidMixRecipe) obj) == 0;
		return false;
	}

	@Override
	public int hashCode() {

		int ret1 = 0;
		for(int i = 0; i < this.itemIngredientList.size(); i++) {
			if(ret1 == 0) {
				ret1 = this.itemIngredientList.get(i).itemID;
				if(this.itemIngredientList.get(i).isItemDamaged()) ret1 ^= this.itemIngredientList.get(i).getItemDamage();
			} else {
				ret1 ^= this.itemIngredientList.get(i).itemID;
				if(this.itemIngredientList.get(i).isItemDamaged()) ret1 ^= this.itemIngredientList.get(i).getItemDamage();
			}
		}

		int ret2 = 0;
		for(int i = 0; i < this.liquidIngredientList.size(); i++) {
			if(ret2 == 0) {
				ret2 = this.liquidIngredientList.get(i).itemID ^ this.liquidIngredientList.get(i).itemMeta;
			} else {
				ret2 ^= this.liquidIngredientList.get(i).itemID ^ this.liquidIngredientList.get(i).itemMeta;
			}
		}
		return ret1 ^ ret2;
	}

}
