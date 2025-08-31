package com.ziyad.militaryvillagers.events;

import com.ziyad.militaryvillagers.gui.menu.JobChangeMenu;
import com.ziyad.militaryvillagers.registry.ModMenus;
import com.ziyad.militaryvillagers.util.ProfessionUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public final class CommonEvents {

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract e) {
        if (e.getTarget() instanceof Villager villager && e.getSide().isServer()
                && e.getEntity().isShiftKeyDown() && e.getHand() == e.getEntity().getUsedItemHand()) {

            ServerPlayer sp = (ServerPlayer) e.getEntity();
            // افتح القائمة ومرر ID القروي للكلينت
            sp.openMenu(new SimpleMenuProvider(
                    (containerId, inv, player) -> new JobChangeMenu(containerId, inv, villager.getId()),
                    Component.translatable("menu.militaryvillagers.job_change")
            ), buf -> buf.writeVarInt(villager.getId())); // يقرأها الكلينت في كونستركتر الـMenu
            e.setCanceled(true);
        }
    }
}
