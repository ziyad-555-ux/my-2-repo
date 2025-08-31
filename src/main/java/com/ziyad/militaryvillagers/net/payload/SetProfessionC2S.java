package com.ziyad.militaryvillagers.net.payload;

import com.ziyad.militaryvillagers.registry.ModAttachments;
import com.ziyad.militaryvillagers.data.SoldierData;
import com.ziyad.militaryvillagers.util.ProfessionUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetProfessionC2S(int villagerId, String choice) implements CustomPacketPayload {

    public static final Type<SetProfessionC2S> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("militaryvillagers", "set_prof"));

    public static final StreamCodec<ByteBuf, SetProfessionC2S> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, SetProfessionC2S::villagerId,
                    ByteBufCodecs.STRING_UTF8, SetProfessionC2S::choice,
                    SetProfessionC2S::new
            );

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(final SetProfessionC2S msg, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer sp = (ServerPlayer) ctx.player();
            ServerLevel level = sp.serverLevel();
            Entity e = level.getEntity(msg.villagerId());
            if (!(e instanceof Villager v)) return;

            String ch = msg.choice();
            if ("MILITARY".equals(ch)) {
                // فعل وضع العسكري
                SoldierData data = v.getData(ModAttachments.SOLDIER.get());
                data.isSoldier = true;
                data.rank = SoldierData.Rank.PRIVATE;
                v.syncData(ModAttachments.SOLDIER.get()); // مزامنة فورية للكلينت (اختياري)
                sp.sendSystemMessage(Component.literal("تم تحويل القروي لعسكري (Private)."));
                return;
            }

            // محاولة تغيير المهنة لمهنة فانلا
            VillagerProfession prof = VillagerProfession.valueOf(ch);
            boolean allowed = ProfessionUtil.isProfessionAllowedByNearbyPOI(v, prof, 32);
            if (allowed) {
                v.setVillagerData(v.getVillagerData().setProfession(prof));
                sp.sendSystemMessage(Component.literal("تم تغيير المهنة إلى: " + ch));
            } else {
                sp.sendSystemMessage(Component.literal("ما فيه محطة عمل قريبة لهالمهنة: " + ch));
            }
        });
    }
}
