package com.newegg.ec.redis.dao;

import com.newegg.ec.redis.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Jay.H.Zou
 * @date 2019/9/18
 */
@Mapper
public interface IGroupUserDao {

    @Select("SELECT distinct group_id, user_id  FROM group_user WHERE user_role = 0")
    List<User> selectAllSuperAdmin();

    @Select("SELECT * FROM group_user WHERE grant_group_id = #{grantGroupId} AND user_id = #{userId}")
    User isGranted(@Param("grantGroupId") Integer grantGroupId, @Param("userId") Integer userId);

    @Insert("INSERT INTO group_user (group_id, user_id, user_role, grant_group_id, update_time) " +
            "VALUES (#{user.groupId}, #{user.userId}, #{user.userRole}, #{grantGroupId}, current_timestamp)")
    int insertGroupUser(@Param("user") User user, @Param("grantGroupId") Integer grantGroupId);

    @Update("UPDATE group_user " +
            "SET user_role = #{userRole} " +
            "WHERE group_id = #{groupId} " +
            "AND grant_group_id = #{groupId} " +
            "AND user_id = #{userId}")
    int updateUserRole(User user);

    @Delete("DELETE FROM group_user WHERE grant_group_id = #{grantGroupId} AND user_id = #{userId}")
    int deleteGroupUserByGrantGroupId(@Param("grantGroupId") Integer grantGroupId, @Param("userId") Integer userId);

    @Delete("DELETE FROM group_user WHERE user_id = #{userId}")
    int deleteGroupUserByUserId(@Param("userId") Integer userId);

    @Select("create TABLE group_user( " +
            "group_user_id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 100, INCREMENT BY 1), " +
            "group_id integer NOT NULL, " +
            "user_id integer NOT NULL, " +
            "grant_group_id integer NOT NULL, " +
            "user_role integer NOT NULL, " +
            "allowed_urls varchar(255) DEFAULT NULL, " +
            "grant_clusters varchar(255) DEFAULT NULL, " +
            "update_time timestamp NOT NULL, " +
            "PRIMARY KEY (group_user_id), " +
            "UNIQUE (user_id, group_id, grant_group_id, user_role) " +
            ")")
    void createGroupUserTable();
}
