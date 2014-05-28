package mods.japanAPI.items;

import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.japanAPI.JapanAPI;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class JAPI_ItemCrafingTool extends Item implements ICraftingHandler
{

    protected int toolType;
    protected String name;
    protected boolean repair;


    public JAPI_ItemCrafingTool(int par1,Types types,int type)
    {
        super(par1 -256);
        setMaxDamage(types.maxUse);
        this.toolType = type;
        this.name = types.name();
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack par1ItemStack)
    {
        return false;
    }

    @Override
    public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
    {
        repair = this.itemID == item.itemID;
    }

    @Override
    public boolean hasContainerItem()
    {
        return !repair;
    }

    @Override
    public ItemStack getContainerItemStack(ItemStack itemStack)
    {
        EntityPlayer player = null;
        if (itemStack != null && itemStack.itemID == this.itemID)
        {
            if(itemStack.getItemDamage() > itemStack.getMaxDamage())
            {
                player.playSound("random.break",1,0.9F + JapanAPI.RANDOM.nextFloat() * 0.2F);
            }else
            itemStack.setItemDamage(itemStack.getItemDamage() +1);
            player.playSound("random.anvil_use",1,0.9F + JapanAPI.RANDOM.nextFloat() * 0.2F);
        }
        return itemStack;
    }



    @Override
    public void onSmelting(EntityPlayer player, ItemStack item)
    {
        return;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon("additonalOre:" + getUnlocalizedName());
    }


    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage()
    {
        return itemIcon;
    }

    public enum Types
    {
        Wood(16),
        Stone(32),
        Iron(128),
        Bronze(256),
        Steel(512),
        TungstenSteel(1024),
        Titanium(2048)
        ;
        public int maxUse;

        private Types(int maxUse)
        {
            this.maxUse = maxUse;
        }
    }
}
