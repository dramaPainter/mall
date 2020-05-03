package drama.painter.core.web.utility;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * @author murphy
 */
public class Networks {
    static final int CONNECTION_TIME_OUT = 30000;
    static final String HTTPS_PROTOCOL = "https://";
    static final SSLContext CONTEXT;

    static {
        try {
            CONTEXT = SSLContext.getInstance("SSL");
            CONTEXT.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new SecureRandom());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String url) throws IOException {
        return get(url, "utf-8");
    }

    public static byte[] getBytes(String url) throws IOException {
        HttpURLConnection conn = getConnection(url, null, null, null, null);
        try {
            return navibyte(conn.getInputStream());
        } finally {
            conn.disconnect();
        }
    }

    public static String get(String url, String encoding) throws IOException {
        return post(url, null, encoding, "application/x-www-form-urlencoded", null);
    }

    public static String post(String url, String body) throws IOException {
        return post(url, body, "utf-8", "application/x-www-form-urlencoded", null);
    }

    public static String post(String url, String body, String encoding, String contentType, String auth) throws IOException {
        HttpURLConnection conn = getConnection(url, body, encoding, contentType, auth);
        try {
            return post(conn.getInputStream(), encoding);
        } finally {
            conn.disconnect();
        }
    }

    public static String post(InputStream stream, String encoding) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, encoding))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } finally {
            stream.close();
        }
    }

    public static String postJson(String url, String body) throws IOException {
        return post(url, body, "utf-8", "application/json", null);
    }

    public static String postJson(String url, String body, String auth) throws IOException {
        return post(url, body, "utf-8", "application/json", auth);
    }

    static HttpURLConnection getConnection(String url, String body, String encoding, String contentType, String auth) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

        if (url.toLowerCase().startsWith(HTTPS_PROTOCOL)) {
            HttpsURLConnection https = (HttpsURLConnection) conn;
            https.setSSLSocketFactory(CONTEXT.getSocketFactory());
            https.setHostnameVerifier((a, b) -> true);
        }

        if (contentType != null) {
            conn.setRequestProperty("Content-Type", contentType);
        }

        // 必须设置setConnectTimeout时间，否则会一直阻塞到死
        conn.setConnectTimeout(CONNECTION_TIME_OUT);
        conn.setReadTimeout(CONNECTION_TIME_OUT);
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3100.0 Safari/537.36");
        conn.setRequestProperty("Connection", "close");

        if (auth != null) {
            conn.setRequestProperty("Authorization", auth);
        }

        if (body != null) {
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            try (OutputStream fos = conn.getOutputStream()) {
                fos.write(body.getBytes(encoding));
                fos.flush();
            }
        }

        return conn;
    }

    static byte[] navibyte(InputStream stream) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1204];
            int read;
            while ((read = stream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            return out.toByteArray();
        } finally {
            stream.close();
        }
    }

    static class TrustAnyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }
}
