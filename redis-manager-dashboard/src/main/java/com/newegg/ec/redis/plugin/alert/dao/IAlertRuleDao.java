package com.newegg.ec.redis.plugin.alert.dao;

import com.newegg.ec.redis.plugin.alert.entity.AlertRule;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Jay.H.Zou
 * @date 7/19/2019
 */
@Mapper
public interface IAlertRuleDao {

    @Select("SELECT * FROM alert_rule WHERE group_id = #{groupId} ORDER BY cluster_alert DESC")
    List<AlertRule> selectAlertRuleByGroupId(Integer groupId);

    @Select("<script>" +
            "SELECT * FROM alert_rule WHERE global2 = 1 " +
            "AND group_id = #{groupId} " +
            "<if test='ruleIdList != null and ruleIdList.size() > 0'>" +
            "OR rule_id IN " +
            "<foreach item='ruleId' collection='ruleIdList' open='(' separator=',' close=')'>" +
            "#{ruleId}" +
            "</foreach>" +
            "</if>" +
            "</script>")
    List<AlertRule> selectAlertRuleByIds(@Param("groupId") Integer groupId, @Param("ruleIdList") List<Integer> ruleIdList);

    @Select("<script>" +
            "SELECT * FROM alert_rule WHERE group_id = #{groupId} " +
            "AND global2 = 0 " +
            "<if test='ruleIdList != null and ruleIdList.size > 0'>" +
            "AND rule_id NOT IN " +
            "<foreach item='ruleId' collection='ruleIdList' open='(' separator=',' close=')'>" +
            "#{ruleId}" +
            "</foreach>" +
            "</if>" +
            "</script>")
    List<AlertRule> selectAlertRuleNotUsed(@Param("groupId") Integer groupId, @Param("ruleIdList") List<Integer> ruleIdList);

    @Select("SELECT * FROM alert_rule WHERE rule_id = #{ruleId}")
    AlertRule selectAlertRuleById(Integer ruleId);

    @Insert("INSERT INTO alert_rule (group_id, cluster_alert, rule_key, rule_value, compare_type, check_cycle, " +
            "valid, global2, rule_info, update_time, last_check_time) " +
            "VALUES (#{groupId}, #{clusterAlert}, #{ruleKey}, #{ruleValue}, #{compareType}, #{checkCycle}, " +
            "#{valid}, #{global}, #{ruleInfo}, current_timestamp, current_timestamp)")
    int insertAlertRule(AlertRule alertRule);

    @Update("UPDATE alert_rule SET group_id = #{groupId}, cluster_alert = #{clusterAlert}, rule_key = #{ruleKey}, rule_value = #{ruleValue}, " +
            "compare_type = #{compareType}, check_cycle = #{checkCycle}, valid = #{valid}, global2 = #{global}, rule_info = #{ruleInfo}, " +
            "update_time = current_timestamp " +
            "WHERE rule_id = #{ruleId}")
    int updateAlertRule(AlertRule alertRule);

    @Update("<script>" +
            "UPDATE alert_rule SET last_check_time = current_timestamp " +
            "WHERE rule_id IN " +
            "<foreach item='ruleId' collection='ruleIdList' open='(' separator=',' close=')'>" +
            "#{ruleId}" +
            "</foreach>" +
            "</script>")
    int updateAlertRuleLastCheckTime(@Param("ruleIdList") List<Integer> ruleIdList);

    @Delete("DELETE FROM alert_rule WHERE rule_id = #{ruleId}")
    int deleteAlertRuleById(Integer ruleId);

    @Delete("DELETE FROM alert_rule WHERE group_id = #{groupId}")
    int deleteAlertRuleByGroupId(Integer groupId);

    @Select("create TABLE alert_rule (" +
            "rule_id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 100, INCREMENT BY 1), " +
            "group_id integer NOT NULL, " +
            "cluster_alert smallint DEFAULT 0, " +
            "rule_key varchar(50) NOT NULL, " +
            "rule_value varchar(50) NOT NULL, " +
            "compare_type integer NOT NULL, " +
            "check_cycle integer NOT NULL, " +
            "valid smallint NOT NULL, " +
            "global2 smallint NOT NULL, " +
            "rule_info varchar(255), " +
            "update_time timestamp NOT NULL, " +
            "last_check_time timestamp NOT NULL, " +
            "PRIMARY KEY (rule_id) " +
            ")")
    void createAlertChannelTable();

}
