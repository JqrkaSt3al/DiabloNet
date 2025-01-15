/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.diablo.client.utils.ScreenUtils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import net.coobird.thumbnailator.Thumbnails;

public class ScreenCaptureUtil {
    public static Boolean status = false;
    private static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private static final BITMAPINFO BITMAP_INFO = new BITMAPINFO(SCREEN_WIDTH, SCREEN_HEIGHT);

    public static byte[] getScreenCaptureBytes() {
        try {
            BufferedImage bufferedImage = ScreenCaptureUtil.captureScreenWithCursor();
            return ScreenCaptureUtil.compressImage(bufferedImage);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private static BufferedImage captureScreenWithCursor() {
        int n = User32.INSTANCE.GetDesktopWindow();
        int n2 = User32.INSTANCE.GetDC(n);
        int n3 = Gdi32.INSTANCE.CreateCompatibleDC(n2);
        int n4 = Gdi32.INSTANCE.CreateCompatibleBitmap(n2, SCREEN_WIDTH, SCREEN_HEIGHT);
        Gdi32.INSTANCE.SelectObject(n3, n4);
        Gdi32.INSTANCE.BitBlt(n3, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, n2, 0, 0, 0xCC0020);
        ScreenCaptureUtil.addCursorToScreen(n3);
        BufferedImage bufferedImage = ScreenCaptureUtil.convertToBufferedImage(n3, n4);
        ScreenCaptureUtil.cleanupResources(n2, n3, n4, n);
        return bufferedImage;
    }

    private static void addCursorToScreen(int n) {
        CURSORINFO cURSORINFO = new CURSORINFO();
        if (User32.INSTANCE.GetCursorInfo(cURSORINFO) && cURSORINFO.hCursor != null) {
            WinDef.POINT pOINT = cURSORINFO.ptScreenPos;
            User32.INSTANCE.DrawIconEx(n, pOINT.x, pOINT.y, cURSORINFO.hCursor, 32, 32, 0, null, 3);
        }
    }

    private static BufferedImage convertToBufferedImage(int n, int n2) {
        byte[] byArray = new byte[SCREEN_WIDTH * SCREEN_HEIGHT * 4];
        Gdi32.INSTANCE.GetDIBits(n, n2, 0, SCREEN_HEIGHT, byArray, BITMAP_INFO, 0);
        BufferedImage bufferedImage = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, 2);
        bufferedImage.setRGB(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, ScreenCaptureUtil.convertPixelsToARGB(byArray), 0, SCREEN_WIDTH);
        return bufferedImage;
    }

    private static int[] convertPixelsToARGB(byte[] byArray) {
        int[] nArray = new int[SCREEN_WIDTH * SCREEN_HEIGHT];
        for (int j = 0; j < SCREEN_HEIGHT; ++j) {
            for (int k = 0; k < SCREEN_WIDTH; ++k) {
                int n = ((SCREEN_HEIGHT - 1 - j) * SCREEN_WIDTH + k) * 4;
                int n2 = byArray[n] & 0xFF;
                int n3 = byArray[n + 1] & 0xFF;
                int n4 = byArray[n + 2] & 0xFF;
                int n5 = byArray[n + 3] & 0xFF;
                nArray[j * ScreenCaptureUtil.SCREEN_WIDTH + k] = n5 << 24 | n4 << 16 | n3 << 8 | n2;
            }
        }
        return nArray;
    }

    private static byte[] compressImage(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Thumbnails.of(bufferedImage).outputFormat("jpg").outputQuality(0.8).scale(1.0).toOutputStream(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private static void cleanupResources(int n, int n2, int n3, int n4) {
        Gdi32.INSTANCE.DeleteObject(n3);
        Gdi32.INSTANCE.DeleteDC(n2);
        User32.INSTANCE.ReleaseDC(n4, n);
    }

    public static interface User32
    extends Library {
        public static final User32 INSTANCE = (User32)Native.loadLibrary("user32", User32.class);

        public int GetDesktopWindow();

        public int GetDC(int var1);

        public boolean ReleaseDC(int var1, int var2);

        public boolean GetCursorInfo(CURSORINFO var1);

        public boolean DrawIconEx(int var1, int var2, int var3, Pointer var4, int var5, int var6, int var7, Pointer var8, int var9);
    }

    public static interface Gdi32
    extends Library {
        public static final Gdi32 INSTANCE = (Gdi32)Native.loadLibrary("gdi32", Gdi32.class);

        public int CreateCompatibleDC(int var1);

        public int CreateCompatibleBitmap(int var1, int var2, int var3);

        public int SelectObject(int var1, int var2);

        public int BitBlt(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9);

        public boolean DeleteObject(int var1);

        public boolean DeleteDC(int var1);

        public boolean GetDIBits(int var1, int var2, int var3, int var4, byte[] var5, BITMAPINFO var6, int var7);
    }

    public static class CURSORINFO
    extends Structure {
        public int cbSize = this.size();
        public int flags;
        public Pointer hCursor;
        public WinDef.POINT ptScreenPos;

        @Override
        protected List getFieldOrder() {
            return Arrays.asList("cbSize", "flags", "hCursor", "ptScreenPos");
        }
    }

    public static class BITMAPINFO
    extends Structure {
        public int biSize = 40;
        public int biWidth;
        public int biHeight;
        public short biPlanes = 1;
        public short biBitCount = (short)32;
        public int biCompression = 0;
        public int biSizeImage;
        public int biXPelsPerMeter = 0;
        public int biYPelsPerMeter = 0;
        public int biClrUsed = 0;
        public int biClrImportant = 0;

        public BITMAPINFO(int n, int n2) {
            this.biWidth = n;
            this.biHeight = n2;
        }

        @Override
        protected List getFieldOrder() {
            return Arrays.asList("biSize", "biWidth", "biHeight", "biPlanes", "biBitCount", "biCompression", "biSizeImage", "biXPelsPerMeter", "biYPelsPerMeter", "biClrUsed", "biClrImportant");
        }
    }
}

