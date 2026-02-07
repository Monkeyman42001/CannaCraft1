package net.monkeyman42001.cannacraft.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.monkeyman42001.cannacraft.CannaCraft;
import net.monkeyman42001.cannacraft.recipe.GrinderRecipe;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CannacraftRecipeSerializers {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
		DeferredRegister.create(Registries.RECIPE_SERIALIZER, CannaCraft.MOD_ID);

	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<GrinderRecipe>> GRINDER =
		RECIPE_SERIALIZERS.register("grinder", () -> new SimpleCraftingRecipeSerializer<>(GrinderRecipe::new));

	public static void register(IEventBus eventBus) {
		RECIPE_SERIALIZERS.register(eventBus);
	}
}
