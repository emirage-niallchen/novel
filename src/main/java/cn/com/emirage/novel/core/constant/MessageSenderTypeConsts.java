package cn.com.emirage.novel.core.constant;


public class MessageSenderTypeConsts {

    private MessageSenderTypeConsts() {
        throw new IllegalStateException("Constant class");
    }

    /**
     * 注册成功的邮件发送器
     */
    public static final String REGISTER_MAIL_SENDER = "registerMailSender";

    /**
     * 秒杀活动的系统通知发送器
     */
    public static final String SECKILL_SYS_NOTICE_SENDER = "seckillSysNoticeSender";

}
