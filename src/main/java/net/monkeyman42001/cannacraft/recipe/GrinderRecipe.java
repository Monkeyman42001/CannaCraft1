package net.monkeyman42001.cannacraft.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.monkeyman42001.cannacraft.component.CannacraftDataComponents;
import net.monkeyman42001.cannacraft.component.LineageNode;
import net.monkeyman42001.cannacraft.component.Strain;
import net.monkeyman42001.cannacraft.item.CannacraftItems;
import net.monkeyman42001.cannacraft.registry.CannacraftRecipeSerializers;

public class GrinderRecipe extends CustomRecipe {
	public GrinderRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		boolean foundNug = false;
		boolean foundGrinder = false;
		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (stack.isEmpty()) {
				continue;
			}
			if (stack.getItem() == CannacraftItems.NUG.get()) {
				if (foundNug) {
					return false;
				}
				foundNug = true;
			} else if (stack.getItem() == CannacraftItems.GRINDER.get()) {
				if (foundGrinder) {
					return false;
				}
				foundGrinder = true;
			} else {
				return false;
			}
		}
		return foundNug && foundGrinder;
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		ItemStack nug = ItemStack.EMPTY;
		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (stack.getItem() == CannacraftItems.NUG.get()) {
				nug = stack;
				break;
			}
		}

		ItemStack result = new ItemStack(CannacraftItems.GROUND_CANNABIS.get(), 3);
		Strain strain = nug.get(CannacraftDataComponents.STRAIN.get());
		if (strain != null && !strain.name().isBlank()) {
			result.set(CannacraftDataComponents.STRAIN.get(), strain);
		}
		LineageNode lineage = nug.get(CannacraftDataComponents.LINEAGE.get());
		if (lineage != null) {
			result.set(CannacraftDataComponents.LINEAGE.get(), lineage);
		}
		return result;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
		NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);
		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (stack.getItem() == CannacraftItems.GRINDER.get()) {
				remaining.set(i, damageGrinder(stack));
			}
		}
		return remaining;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		return new ItemStack(CannacraftItems.GROUND_CANNABIS.get(), 3);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CannacraftRecipeSerializers.GRINDER.get();
	}

	private static ItemStack damageGrinder(ItemStack stack) {
		if (!stack.isDamageableItem()) {
			return ItemStack.EMPTY;
		}
		int nextDamage = stack.getDamageValue() + 1;
		if (nextDamage >= stack.getMaxDamage()) {
			return ItemStack.EMPTY;
		}
		ItemStack damaged = stack.copy();
		damaged.setDamageValue(nextDamage);
		return damaged;
	}
}
