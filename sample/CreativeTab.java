package mods.sample;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTab extends CreativeTabs {

	String type;

	public CreativeTab(String type) {
		super(type);
		this.type = type;
	}

	@Override
	@SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
		return new ItemStack(Item.cookie);
    }

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel() { return type; }
}