package net.monkeyman42001.cannacraft.villager;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import net.monkeyman42001.cannacraft.item.CannacraftItems;
import net.monkeyman42001.cannacraft.item.CannabisSeedItem;
import net.monkeyman42001.cannacraft.component.Strain;

import java.util.List;

public class CannacraftModTrades {
    private record TradeConfig(
            ItemCost cost,
            int maxUses,
            int villagerXp,
            float priceMultiplier,
            int level
    ) {}

    private static final TradeConfig[] STRAIN_TRADE_CONFIGS = new TradeConfig[] {
            new TradeConfig(new ItemCost(Items.EMERALD, 1), 16, 2, 0.05F, 1),
            new TradeConfig(new ItemCost(Items.WHEAT, 1), 16, 2, 0.05F, 1),
            new TradeConfig(new ItemCost(Items.DIAMOND, 1), 12, 5, 0.10F, 1),
            new TradeConfig(new ItemCost(Items.EMERALD, 2), 12, 5, 0.08F, 2),
            new TradeConfig(new ItemCost(Items.EMERALD, 3), 12, 5, 0.08F, 2),
            new TradeConfig(new ItemCost(Items.EMERALD, 4), 10, 8, 0.10F, 3),
            new TradeConfig(new ItemCost(Items.EMERALD, 3), 10, 8, 0.10F, 3)
    };

    private static VillagerTrades.ItemListing buildStrainTrade(Strain strain, TradeConfig config) {
        return (trader, random) -> {
            ItemStack result = new ItemStack(CannacraftItems.CANNABIS_SEED.get(), 1);
            CannabisSeedItem.setStrain(result, strain);

            return new MerchantOffer(
                    config.cost(),
                    result,
                    config.maxUses(),
                    config.villagerXp(),
                    config.priceMultiplier()
            );
        };
    }
	@SubscribeEvent
    public static void addTrades(VillagerTradesEvent event) {
        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

        Strain[] strains = Strain.DEFAULT_STRAINS;
        if (strains.length != STRAIN_TRADE_CONFIGS.length) {
            throw new IllegalStateException("Strain list and trade config list must match length.");
        }
        for (int i = 0; i < strains.length; i++) {
            TradeConfig config = STRAIN_TRADE_CONFIGS[i];
            trades.get(config.level()).add(buildStrainTrade(strains[i], config));
        }
    }
}
