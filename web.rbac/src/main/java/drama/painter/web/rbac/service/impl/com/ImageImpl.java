package drama.painter.web.rbac.service.impl.com;

import drama.painter.core.web.enums.UploadEnum;
import drama.painter.core.web.ftp.upload.IUpload;
import drama.painter.core.web.misc.Constant;
import drama.painter.core.web.misc.Page;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.utility.Strings;
import drama.painter.web.rbac.mapper.com.ImageMapper;
import drama.painter.web.rbac.model.com.Image;
import drama.painter.web.rbac.service.intf.com.IImage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author murphy
 */
@Service
public class ImageImpl implements IImage {
    final ImageMapper imageMapper;
    final IUpload upload;


    public ImageImpl(ImageMapper im, IUpload iu) {
        this.imageMapper = im;
        this.upload = iu;
    }

    @Override
    public Result<List<Image>> list(int page, UploadEnum type, String value) {
        Page p = new Page(page, Constant.PAGE_SIZE);
        Integer id = Strings.parseInteger(value);
        String name = StringUtils.isEmpty(value) ? null : (id == null ? value : null);
        List<Image> list = imageMapper.list(p, type, name, id);
        return Result.toData(p.getRowCount(), list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result save(Object file, UploadEnum type, String value) {
        if (file == null) {
            return Result.toFail("请选择要上传的文件");
        }

        Result<String> r = upload.upload(file, type);
        if (r.getCode() == Result.SUCCESS.getCode()) {
            imageMapper.save(Arrays.asList(new Image(null, "", type, 0, r.getData())));
        }
        return r;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result saveList(List<MultipartFile> files, UploadEnum type, String value) {
        if (files.isEmpty()) {
            return Result.toFail("请选择要上传的文件");
        }

        Result<List<String>> r = upload.uploadList(files, type);
        if (r.getCode() == Result.SUCCESS.getCode()) {
            List<Image> list = r.getData().stream()
                    .map(o -> new Image(null, "", type, 0, o))
                    .collect(Collectors.toList());
            imageMapper.save(list);
        }
        return r;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result update(List<Image> list) {
        int id = list.get(0).getValue();
        imageMapper.reset(id);
        imageMapper.update(list);
        return Result.SUCCESS;
    }

    @Override
    public void remove(int id) {
        imageMapper.remove(id);
    }
}
