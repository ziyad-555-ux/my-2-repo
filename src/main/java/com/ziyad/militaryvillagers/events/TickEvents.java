package com.ziyad.militaryvillagers.events;

import com.ziyad.militaryvillagers.systems.FoodSystem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber
public final class TickEvents {
    private static final int DAY_TICKS = 24000;
    private static int acc = 0;

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post e) {
        if (e.getLevel().isClientSide()) return;
        acc++;
        if (acc >= DAY_TICKS) {
            acc = 0;
            FoodSystem.endOfDay((net.minecraft.server.level.ServerLevel) e.getLevel());
        }
    }
}
