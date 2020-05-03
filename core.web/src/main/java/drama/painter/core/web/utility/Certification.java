package drama.painter.core.web.utility;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

/**
 * @author murphy
 */
public final class Certification {
    public static SSLContext parse(String certText) {
        try {
            Certificate ca;
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            try (InputStream inputStream = new ByteArrayInputStream(certText.getBytes())) {
                ca = cf.generateCertificate(inputStream);
            }

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init(keyStore);
            sslContext.init(null, factory.getTrustManagers(), new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
