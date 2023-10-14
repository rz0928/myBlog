package com.example.enums;

public enum AppHttpCodeEnum {
    SUCCESS(200,"操作成功"),
    NEED_LOGIN(401,"需登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIT(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503,"邮箱已存在"),
    REQUIRE_USERNAME(504,"需填写用户名"),
    LOGIN_ERROR(505,"账号或密码错误"),
    CONTENT_NOT_NULL(506, "评论内容不能为空"),
    FILE_TYPE_ERROR(507,"文件类型不是png/jpg/jpeg" ),
    NEED_USERNAME(508, "缺少用户名"),
    NEED_EMAIL(509, "缺少邮箱"),
    NEED_NICKNAME(510, "缺少昵称"),
    NEED_PASSWORD(511, "缺少密码"),
    NEED_FILE(512, "缺少文件"),
    NICKNAME_EXIT(513, "昵称已存在"),
    NEED_TAG_NAME(514,"需要标签名称");
    final int code;
    final String message;
   AppHttpCodeEnum(int code,String message){
       this.code=code;
       this.message=message;
   }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }

    @Override
    public String toString() {
        return "AppHttpCodeEnum{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
