package net.monkeyman42001.cannacraft.network;

import net.monkeyman42001.cannacraft.CannaCraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record GrowTentNamePayload(BlockPos pos, String name) implements CustomPacketPayload {
	public static final Type<GrowTentNamePayload> TYPE = new Type<>(
			ResourceLocation.fromNamespaceAndPath(CannaCraft.MOD_ID, "grow_tent_name")
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, GrowTentNamePayload> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, GrowTentNamePayload::pos,
			ByteBufCodecs.STRING_UTF8, GrowTentNamePayload::name,
			GrowTentNamePayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
