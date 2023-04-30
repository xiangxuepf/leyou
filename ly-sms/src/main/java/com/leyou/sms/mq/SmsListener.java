package com.leyou.sms.mq;

import com.leyou.common.utils.JsonUtils;
import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.utils.SmsUtils;
import com.rabbitmq.tools.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


import java.util.Map;

@Slf4j
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {
    @Autowired
    private SmsProperties prop;
    @Autowired
    private SmsUtils smsUtils;

    /**
     * 发送短信验证码
     * @param msg
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.sms.queue", durable = "true"),
            exchange = @Exchange(value = "leyou.sms.exchange",
                    ignoreDeclarationExceptions = "true"),
            key = {"sms.verify.code"}))
    public void listenSms(Map<String, String> msg) throws Exception {
        if(CollectionUtils.isEmpty(msg)){
            return;
        }
        String phone = msg.remove("phone");
        if(org.apache.commons.lang3.StringUtils.isBlank(phone)){
            return;
        }

        // 处理消息
        // 异常处理，建议不要重试，直接捕获记录日志即可；让用户自己点击重新发送;
        // 如果给重试，容易触发限流，那么限流就不止限制 你一个账号了；
        // 异常就在工具类解决，否则每个监听器都要写这个异常捕获；
            smsUtils.sendSms(phone,prop.getSignName()
                    ,prop.getVerifyCodeTemplate()
                    , JsonUtils.serialize(msg));

    }


}
