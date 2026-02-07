package net.monkeyman42001.cannacraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.monkeyman42001.cannacraft.block.CannacraftModBlockEntities;
import net.monkeyman42001.cannacraft.component.LineageNode;
import net.monkeyman42001.cannacraft.component.Strain;

public class CannabisPlantBlockEntity extends BlockEntity {
	private Strain strain;
	private LineageNode lineage;

	public CannabisPlantBlockEntity(BlockPos pos, BlockState state) {
		super(CannacraftModBlockEntities.CANNABIS_PLANT.get(), pos, state);
	}

	public Strain getStrain() {
		return strain;
	}

	public void setStrain(Strain strain) {
		this.strain = strain;
		setChanged();
	}

	public LineageNode getLineage() {
		return lineage;
	}

	public void setLineage(LineageNode lineage) {
		this.lineage = lineage;
		setChanged();
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.saveAdditional(tag, provider);
		if (strain != null) {
			Strain.CODEC.encodeStart(NbtOps.INSTANCE, strain)
					.result()
					.ifPresent(value -> tag.put("strain", (Tag) value));
		}
		if (lineage != null) {
			LineageNode.CODEC.encodeStart(NbtOps.INSTANCE, lineage)
					.result()
					.ifPresent(value -> tag.put("lineage", (Tag) value));
		}
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.loadAdditional(tag, provider);
		if (tag.contains("strain")) {
			Tag strainTag = tag.get("strain");
			if (strainTag != null) {
				this.strain = Strain.CODEC.parse(NbtOps.INSTANCE, strainTag).result().orElse(null);
			}
		}
		if (tag.contains("lineage")) {
			Tag lineageTag = tag.get("lineage");
			if (lineageTag != null) {
				this.lineage = LineageNode.CODEC.parse(NbtOps.INSTANCE, lineageTag).result().orElse(null);
			}
		}
		if (this.strain != null) {
			return;
		}
		if (tag.contains("strain_name")) {
			String name = tag.getString("strain_name");
			float thc = tag.getFloat("strain_thc");
			float terpene = tag.getFloat("strain_terpene");
			int color = tag.contains("strain_color") ? tag.getInt("strain_color") : 0xFFFFFF;
			this.strain = Strain.fromLegacy(name, thc, terpene, color);
		} else {
			this.strain = null;
		}
	}
}
