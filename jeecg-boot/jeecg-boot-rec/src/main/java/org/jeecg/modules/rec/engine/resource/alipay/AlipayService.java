package org.jeecg.modules.rec.engine.resource.alipay;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.rec.engine.config.RecConfig;
import org.jeecg.modules.rec.engine.model.TradeData;
import org.jeecg.modules.rec.engine.resource.alipay.common.ExcelUtils;
import org.jeecg.modules.rec.engine.resource.alipay.common.HttpUtils;
import org.jeecg.modules.rec.engine.resource.alipay.common.ZipUtils;
import org.jeecg.modules.rec.engine.resource.alipay.entity.AlipayRefundRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

/**
 * @Author: zhou x
 * @Date: 2022/2/11 15:59
 */
@Service
public class AlipayService {
    RecConfig recConfig;
    AlipayClient alipayClient;

    public AlipayService(@Autowired RecConfig recConfig) {
        this.recConfig = recConfig;
        alipayClient = new DefaultAlipayClient(recConfig.alipay_gatewayUrl(), recConfig.alipay_appId(), recConfig.alipay_appPrivateKey(), recConfig.alipay_format(), recConfig.alipay_charset(), recConfig.alipay_alipayPublicKey(), recConfig.alipay_signType());
//        try {
//            Date date = DateUtils.parseDateDef("2022-02-14", "yyyy-MM-dd");
//            tradeQuery(date);
//        } catch (Exception ex) {
//            System.out.println("asd");
//        }
    }

    public List<TradeData> tradeQuery(Date date) throws Exception {
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();//创建API对应的request类
        request.setBizContent("{" +
                "    \"bill_type\":\"trade\"," +
                "    \"bill_date\":\"" + DateUtils.formatDate(date, "yyyy-MM-dd") + "\"}"); //设置业务参数
        AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);//通过alipayClient调用API，获得对应的response类
        if ("40004".equals(response.getCode())) return null;
        if (!response.isSuccess()) throw new AlipayApiException(response.getBody());
        String zipPath = System.getProperty("user.dir") + "/alipay/bill/" + DateUtils.formatDate(date, "yyyyMMdd");
        String downPath = zipPath + ".zip";
        HttpUtils.downloadNet(response.getBillDownloadUrl(), downPath);
        ZipUtils.unZip(downPath, zipPath);
        File[] files = new File(zipPath).listFiles(op -> !op.getName().contains("汇总"));
        if (files.length < 1) throw new AlipayApiException("账单文件未找到");
        return parseBillExcel(files[0]);
    }

    public AlipayTradeRefundResponse tradeRefund(AlipayRefundRequest refundRequest) throws AlipayApiException {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setNotifyUrl(recConfig.alipay_refundNotifyUrl());
        Map<String, Object> bizMap = new HashMap<>();
        bizMap.put("refund_amount", refundRequest.getRefundPrice());
        bizMap.put("refund_reason", refundRequest.getIntro());
        bizMap.put("out_trade_no", refundRequest.getOrderNo());
        bizMap.put("trade_no", refundRequest.getTradeNo());
        bizMap.put("out_request_no", refundRequest.getRefundBatchNo());
        String bizContent = JSON.toJSONString(bizMap);
        request.setBizContent(bizContent);
        return alipayClient.execute(request);
    }

    /**
     * 解析Excel账单数据
     *
     * @param file
     * @return
     */
    private List<TradeData> parseBillExcel(File file) throws Exception {
        List<TradeData> tradeDataList = new ArrayList<>();
        ExcelUtils.readCSV(file, op -> {
            if ("#".equals(op.get(0).substring(0, 1)) || "支".equals(op.get(0).substring(0, 1))) return;
            if (!"挂号||门诊缴费||住院缴费||核酸检测".contains(op.get(3))) return;
            TradeData tradeData = new TradeData();
            tradeData.setTradeNo(StringUtils.replaceBlank(op.get(0)));
            tradeData.setOrderNo(StringUtils.replaceBlank(op.get(1)));
            tradeData.setNote(op.get(3));
            tradeData.setCreateTime(DateUtils.parseDateDef(op.get(5), "yyyy-MM-dd hh:mm:ss"));
            tradeData.setAmount(Float.parseFloat(op.get(11)));
            tradeData.setOperationNo(StringUtils.replaceBlank(op.get(21)));
            tradeDataList.add(tradeData);
        });
        return tradeDataList;
    }
}
