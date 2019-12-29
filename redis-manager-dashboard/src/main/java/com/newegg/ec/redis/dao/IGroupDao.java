package com.newegg.ec.redis.dao;

import com.newegg.ec.redis.entity.Group;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Manage group
 *
 * @author Jay.H.Zou
 * @date 7/19/2019
 */
@Mapper
public interface IGroupDao {

    @Select("SELECT * FROM group2")
    List<Group> selectAllGroup();

    @Select("<script>" +
            "SELECT " +
            "distinct group2.group_id AS group_id, " +
            "group2.group_name AS group_name, " +
            "group2.group_info AS group_info, " +
            "group2.update_time AS update_time " +
            "FROM group2, group_user " +
            "WHERE group_user.grant_group_id = group2.group_id " +
            "<if test='userId != null'>" +
            "AND group_user.user_id = #{userId} " +
            "</if>" +
            "ORDER BY group_name" +
            "</script>")
    List<Group> selectGroupByUserId(@Param("userId") Integer userId);

    @Select("SELECT * FROM group2 WHERE group_name = #{groupName}")
    Group selectGroupByGroupName(String groupName);

    @Select("SELECT * FROM group2 WHERE group_id = #{groupId}")
    Group selectGroupById(Integer groupId);

    @Insert("INSERT INTO group2 (group_name, group_info, update_time) " +
            "VALUES (#{groupName, jdbcType=VARCHAR}, #{groupInfo, jdbcType=VARCHAR}, current_timestamp)")
    @Options(useGeneratedKeys = true, keyProperty = "groupId", keyColumn = "group_id")
    int insertGroup(Group group);

    @Update("UPDATE group2 SET group_name = #{groupName, jdbcType=VARCHAR}, group_info = #{groupInfo, jdbcType=VARCHAR}, update_time = current_timestamp WHERE group_id = #{groupId}")
    int updateGroup(Group group);

    @Delete("DELETE FROM group WHERE group_id = #{groupId}")
    int deleteGroupById(Integer groupId);

    @Select("create TABLE group2 (" +
            "group_id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 100, INCREMENT BY 1), " +
            "group_name varchar(255) NOT NULL, " +
            "group_info varchar(255) DEFAULT NULL, " +
            "update_time timestamp NOT NULL, " +
            "PRIMARY KEY (group_id), " +
            "UNIQUE (group_name) " +
            ")")
    void createGroupTable();
}
