package net.monkeyman42001.cannacraft.event;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.monkeyman42001.cannacraft.component.CannacraftDataComponents;
import net.monkeyman42001.cannacraft.component.Strain;
import net.monkeyman42001.cannacraft.item.CannacraftItems;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CannacraftCraftingEvents {
	@SubscribeEvent
	public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
		ItemStack result = event.getCrafting();
		Container inventory = event.getInventory();
		if (result.getItem() == CannacraftItems.JOINT.get()) {
			Map<String, Strain> uniqueStrains = new LinkedHashMap<>();
			for (int i = 0; i < inventory.getContainerSize(); i++) {
				ItemStack stack = inventory.getItem(i);
				if (stack.getItem() == CannacraftItems.NUG.get()
						|| stack.getItem() == CannacraftItems.GROUND_CANNABIS.get()) {
					Strain strain = stack.get(CannacraftDataComponents.STRAIN.get());
					if (strain != null && !strain.name().isBlank()) {
						uniqueStrains.putIfAbsent(strain.name(), strain);
					}
				}
			}

			if (!uniqueStrains.isEmpty()) {
				List<Strain> hodgepodge = new ArrayList<>(uniqueStrains.values());
				result.set(CannacraftDataComponents.HODGEPODGE.get(), hodgepodge);
				result.set(CannacraftDataComponents.STRAIN.get(), hodgepodge.get(0));
			}
			return;
		}

		if (result.getItem() == CannacraftItems.CANNABIS_SEED.get() && result.getCount() == 2) {
			ItemStack nug = ItemStack.EMPTY;
			int nugCount = 0;
			int otherCount = 0;
			for (int i = 0; i < inventory.getContainerSize(); i++) {
				ItemStack stack = inventory.getItem(i);
				if (stack.isEmpty()) {
					continue;
				}
				if (stack.getItem() == CannacraftItems.NUG.get()) {
					nug = stack;
					nugCount += stack.getCount();
				} else {
					otherCount += stack.getCount();
				}
			}
			if (nugCount == 1 && otherCount == 0 && !nug.isEmpty()) {
				var strain = nug.get(CannacraftDataComponents.STRAIN.get());
				if (strain != null && !strain.name().isBlank()) {
					result.set(CannacraftDataComponents.STRAIN.get(), strain);
				}
				var lineage = nug.get(CannacraftDataComponents.LINEAGE.get());
				if (lineage != null) {
					result.set(CannacraftDataComponents.LINEAGE.get(), lineage);
				}
			}
		}
	}
}
