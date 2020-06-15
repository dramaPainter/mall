package drama.painter.web.rbac.controller.other;

import drama.painter.core.web.enums.UploadEnum;
import drama.painter.core.web.misc.Result;
import drama.painter.web.rbac.model.com.Image;
import drama.painter.web.rbac.service.intf.com.IImage;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author murphy
 */
@RestController
public class UploadController {
    final IImage img;

    public UploadController(IImage img) {
        this.img = img;
    }

    @PostMapping("/upload/single")
    public Result single(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam(value = "image", required = false) String image, UploadEnum type, String value) {
        return img.save(file == null ? image : file, type, value);
    }

    @PostMapping(value = "/upload/multiple")
    public Result multiple(@RequestParam(value = "file", required = false) List<MultipartFile> file, UploadEnum type, String value) {
        return img.saveList(file, type, value);
    }

    @PostMapping(value = "/upload/remove")
    public Result remove(@RequestBody int id) {
        img.remove(id);
        return Result.SUCCESS;
    }

    @GetMapping(value = "/upload/list")
    public Result list(Integer page, UploadEnum type, String value) {
        return img.list(page, type, value);
    }
}
