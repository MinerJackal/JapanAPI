package mods.sample.blocks;

import java.util.ArrayList;

import mods.sample.Sample;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SampleBlock extends Block {

	public static int blockID;

	public SampleBlock(int id) {
		super(blockID + id, Material.rock);
		setStepSound(soundStoneFootstep);
		setUnlocalizedName("Sample:sample" + id);
		setCreativeTab(Sample.TABS_sample);
	}

	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		return ret;
	}

}
