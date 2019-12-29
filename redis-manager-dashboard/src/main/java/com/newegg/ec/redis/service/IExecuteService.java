package com.newegg.ec.redis.service;

import com.newegg.ec.redis.entity.DataCommandsParam;

public interface IExecuteService {
    String execute(DataCommandsParam dataCommandsParam);
}
