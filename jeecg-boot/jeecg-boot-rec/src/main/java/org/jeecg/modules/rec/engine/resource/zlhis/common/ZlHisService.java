package org.jeecg.modules.rec.engine.resource.zlhis.common;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Console;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.rec.engine.model.RecException;
import org.jeecg.modules.rec.engine.resource.zlhis.enums.BussServiceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zhou x
 * @Date: 2022/2/10 17:01
 */
@Service
public class ZlHisService {
    @Autowired
    private ZlHisUtil zlHisUtil;

    /**
     * 业务平台调用
     *
     * @return
     */
    public JSONObject httpPostBussService(BussServiceEnum service, String data) throws Exception {
        return zlHisUtil.httpPostService(service.getModuleName(), service.getServiceName(), data,
                service.getParseArrayPath() != null ? service.getParseArrayPath().split("\\|") : null);
    }

    public JSONObject doService(String moduleName, String serviceName, String data) throws Exception {
        return zlHisUtil.httpPostService(moduleName, serviceName, data);
    }

    public JSONObject doService(BussServiceEnum bussServiceEnum, String data) throws Exception {
        return httpPostBussService(bussServiceEnum, data);
    }

    public <T> List<T> doService(BussServiceEnum bussServiceEnum, String data, String rootName, Class<T> tClass) throws Exception {
        JSONObject jsonObject = doService(bussServiceEnum, data);
        if (jsonObject == null || "F".equals(jsonObject.getString("STATE"))) throw new Exception("zlhis 接口调用失败");
        if (StringUtils.isNotBlank(rootName) && jsonObject.get("CONTENT") instanceof JSONObject)
            return DataTransUtil.json2JavaObject(jsonObject.getJSONObject("CONTENT").getJSONArray(rootName), tClass);
        else
            return DataTransUtil.json2JavaObject(jsonObject.getJSONArray("CONTENT"), tClass);
    }

    public <T> List<T> doService(BussServiceEnum bussServiceEnum, String data, Class<T> tClass, String jsonPath) throws Exception {
        JSONObject jsonObject = doService(bussServiceEnum, data);
        if (StringUtils.isNotBlank(jsonPath) && jsonObject.get("CONTENT") instanceof JSONObject) {
            return DataTransUtil.json2JavaObject((JSONArray) JSONPath.eval(jsonObject.getJSONObject("CONTENT"), jsonPath), tClass);
        } else
            return DataTransUtil.json2JavaObject(jsonObject.getJSONArray("CONTENT"), tClass);
    }
}
