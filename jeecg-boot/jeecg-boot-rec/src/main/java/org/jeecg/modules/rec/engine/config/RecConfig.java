package org.jeecg.modules.rec.engine.config;

import org.jeecg.common.util.DateUtils;
import org.jeecg.service.IAppDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 配置
 *
 * @Author: zhou x
 * @Date: 2022/2/9 16:44
 */
@Component
public class RecConfig {

    @Autowired
    private IAppDictItemService appDictItemService;

    /**
     * 账单下载开始时间
     *
     * @return
     */
    public Date recBillDownDate() {
        try {
            return DateUtils.parseDate(appDictItemService.getDicItemValue("1491329897692807170"), "yyyy-MM-dd");
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 对账开始时间
     *
     * @return
     */
    public Date recStartDate() {
        try {
            return DateUtils.parseDate(appDictItemService.getDicItemValue("1493479063265116161"), "yyyy-MM-dd");
        } catch (Exception ex) {
            return null;
        }
    }

    public String zlHis_serviceUrl() {
        return appDictItemService.getDicItemValue("1491713549572399105");
    }

    public String zlHis_channelName() {
        return appDictItemService.getDicItemValue("1491714855489921026");
    }

    public String zlHis_token() {
        return appDictItemService.getDicItemValue("1491714917116829698");
    }

    public String zlHis_key() {
        return appDictItemService.getDicItemValue("1491714957054992385");
    }

    public String alipay_gatewayUrl() {
        return appDictItemService.getDicItemValue("1493065176342528001");
    }

    public String alipay_appId() {
        return appDictItemService.getDicItemValue("1493065253861654529");
    }

    public String alipay_appPrivateKey() {
        return appDictItemService.getDicItemValue("1493068180248506369");
    }

    public String alipay_format() {
        return appDictItemService.getDicItemValue("1493068342140252162");
    }

    public String alipay_charset() {
        return appDictItemService.getDicItemValue("1493068401019891713");
    }

    public String alipay_alipayPublicKey() {
        return appDictItemService.getDicItemValue("1493068485782581249");
    }

    public String alipay_signType() {
        return appDictItemService.getDicItemValue("1493068544297316353");
    }

    public String alipay_refundNotifyUrl() {
        return appDictItemService.getDicItemValue("1493068633388527617");
    }
}
