package drama.painter.core.web.ftp.upload;

import drama.painter.core.web.misc.Result;

import java.util.List;

/**
 * @author murphy
 */
public interface IUpload {
    /**
     * 上传单个文件
     *
     * @param file     可以是BASE64字符串，也可以是MultipartFile
     * @param filePath 上传相对路径，可使用变量
     * @param id       可选用的替换值id
     * @return
     */
    Result upload(Object file, String filePath, long id);

    /**
     * 上传单个文件
     *
     * @param files    可以是BASE64字符串的List，也可以是MultipartFile的List
     * @param filePath 上传相对路径，可使用变量
     * @param id       可选用的替换值id
     * @return
     */
    Result uploadList(List<?> files, String filePath, long id);
}
