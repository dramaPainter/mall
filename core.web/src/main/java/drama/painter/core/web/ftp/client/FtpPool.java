package drama.painter.core.web.ftp.client;

import drama.painter.core.web.utility.Logs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.ObjectPool;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author murphy
 */
@Slf4j
public class FtpPool {
    private static volatile boolean hasInit = false;
    private static ObjectPool<FTPClient> ftpClientPool;

    public static void init(ObjectPool<FTPClient> ftpClientPool) {
        if (!hasInit) {
            synchronized (FtpPool.class) {
                if (!hasInit) {
                    FtpPool.ftpClientPool = ftpClientPool;
                    hasInit = true;
                }
            }
        }
    }

    public static void upload(InputStream stream, String basePath, String filePath) throws IOException {
        FTPClient ftp = getFtpClient();

        try {
            String[] dirs = filePath.split("/");
            String tempPath = basePath;
            for (int i = 1; i < dirs.length - 1; i++) {
                tempPath += "/" + dirs[i];
                if (!ftp.changeWorkingDirectory(tempPath)) {
                    ftp.makeDirectory(tempPath);
                    ftp.changeWorkingDirectory(tempPath);
                }
            }

            try (InputStream is = stream) {
                boolean success = ftp.storeFile(basePath + filePath, is);
                FtpEnum.FTP_UPLOAD_FAILED.doAssert(!success, ftp.getReplyString(), filePath);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            ftp.logout();
            releaseFtpClient(ftp);
        }
    }

    private static FTPClient getFtpClient() {
        FtpEnum.FTP_NOT_INIT.doAssert(!hasInit);

        FTPClient ftpClient = null;
        int tryTimes = 3;

        for (int i = 1; i <= tryTimes; i++) {
            try {
                ftpClient = ftpClientPool.borrowObject();
                break;
            } catch (Exception e) {
                log.error("FTP尝试第{}次borrowObject失败: {}", i, e.toString());
                Logs.sleep(1);
            }
        }

        FtpEnum.FTP_NOT_INIT.doAssert(ftpClient == null);
        return ftpClient;
    }

    private static void releaseFtpClient(FTPClient ftpClient) {
        if (ftpClient == null) {
            return;
        }

        try {
            ftpClientPool.returnObject(ftpClient);
        } catch (Exception e) {
            log.error("进程池无法回收FtpClient。", e);
            if (ftpClient.isAvailable()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    log.error("FtpClient无法disconnect()：", ioe);
                }
            }
        }
    }
}
