package drama.painter.core.web.ftp.client;

import drama.painter.core.web.utility.Encrypts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author murphy
 */
@Slf4j
public class FtpClient implements PooledObjectFactory<FTPClient> {
    private final FtpConfig.FtpConfigProperties ftpConfig;

    public FtpClient(FtpConfig.FtpConfigProperties props) {
        ftpConfig = props;
    }

    @Override
    public PooledObject<FTPClient> makeObject() throws Exception {
        FTPClient ftpClient = new FTPClient();

        if (log.isDebugEnabled()) {
            ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        }

        try {
            ftpClient.setDataTimeout(ftpConfig.getReadTimeOut());
            ftpClient.setConnectTimeout(ftpConfig.getConnectTimeOut());
            ftpClient.connect(ftpConfig.getHost(), ftpConfig.getPort());

            int reply = ftpClient.getReplyCode();
            FtpEnum.FTP_REFUSED.doAssert(!FTPReply.isPositiveCompletion(reply), ftpClient.getReplyString());

            boolean result = ftpClient.login(ftpConfig.getUsername(), Encrypts.decrypt(ftpConfig.getPassword()));
            FtpEnum.FTP_LOGIN_FAILED.doAssert(!result, ftpConfig.getUsername(), ftpConfig.getPassword());

            log.debug("已连接FTP服务器：{}", ftpConfig.getHost());
            ftpClient.setBufferSize(ftpConfig.getBufferSize());
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            ftpClient.changeWorkingDirectory(ftpConfig.getBasePath());
            return new DefaultPooledObject(ftpClient);
        } catch (Exception e) {
            if (ftpClient.isAvailable()) {
                ftpClient.disconnect();
            }
            ftpClient = null;
            throw e;
        }
    }

    @Override
    public void destroyObject(PooledObject<FTPClient> p) throws IOException {
        if (p == null || p.getObject() == null) {
            return;
        }

        if (p.getObject().isConnected()) {
            p.getObject().disconnect();
        }
    }

    @Override
    public boolean validateObject(PooledObject<FTPClient> p) {
        if (p == null || p.getObject() == null || !p.getObject().isConnected()) {
            return false;
        }

        try {
            return p.getObject().sendNoOp();
        } catch (IOException e) {
            log.debug("FTP noop命令出错", e);
            return false;
        }
    }

    @Override
    public void activateObject(PooledObject<FTPClient> p) {
    }

    @Override
    public void passivateObject(PooledObject<FTPClient> p) {
    }
}
