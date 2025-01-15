/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.diablo.client.utils.ScreenUtils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class TestScreenCaptureUtil {
    private static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private static final BITMAPINFO BITMAP_INFO = new BITMAPINFO(SCREEN_WIDTH, SCREEN_HEIGHT);
    private static int hdcScreen;
    private static int hdcMemory;
    private static int hBitmap;

    public static void initializeResources() {
        int n = User32.INSTANCE.GetDesktopWindow();
        hdcScreen = User32.INSTANCE.GetDC(n);
        hdcMemory = Gdi32.INSTANCE.CreateCompatibleDC(hdcScreen);
        hBitmap = Gdi32.INSTANCE.CreateCompatibleBitmap(hdcScreen, SCREEN_WIDTH, SCREEN_HEIGHT);
        Gdi32.INSTANCE.SelectObject(hdcMemory, hBitmap);
    }

    public static byte[] getScreenCaptureBytes() {
        try {
            BufferedImage bufferedImage = TestScreenCaptureUtil.captureScreenWithCursor();
            return TestScreenCaptureUtil.encodeFrame(bufferedImage);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static BufferedImage getScreenCapture() {
        try {
            return TestScreenCaptureUtil.captureScreenWithCursor();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private static BufferedImage captureScreenWithCursor() {
        Gdi32.INSTANCE.BitBlt(hdcMemory, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, hdcScreen, 0, 0, 0xCC0020);
        TestScreenCaptureUtil.addCursorToScreen();
        return TestScreenCaptureUtil.convertToBufferedImage();
    }

    private static void addCursorToScreen() {
        CURSORINFO cURSORINFO = new CURSORINFO();
        if (User32.INSTANCE.GetCursorInfo(cURSORINFO) && cURSORINFO.hCursor != null) {
            int n = cURSORINFO.ptScreenPos.x;
            int n2 = cURSORINFO.ptScreenPos.y;
            User32.INSTANCE.DrawIconEx(hdcMemory, n, n2, cURSORINFO.hCursor, 32, 32, 0, null, 3);
        }
    }

    private static BufferedImage convertToBufferedImage() {
        byte[] byArray = new byte[SCREEN_WIDTH * SCREEN_HEIGHT * 4];
        Gdi32.INSTANCE.GetDIBits(hdcMemory, hBitmap, 0, SCREEN_HEIGHT, byArray, BITMAP_INFO, 0);
        BufferedImage bufferedImage = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, 2);
        bufferedImage.setRGB(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, TestScreenCaptureUtil.convertPixelsToARGB(byArray), 0, SCREEN_WIDTH);
        return bufferedImage;
    }

    private static int[] convertPixelsToARGB(byte[] byArray) {
        int[] nArray = new int[SCREEN_WIDTH * SCREEN_HEIGHT];
        for (int j = 0; j < nArray.length; ++j) {
            int n = j * 4;
            int n2 = byArray[n] & 0xFF;
            int n3 = byArray[n + 1] & 0xFF;
            int n4 = byArray[n + 2] & 0xFF;
            int n5 = byArray[n + 3] & 0xFF;
            nArray[j] = n5 << 24 | n4 << 16 | n3 << 8 | n2;
        }
        return nArray;
    }

    private static byte[] encodeFrame(BufferedImage object) {
        Object object2;
        Object object3;
        block4: {
            if (((BufferedImage)object).getType() != 1) {
                object3 = new BufferedImage(((BufferedImage)object).getWidth(), ((BufferedImage)object).getHeight(), 1);
                object2 = ((BufferedImage)object3).getGraphics();
                ((Graphics)object2).drawImage((Image)object, 0, 0, null);
                ((Graphics)object2).dispose();
                object = object3;
            }
            try {
                object3 = new ByteArrayOutputStream();
                object2 = ImageIO.getImageWritersByFormatName("jpg");
                if (object2.hasNext()) break block4;
                byte[] byArray = null;
                ((ByteArrayOutputStream)object3).close();
                return byArray;
            } catch (IOException iOException) {
                iOException.printStackTrace();
                return null;
            }
        }
        ImageWriter imageWriter = (ImageWriter)object2.next();
        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
        imageWriteParam.setCompressionMode(2);
        imageWriteParam.setCompressionQuality(0.9f);
        Object object4 = ImageIO.createImageOutputStream(object3);
        imageWriter.setOutput(object4);
        imageWriter.write(null, new IIOImage((RenderedImage)object, null, null), imageWriteParam);
        ImageOutputStream imageOutputStream = object4;
        object4.close();
        object4 = ((ByteArrayOutputStream)object3).toByteArray();
        ((ByteArrayOutputStream)object3).close();
        return object4;
    }

    public static void cleanupResources() {
        Gdi32.INSTANCE.DeleteObject(hBitmap);
        Gdi32.INSTANCE.DeleteDC(hdcMemory);
        User32.INSTANCE.ReleaseDC(User32.INSTANCE.GetDesktopWindow(), hdcScreen);
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
            this.biHeight = -n2;
        }

        @Override
        protected List getFieldOrder() {
            return Arrays.asList("biSize", "biWidth", "biHeight", "biPlanes", "biBitCount", "biCompression", "biSizeImage", "biXPelsPerMeter", "biYPelsPerMeter", "biClrUsed", "biClrImportant");
        }
    }
}

