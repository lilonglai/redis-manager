package com.newegg.ec.redis.service.impl;

import com.newegg.ec.redis.entity.DataCommandsParam;
import com.newegg.ec.redis.service.IExecuteService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

@Service
public class ExecuteService implements IExecuteService {
    public String execute(DataCommandsParam dataCommandsParam){
        try {
            if(StringUtils.isEmpty(dataCommandsParam.getCommand())){
                return "";
            }
            Process tr = Runtime.getRuntime().exec(dataCommandsParam.getCommand());
            StringBuffer buffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(tr.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
            }
            return buffer.toString();
        }
        catch (Exception e){
            return e.getMessage();
        }
    }

    public static void main(String[] args){
        DataCommandsParam dataCommandsParam = new DataCommandsParam();
        String[] newArgs = Arrays.copyOfRange(args, 0, args.length);
        dataCommandsParam.setCommand(String.join(" ", newArgs));
        ExecuteService executeService = new ExecuteService();
        String result = executeService.execute(dataCommandsParam);
        System.out.println(result);
    }
}
