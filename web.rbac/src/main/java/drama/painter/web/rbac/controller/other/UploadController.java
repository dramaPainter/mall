package drama.painter.web.rbac.controller.other;

import drama.painter.core.web.enums.UploadEnum;
import drama.painter.core.web.ftp.upload.IUpload;
import drama.painter.core.web.misc.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author murphy
 */
@RestController
public class UploadController {
    final IUpload upload;

    public UploadController(IUpload upload) {
        this.upload = upload;
    }

    @PostMapping("/upload/single")
    public Result single(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam(value = "image", required = false) String image, UploadEnum type) {
        return upload.upload(file == null ? image : file, type);
    }

    @PostMapping(value = "/upload/multiple")
    public Result multiple(@RequestParam(value = "files", required = false) List<MultipartFile> files, @RequestParam(value = "images", required = false) List<String> images, UploadEnum type) {
        return upload.uploadList(files == null || files.isEmpty() ? files : images, type);
    }
}
