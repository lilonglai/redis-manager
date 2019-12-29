package com.newegg.ec.redis.dao;

import com.newegg.ec.redis.entity.RedisNode;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Manage redis nodes
 * 可能不需要
 *
 * @author Jay.H.Zou
 * @date 7/19/2019
 */
@Mapper
public interface IRedisNodeDao {

    @Select("SELECT * FROM redis_node WHERE cluster_id = #{clusterId}")
    List<RedisNode> selectRedisNodeListByClusterId(Integer clusterId);

    @Select("SELECT * FROM redis_node WHERE redis_node_id = #{redisNodeId}")
    RedisNode selectRedisNodeById(Integer redisNodeId);

    @Select("SELECT * FROM redis_node WHERE cluster_id = #{clusterId} AND host = #{host} AND port = #{port}")
    RedisNode existRedisNode(RedisNode redisNode);

    @Insert("<script>" +
            "INSERT INTO redis_node (group_id, cluster_id, node_id, master_id, host, port, node_role, " +
            "flags, link_state, slot_range, slot_number, container_id, container_name, insert_time, update_time) " +
            "VALUES " +
            "<foreach item='redisNode' collection='redisNodeList' separator=','>" +
            "(#{redisNode.groupId}, #{redisNode.clusterId}, #{redisNode.nodeId}, #{redisNode.masterId}, #{redisNode.host}, #{redisNode.port}, #{redisNode.nodeRole}, " +
            "#{redisNode.flags}, #{redisNode.linkState}, #{redisNode.slotRange}, #{redisNode.slotNumber}, #{redisNode.containerId}, #{redisNode.containerName}, current_timestamp, current_timestamp)" +
            "</foreach>" +
            "</script>")
    int insertRedisNodeList(@Param("redisNodeList") List<RedisNode> redisNodeList);

    @Update("UPDATE redis_node SET master_id = #{masterId}, node_role = #{nodeRole}, flags = #{flags}, " +
            "link_state = #{linkState}, slot_range = #{slotRange}, update_time = current_timestamp")
    int updateRedisNode(RedisNode redisNode);

    @Delete("DELETE FROM redis_node WHERE cluster_id = #{clusterId}")
    int deleteRedisNodeListByClusterId(Integer clusterId);

    @Delete("DELETE FROM redis_node WHERE redis_node_id = #{redisNodeId}")
    int deleteRedisNodeById(Integer redisNodeId);

    @Select("CREATE TABLE redis_node ( " +
            "redis_node_id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 100, INCREMENT BY 1), " +
            "group_id integer NOT NULL, " +
            "cluster_id integer NOT NULL, " +
            "node_id varchar(50) DEFAULT NULL, " +
            "master_id varchar(50) DEFAULT NULL, " +
            "host varchar(50) NOT NULL, " +
            "port integer NOT NULL, " +
            "node_role varchar(50) DEFAULT NULL, " +
            "flags varchar(50) DEFAULT NULL, " +
            "link_state varchar(50) DEFAULT NULL, " +
            "slot_range varchar(50) DEFAULT NULL, " +
            "slot_number integer DEFAULT NULL, " +
            "container_id varchar(255) DEFAULT NULL, " +
            "container_name varchar(60) DEFAULT NULL, " +
            "insert_time timestamp NOT NULL, " +
            "update_time timestamp NOT NULL, " +
            "PRIMARY KEY (redis_node_id) " +
            ")")
    void createRedisNodeTable();

}
