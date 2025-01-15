/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.diablo.client.utils.ScreenUtils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import net.coobird.thumbnailator.Thumbnails;

public class DirectXScreenCaptureUtil {
    public static byte[] getScreenCaptureBytes() {
        try {
            BufferedImage bufferedImage = DirectXScreenCaptureUtil.captureScreenWithDirectX();
            byte[] byArray = DirectXScreenCaptureUtil.compressImage(bufferedImage);
            return byArray;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static BufferedImage captureScreenWithDirectX() {
        IntByReference intByReference;
        Pointer pointer = Direct3D9.INSTANCE.Direct3DCreate9(32);
        if (pointer == null) {
            System.out.println("Failed to initialize Direct3D");
            return null;
        }
        D3DPRESENT_PARAMETERS d3DPRESENT_PARAMETERS = new D3DPRESENT_PARAMETERS();
        PointerByReference pointerByReference = new PointerByReference();
        int n = Direct3D9.INSTANCE.CreateDevice(pointer, 0, 1, null, 0, Pointer.NULL, pointerByReference);
        if (n != 0 || pointerByReference.getValue() == null) {
            System.out.println("Failed to create Direct3D device");
            return null;
        }
        PointerByReference pointerByReference2 = new PointerByReference();
        n = Direct3D9.INSTANCE.GetBackBuffer(pointerByReference.getValue(), 0, 0, pointerByReference2);
        if (n != 0 || pointerByReference2.getValue() == null) {
            System.out.println("Failed to get back buffer");
            return null;
        }
        BufferedImage bufferedImage = null;
        Pointer pointer2 = pointerByReference2.getValue();
        n = Direct3D9.INSTANCE.LockRect(pointer2, Pointer.NULL, intByReference = new IntByReference(), 0);
        if (n == 0) {
            int n2 = 1920;
            int n3 = 1080;
            bufferedImage = new BufferedImage(n2, n3, 2);
            ByteBuffer byteBuffer = ByteBuffer.allocate(n2 * n3 * 4);
            byteBuffer.put(new byte[n2 * n3 * 4]);
            for (int j = 0; j < n3; ++j) {
                for (int k = 0; k < n2; ++k) {
                    int n4 = byteBuffer.getInt((j * n2 + k) * 4);
                    bufferedImage.setRGB(k, j, n4);
                }
            }
            Direct3D9.INSTANCE.UnlockRect(pointer2);
        }
        return bufferedImage;
    }

    private static byte[] compressImage(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Thumbnails.of(bufferedImage).outputFormat("jpg").outputQuality(0.8).scale(0.6).toOutputStream(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static interface Direct3D9
    extends Library {
        public static final Direct3D9 INSTANCE = (Direct3D9)Native.loadLibrary("d3d9", Direct3D9.class);
        public static final int D3D_SDK_VERSION = 32;
        public static final int D3DADAPTER_DEFAULT = 0;
        public static final int D3DDEVTYPE_HAL = 1;

        public Pointer Direct3DCreate9(int var1);

        public int CreateDevice(Pointer var1, int var2, int var3, WinDef.HWND var4, int var5, Pointer var6, PointerByReference var7);

        public int GetBackBuffer(Pointer var1, int var2, int var3, PointerByReference var4);

        public int GetRenderTargetData(Pointer var1, Pointer var2, Pointer var3);

        public int LockRect(Pointer var1, Pointer var2, IntByReference var3, int var4);

        public int UnlockRect(Pointer var1);
    }

    public static class D3DPRESENT_PARAMETERS
    extends Structure {
        public int BackBufferWidth;
        public int BackBufferHeight;
        public int BackBufferFormat;
        public int BackBufferCount;
        public int MultiSampleType;
        public int MultiSampleQuality;
        public int SwapEffect;
        public WinDef.HWND hDeviceWindow;
        public boolean Windowed;

        @Override
        protected List getFieldOrder() {
            return Arrays.asList("BackBufferWidth", "BackBufferHeight", "BackBufferFormat", "BackBufferCount", "MultiSampleType", "MultiSampleQuality", "SwapEffect", "hDeviceWindow", "Windowed");
        }
    }
}

