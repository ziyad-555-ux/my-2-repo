package com.ziyad.militaryvillagers.registry;

import com.ziyad.militaryvillagers.MilitaryVillagersMod;
import com.ziyad.militaryvillagers.data.SoldierData;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MilitaryVillagersMod.MODID);

    // نخزّن SoldierData على الكيانات (Villager). نستعمل MapCodec/Codec للتخزين + مزامنة عند الحاجة.
    public static final Supplier<AttachmentType<SoldierData>> SOLDIER =
            ATTACHMENT_TYPES.register("soldier",
                    () -> AttachmentType.builder(SoldierData::new)
                            .serialize(SoldierData.CODEC.fieldOf("soldier")) // حفظ NBT
                            .sync(SoldierData.STREAM_CODEC)                  // مزامنة للشبكة
                            .build()
            );
}
