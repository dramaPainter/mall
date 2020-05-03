package drama.painter.core.web.ftp.client;

import drama.painter.core.web.asserts.BaseAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author murphy
 */
@Getter
@AllArgsConstructor
public enum FtpEnum implements BaseAssert {
    /**
     * FTP还未初始化或连接失败
     */
    FTP_NOT_INIT(-201, "FTP还未初始化或连接失败"),

    /**
     * FTP无法上传文件
     */
    FTP_UPLOAD_FAILED(-202, "FTP无法上传文件：%s 路径：%s"),

    /**
     * FTP登陆失败
     */
    FTP_LOGIN_FAILED(-203, "FTP登陆失败：帐号或密码不正确。【%s】【%s】"),

    /**
     * FTP拒绝连接
     */
    FTP_REFUSED(-204, "FTP拒绝连接：%s"),

    /**
     * 签名不正确
     */
    SIGN_MISMATCH(-101, "签名不正确"),

    /**
     * 玩家不存在
     */
    USER_NOT_EXIST(-102, "玩家不存在"),

    /**
     * 没有可用的域名
     */
    DOMAIN_NOT_EXIST(-103, "没有可用的域名"),

    /**
     * 请选择要上传的文件
     */
    UPLOAD_NOT_EXIST(-104, "请选择要上传的文件");

    private final int value;
    private final String name;
}
