package com.newegg.ec.redis.dao;

import com.newegg.ec.redis.entity.Machine;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Manage machines
 *
 * @author Jay.H.Zou
 * @date 7/19/2019
 */
@Mapper
public interface IMachineDao {

    @Select("SELECT * FROM machine WHERE group_id = #{groupId}")
    List<Machine> selectMachineByGroupId(Integer groupId);

    @Select("SELECT DISTINCT machine_group_name FROM machine WHERE group_id = #{groupId}")
    List<String> getMachineGroupNameList(Integer groupId);

    @Select("SELECT * FROM machine WHERE machine_group_name = #{machineGroupName}")
    List<Machine> selectMachineByMachineGroup(String machineGroupName);

    @Select("<script>" +
            "SELECT * FROM machine WHERE machine_id IN " +
            "<foreach item='machineId' collection='machineIdList' open='(' separator=',' close=')'>" +
            "#{machineId}" +
            "</foreach>" +
            "</script>")
    List<Machine> selectMachineByIds(@Param("machineIdList") List<Integer> machineIdList);

    @Select("SELECT * FROM machine WHERE group_id = #{groupId} AND host = #{host}")
    List<Machine> selectMachineByHost(@Param("groupId") Integer groupId, @Param("host") String host);

    @Update("UPDATE machine SET machine_group_name = #{machineGroupName}, host = #{host}, ssh_port = #{sshPort}, password = #{password}, " +
            "token = #{token}, machine_type = #{machineType}, machine_info = #{machineInfo}, update_time = current_timestamp " +
            "WHERE machine_id = #{machineId}")
    int updateMachine(Machine machine);

    @Insert("<script>" +
            "INSERT INTO machine (machine_group_name, group_id, host, ssh_port, user_name, password, token, machine_type, machine_info, update_time) " +
            "VALUES <foreach item='machine' collection='machineList' separator=','>" +
            "(#{machine.machineGroupName}, #{machine.groupId}, #{machine.host}, #{machine.sshPort}, #{machine.userName}, #{machine.password}, " +
            "#{machine.token}, #{machine.machineType}, #{machine.machineInfo}, current_timestamp)" +
            "</foreach>" +
            "</script>")
    int insertMachine(@Param("machineList") List<Machine> machineList);

    @Delete("DELETE FROM machine WHERE machine_id = #{machineId}")
    int deleteMachineById(Integer machineId);

    @Delete("<script>" +
            "DELETE FROM machine WHERE machine_id IN " +
            "<foreach item='machineId' collection='machineIdList' open='(' separator=',' close=')'>" +
            "#{machineId}" +
            "</foreach>" +
            "</script>")
    int deleteMachineByIdBatch(@Param("machineIdList") List<Integer> machineIdList);

    @Select("create TABLE machine( " +
            "machine_id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 100, INCREMENT BY 1), " +
            "machine_group_name varchar(255) NOT NULL, " +
            "group_id integer NOT NULL, " +
            "host varchar(50) NOT NULL, " +
            "ssh_port integer NOT NULL, " +
            "user_name varchar(50) NOT NULL, " +
            "password varchar(255) DEFAULT NULL, " +
            "token varchar(255) DEFAULT NULL, " +
            "machine_type integer DEFAULT NULL, " +
            "machine_info varchar(255) DEFAULT NULL, " +
            "update_time timestamp NOT NULL, " +
            "PRIMARY KEY (machine_id) " +
            ")")
    void createMachineTable();

}
