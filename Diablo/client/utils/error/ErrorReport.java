/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.diablo.client.utils.error;

public class ErrorReport {
    public /* enum */ int l;

    public static void I(Throwable throwable, String string) {
        System.out.println("\u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430 \u0432 " + string + ". \u0422\u0438\u043f \u043e\u0448\u0438\u0431\u043a\u0438: " + throwable.getClass().getSimpleName() + "\n\u041f\u0440\u0438\u0447\u0438\u043d\u0430 \u043e\u0448\u0438\u0431\u043a\u0438: " + throwable.getMessage());
    }
}

