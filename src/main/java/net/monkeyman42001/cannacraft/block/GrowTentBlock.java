package net.monkeyman42001.cannacraft.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

public class GrowTentBlock extends Block {
	public GrowTentBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	//@Override
	public int getLightBlock(BlockState state) {
		return 15;
	}
}