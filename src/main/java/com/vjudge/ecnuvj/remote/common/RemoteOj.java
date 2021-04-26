package com.vjudge.ecnuvj.remote.common;


import java.util.HashMap;
import java.util.Map;

/**
 * @author 14861
 */

public enum RemoteOj {
    POJ(1),
    HDU(2),
    ACdream(3),
    Aizu(4),
    CFGym(5),
    CodeForces(6),
    CSU(7),
    FZU(8),
    HUST(9),
    HYSBZ(10),
    LightOJ(11),
    NBUT(12),
    SCU(13),
    SGU(14),
    SPOJ(15),
    Tyvj(16),
    UESTC(17),
    UESTCOld(18),
    URAL(19),
    UVA(20),
    UVALive(21),
    ZOJ(22),
    ZTrening(23),
    LOCAL(24),
    JSK(25),
    MXT(26),
    TKOJ(27),
    ECN(28),
    ;
    private int code;

    private static final Map<Integer, RemoteOj> MAP = new HashMap<>();

    static {
        for (RemoteOj item : RemoteOj.values()) {
            MAP.put(item.code, item);
        }
    }

    RemoteOj(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static RemoteOj codeValueOf(int code) {
        return MAP.get(code);
    }
}
