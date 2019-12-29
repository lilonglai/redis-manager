package com.newegg.ec.redis.dao;

import com.newegg.ec.redis.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Manage users
 *
 * @author Jay.H.Zou
 * @date 7/19/2019
 */
@Mapper
public interface IUserDao {

    @Select("SELECT * FROM user2")
    List<User> selectAllUser();

    @Select("SELECT " +
            "user2.user_id AS user_id, " +
            "user2.group_id AS group_id, " +
            "user2.user_name AS user_name, " +
            "user2.password AS password, " +
            "group_user.user_role AS user_role, " +
            "user2.avatar AS avatar, " +
            "user2.email AS email, " +
            "user2.mobile AS mobile, " +
            "user2.update_time AS update_time " +
            "FROM user2, group_user " +
            "WHERE group_user.group_id = user2.group_id " +
            "AND group_user.grant_group_id = user2.group_id " +
            "AND group_user.user_id = user2.user_id " +
            "AND user.user_id = #{userId}")
    User selectUserById(Integer userId);

    @Select("SELECT " +
            "user2.user_id AS user_id, " +
            "user2.group_id AS group_id, " +
            "user2.user_name AS user_name, " +
            "group_user.user_role AS user_role, " +
            "user2.avatar AS avatar, " +
            "user2.email AS email, " +
            "user2.mobile AS mobile, " +
            "user2.update_time AS update_time " +
            "FROM user2, group_user " +
            "WHERE group_user.group_id = user2.group_id " +
            "AND group_user.user_id = user2.user_id " +
            "AND group_user.group_id = #{groupId} " +
            "AND group_user.grant_group_id = #{groupId}")
    List<User> selectUserByGroupId(Integer groupId);

    @Select("SELECT " +
            "user.user_id AS user_id, " +
            "group_user.grant_group_id AS group_id, " +
            "user.user_name AS user_name, " +
            "group_user.user_role AS user_role, " +
            "user.avatar AS avatar, " +
            "user.email AS email, " +
            "user.mobile AS mobile, " +
            "user.update_time AS update_time " +
            "FROM group_user, user " +
            "WHERE group_user.group_id = user.group_id " +
            "AND group_user.user_id = user.user_id " +
            "AND grant_group_id = #{grantGroupId} " +
            "AND group_user.group_id != #{grantGroupId}")
    List<User> selectGrantUserByGroupId(Integer grantGroupId);

    @Select("SELECT " +
            "user2.user_id AS user_id, " +
            "user2.group_id AS group_id, " +
            "group_user.user_role AS user_role " +
            "FROM user2, group_user " +
            "WHERE group_user.user_id = user2.user_id " +
            "AND group_user.group_id = user2.group_id " +
            "AND group_user.grant_group_id = #{grantGroupId} " +
            "AND group_user.user_id = #{userId}")
    User selectUserRole(@Param("grantGroupId") Integer grantGroupId, @Param("userId") Integer userId);

    @Select("SELECT * FROM user2 WHERE user_name = #{userName} AND password = #{password}")
    User selectUserByNameAndPassword(User user);

    @Select("SELECT * FROM user2 WHERE user_name = #{userName}")
    User selectUserByName(String userName);

    @Select("SELECT COUNT(user_id) FROM user2 WHERE group_id = #{groupId}")
    int selectUserNumber(Integer groupId);

    @Insert("INSERT INTO user2 (group_id, user_name, password, email, mobile, user_type, update_time) " +
            "VALUES (#{groupId}, #{userName}, #{password}, #{email}, #{mobile}, 0, current_timestamp)")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    int insertUser(User user);

    @Update("UPDATE user2 SET user_name = #{userName}, password = #{password}, " +
            "email = #{email}, mobile = #{mobile}, update_time = current_timestamp " +
            "WHERE user_id = #{userId}")
    int updateUser(User user);

    @Update("UPDATE user2 SET avatar = #{avatar} WHERE user_id = #{userId}")
    int updateUserAvatar(User user);

    @Delete("DELETE FROM user2 WHERE user_id = #{userId}")
    int deleteUserById(Integer userId);

    @Delete("DELETE FROM user2 WHERE group_id = #{groupId}")
    int deleteUserByGroupId(Integer groupId);

    @Select("create TABLE user2 ( " +
            "user_id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 100, INCREMENT BY 1), " +
            "group_id integer NOT NULL, " +
            "user_name varchar(255) NOT NULL, " +
            "password varchar(255) DEFAULT NULL, " +
            "token varchar(255) DEFAULT NULL, " +
            "avatar varchar(255) DEFAULT NULL, " +
            "email varchar(255) DEFAULT NULL, " +
            "mobile varchar(20) DEFAULT NULL, " +
            "user_type integer NOT NULL, " +
            "update_time timestamp NOT NULL, " +
            "PRIMARY KEY (user_id), " +
            "UNIQUE (user_name) " +
            ")")
    void createUserTable();

}
