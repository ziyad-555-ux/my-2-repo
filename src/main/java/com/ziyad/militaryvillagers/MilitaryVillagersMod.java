package com.ziyad.militaryvillagers;

import com.ziyad.militaryvillagers.registry.ModMenus;
import com.ziyad.militaryvillagers.registry.ModAttachments;
import com.ziyad.militaryvillagers.net.NetworkPayloads;
// استورد ما تحتاجه من IEventBus/NeoForge APIs
import net.neoforged.fml.common.Mod;
// لو كنت على 1.21.6 خذ الـmod bus من الكونستركتر (انظر التعليق)
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.bus.api.IEventBus;

@Mod(MilitaryVillagersMod.MODID)
public final class MilitaryVillagersMod {
    public static final String MODID = "militaryvillagers";

    // إن كنت على 1.21.6، يُمرَّر لك سياق التحميل في الكونستركتر:
    public MilitaryVillagersMod(/* FMLJavaModLoadingContext context */) {
        // في بعض قوالب الـMDK الحديثة:
        // IEventBus modBus = context.getModBusGroup().bus();
        // هنا نستخدم الطريقة المألوفة للبساطة، وعدّلها لو مشروعك يرمي تحذير deprecation:
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModMenus.REGISTER.register(modBus);
        ModAttachments.ATTACHMENT_TYPES.register(modBus);
        NetworkPayloads.register(modBus); // تسجيل الباكيتات (RegisterPayloadHandlersEvent)
    }
}
