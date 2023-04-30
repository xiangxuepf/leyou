package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "user_verify_phone_";

    public Boolean checkData(String data, Integer type) {
        User record = new User();

        // 判断数据类型
        switch (type){
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                throw new LyException(ExceptionEnums.INVALID_USER_DATA_TYPE);
        }

        return userMapper.selectCount(record) == 0;

    }

    @Transactional
    public void sendCode(String phone) {

        // 生成key；
        String key = KEY_PREFIX + phone;

        // 生成code;
        String code = NumberUtils.generateCode(6);

        // 发送验证码
        Map<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        amqpTemplate.convertAndSend("ly.sms.exchange", "sms.verify.code.queue", msg);

        // 保存验证码
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
        int i = 1/0;
    }

    public void register(User user, String code) {
        // 1. 校验验证码
        String redisCode = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if(!StringUtils.equals(code,redisCode)){
            return;
        }
        // 2. 生成随机码
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        // 3. 加盐加密存储MD5
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        // 4. 新增用户信息
        user.setId(null);
        user.setCreated(new Date());
        userMapper.insertSelective(user);

        // 5. 删除验证码
        redisTemplate.delete(KEY_PREFIX + user.getPhone());
    }

    public User queryUser(String username, String password) {

        // 1. 先根据用户名查询用户
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);

        if(user == null) {
            throw new LyException(ExceptionEnums.USER_NOT_FOUND);
        }

        // 2. 对用户输入的密码加盐加密
        password = CodecUtils.md5Hex(password, user.getSalt());

        // 3. 判断用户输入的密码是否正确
        if(!StringUtils.equals(password, user.getPassword())){
            throw new LyException(ExceptionEnums.USER_NOT_FOUND);
        }
        return user;
    }
}
