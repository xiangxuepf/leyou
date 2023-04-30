package com.leyou.user.mapper;

import com.leyou.user.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {
    @Select("select * from tb_stock where id = #{id}")
    List<Object> selectUser(@Param("id") String id);
}
