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
    private static final int STEP = 2;
    private static final int ZERO = 0;
    private static final int POOL_SIZE = 5;
    private static ExecutorService POOL = null;

    private final FtpConfig.FtpConfigProperties ftpConfig;

    public Upload(FtpConfig.FtpConfigProperties ftpConfig) {
        this.ftpConfig = ftpConfig;
    }

    @Override
    public Result upload(Object file, String filePath, long id) {
        int userid = ((PageUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId();
        try {
            return new FileUploader(ftpConfig.isLocalized(), file, ftpConfig.getBasePath(), filePath, ftpConfig.getDomain(), userid, id, ZERO).call();
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return Result.toFail(e.getMessage());
        }
    }

    @Override
    public Result uploadList(List<?> files, String filePath, long id) {
        init();

        int userid = ((PageUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId();
        int size = files.size();
        List<FutureTask<Result>> list = null;
        try {
            list = new ArrayList(size);
            if (files.get(0) instanceof String) {
                for (int i = 0; i < size; i += STEP) {
                    FutureTask task = new FutureTask(new FileUploader(ftpConfig.isLocalized(), files.get(i + 1), ftpConfig.getBasePath(), filePath, ftpConfig.getDomain(), userid, id, i / 2));
                    POOL.submit(task);
                    list.add(task);
                }
            } else {
                for (int i = 0; i < size; i++) {
                    FutureTask task = new FutureTask(new FileUploader(ftpConfig.isLocalized(), files.get(i), ftpConfig.getBasePath(), filePath, ftpConfig.getDomain(), userid, id, i));
                    POOL.submit(task);
                    list.add(task);
                }
            }

            List<String> url = new ArrayList(size);
            Result r = Result.toSuccess("文件上传成功");
            for (FutureTask<Result> o : list) {
                Result temp = o.get();
                if (temp.getCode() < ZERO) {
                    r = temp;
                    break;
                }
                url.add(temp.getData().toString());
            }

            r.setData(url);
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