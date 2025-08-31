package com.ziyad.militaryvillagers.net;

import com.ziyad.militaryvillagers.net.payload.SetProfessionC2S;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class NetworkPayloads {
    public static void register(IEventBus modBus) {
        modBus.addListener(NetworkPayloads::onRegister);
    }

    private static void onRegister(RegisterPayloadHandlersEvent e) {
        PayloadRegistrar registrar = e.registrar("1"); // رقم نسخة الشبكة
        registrar.playToServer(IssueOrderC2S.TYPE, IssueOrderC2S.STREAM_CODEC, IssueOrderC2S::handle);
        registrar.playToServer(SetProfessionC2S.TYPE, SetProfessionC2S.STREAM_CODEC, SetProfessionC2S::handle);
    }
}
