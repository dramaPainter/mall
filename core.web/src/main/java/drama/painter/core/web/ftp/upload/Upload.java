package drama.painter.core.web.ftp.upload;

import drama.painter.core.web.ftp.client.FtpConfig;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.security.PageUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.threads.TaskThreadFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author murphy
 */
@Slf4j
@Service
public class Upload implements IUpload {
    private static final int ZERO = 0;
    private static final int POOL_SIZE = 5;
    private static ExecutorService POOL = null;

    private final FtpConfig.FtpConfigProperties ftpConfig;

    public Upload(FtpConfig.FtpConfigProperties ftpConfig) {
        this.ftpConfig = ftpConfig;
    }

    @Override
    public Result upload(Object file, String filePath) {
        try {
            return new FileUploader(ftpConfig.isLocalized(), file, ftpConfig.getBasePath(), filePath, ftpConfig.getDomain()).call();
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return Result.toFail(e.getMessage());
        }
    }

    @Override
    public Result uploadList(List<?> files, String filePath) {
        init();

        int userid = ((PageUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId();
        int size = files.size();
        List<FutureTask<Result>> list = new ArrayList(size);
        try {
            for (int i = 0; i < size; i++) {
                FutureTask task = new FutureTask(new FileUploader(ftpConfig.isLocalized(), files.get(i), ftpConfig.getBasePath(), filePath, ftpConfig.getDomain()));
                POOL.submit(task);
                list.add(task);
            }

            Result r = Result.toSuccess("文件上传成功");
            List<String> urls = new ArrayList(size);
            for (FutureTask<Result> o : list) {
                Result temp = o.get();
                if (temp.getCode() < ZERO) {
                    r = temp;
                    break;
                }
                urls.add(temp.getData().toString());
            }

            r.setData(urls);
            return r;
        } catch (Exception e) {
            log.error("文件上传出错：", e);
            return Result.toFail("上传出现错误：" + e.getMessage());
        } finally {
            if (list != null) {
                list.clear();
            }
        }
    }

    private void init() {
        if (POOL == null) {
            synchronized (this) {
                if (POOL == null) {
                    POOL = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE, ZERO, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(POOL_SIZE),
                            new TaskThreadFactory("FtpUploadThread", false, Thread.NORM_PRIORITY));
                }
            }
        }
    }
}
