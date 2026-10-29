package org.example.practice.mapper;

import org.example.practice.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user (username, password) VALUES (#{username}, #{password})")
    void insertUser(String username, String password);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);
}
