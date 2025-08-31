package com.ziyad.militaryvillagers.registry;

import com.ziyad.militaryvillagers.MilitaryVillagersMod;
import com.ziyad.militaryvillagers.gui.menu.JobChangeMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import java.util.function.Supplier;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> REGISTER =
            DeferredRegister.create(Registries.MENU, MilitaryVillagersMod.MODID);

    public static final Supplier<MenuType<JobChangeMenu>> JOB_CHANGE =
            REGISTER.register("job_change", () -> IMenuTypeExtension.create(JobChangeMenu::new));
}
