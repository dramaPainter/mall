package drama.painter.core.web.ftp.upload;

import drama.painter.core.web.ftp.client.FtpEnum;
import drama.painter.core.web.ftp.client.FtpPool;
import drama.painter.core.web.misc.Result;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

class FileUploader implements Callable<Result> {
    static final BASE64Decoder base64 = new BASE64Decoder();
    final boolean localized;
    final Object file;
    final String basePath;
    final String filePath;
    final String domain;

    public FileUploader(boolean localized, Object file, String basePath, String filePath, String domain) {
        this.localized = localized;
        this.file = file;
        this.basePath = basePath;
        this.domain = domain;
        this.filePath = filePath;
    }

    @Override
    public Result call() throws IOException {
        FtpEnum.UPLOAD_NOT_EXIST.doAssert(file == null);
        InputStream stream;
        if (file instanceof MultipartFile) {
            stream = ((MultipartFile) file).getInputStream();
        } else {
            stream = new ByteArrayInputStream(base64.decodeBuffer(file.toString().split(",")[1]));
        }

        if (localized) {
            localUpload(stream, basePath, filePath);
        } else {
            FtpPool.upload(stream, basePath, filePath);
        }

        return Result.toData(0, domain + filePath);
    }

    private void localUpload(InputStream stream, String basePath, String filePath) throws IOException {
        File file = new File(basePath + filePath);
        String[] dirs = filePath.split("/");
        String tempPath = basePath;
        for (int i = 1; i < dirs.length - 1; i++) {
            tempPath += "/" + dirs[i];
            if (!Files.exists(Paths.get(tempPath))) {
                Files.createDirectory(Paths.get(tempPath));
            }
        }

        try (InputStream inStream = stream) {
            try (FileOutputStream outStream = new FileOutputStream(file)) {
                int read;
                byte[] bytes = new byte[1024];

                while ((read = inStream.read(bytes)) != -1) {
                    outStream.write(bytes, 0, read);
                }
            }
        }
    }
}
