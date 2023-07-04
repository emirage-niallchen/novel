package cn.com.emirage.novel.core.common.constant;

/**
 * 错误码枚举类。
 *
 * 错误码为字符串类型，共 5 位，分成两个部分：错误产生来源+四位数字编号。
 * 错误产生来源分为 A/B/C， A 表示错误来源于用户，比如参数错误，用户安装版本过低，用户支付
 * 超时等问题； B 表示错误来源于当前系统，往往是业务逻辑出错，或程序健壮性差等问题； C 表示错误来源
 * 于第三方服务，比如 CDN 服务出错，消息投递超时等问题；四位数字编号从 0001 到 9999，大类之间的
 * 步长间距预留 100。
 *
 * 错误码分为一级宏观错误码、二级宏观错误码、三级宏观错误码。
 * 在无法更加具体确定的错误场景中，可以直接使用一级宏观错误码。
 *
 * @author 陈博云
 * @date 2023/5/30 11:58
 */
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {
    /**
     * 正确执行后的返回
     * */
    OK("00000","一切 ok"),

    /**
     * 一级宏观错误码，用户端错误
     * */
    USER_ERROR("A0001","用户端错误"),

    /**
     * 二级宏观错误码，用户注册错误
     * */
    USER_REGISTER_ERROR("A0100","用户注册错误"),

    /**
     * 二级宏观错误码，用户未同意隐私协议
     * */
    USER_NO_AGREE_PRIVATE_ERROR("A0101","用户未同意隐私协议"),

    /**
     * 二级宏观错误码，注册国家或地区受限
     * */
    USER_REGISTER_AREA_LIMIT_ERROR("A0102","注册国家或地区受限"),

    /**
     * 二级宏观错误码，用户请求参数错误
     * */
    USER_REQUEST_PARAM_ERROR("A0400","用户请求参数错误"),

    // ...省略若干用户端二级宏观错误码

    /**
     * 一级宏观错误码，系统执行出错
     * */
    SYSTEM_ERROR("B0001","系统执行出错"),

    /**
     * 二级宏观错误码，系统执行超时
     * */
    SYSTEM_TIMEOUT_ERROR("B0100","系统执行超时"),

    // ...省略若干系统执行二级宏观错误码

    /**
     * 一级宏观错误码，调用第三方服务出错
     * */
    THIRD_SERVICE_ERROR("C0001","调用第三方服务出错"),

    /**
     * 一级宏观错误码，中间件服务出错
     * */
    MIDDLEWARE_SERVICE_ERROR("C0100","中间件服务出错")

    // ...省略若干三方服务调用二级宏观错误码

    ;

    /**
     * 错误码
     * */
    private String code;

    /**
     * 中文描述
     * */
    private String message;

}
