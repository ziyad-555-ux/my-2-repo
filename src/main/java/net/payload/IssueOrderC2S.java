package com.ziyad.militaryvillagers.net.payload;

import com.ziyad.militaryvillagers.systems.OrdersSystem;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record IssueOrderC2S(
        int generalVillagerId,
        String orderName,
        long paramA, long paramB, long paramC  // نستخدمها مرنة: UUID أو BlockPos
) implements CustomPacketPayload {

    public static final Type<IssueOrderC2S> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("militaryvillagers","issue_order"));
    public static final StreamCodec<ByteBuf, IssueOrderC2S> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, IssueOrderC2S::generalVillagerId,
                    ByteBufCodecs.STRING_UTF8, IssueOrderC2S::orderName,
                    ByteBufCodecs.LONG, IssueOrderC2S::paramA,
                    ByteBufCodecs.LONG, IssueOrderC2S::paramB,
                    ByteBufCodecs.LONG, IssueOrderC2S::paramC,
                    IssueOrderC2S::new
            );
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(IssueOrderC2S msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer sp = (ServerPlayer) ctx.player();
            ServerLevel level = sp.serverLevel();
            Entity e = level.getEntity(msg.generalVillagerId());
            if (!(e instanceof Villager general)) return;

            // اجلب كل الجنود حول الجنرال (نصف قطر 48 بلوك)
            List<Villager> soldiers = findNearbySoldiers(level, general.blockPosition(), 48);

            OrdersSystem.OrderType order = OrdersSystem.OrderType.valueOf(msg.orderName());
            Object param = switch (order) {
                case FOLLOW -> new UUID(msg.paramA(), msg.paramB()); // قائد = لاعب
                case DEFEND -> BlockPos.of(msg.paramA());            // مركز دفاع
                default -> null;
            };

            OrdersSystem.applyOrder(level, soldiers, order, param);
        });
    }

    private static List<Villager> findNearbySoldiers(ServerLevel lvl, BlockPos center, int r) {
        List<Villager> out = new ArrayList<>();
        for (Entity ent : lvl.getEntities(null, new net.minecraft.world.phys.AABB(center).inflate(r))) {
            if (ent instanceof Villager v) {
                var d = v.getData(com.ziyad.militaryvillagers.registry.ModAttachments.SOLDIER.get());
                if (d.isSoldier) out.add(v);
            }
        }
        return out;
    }
}
