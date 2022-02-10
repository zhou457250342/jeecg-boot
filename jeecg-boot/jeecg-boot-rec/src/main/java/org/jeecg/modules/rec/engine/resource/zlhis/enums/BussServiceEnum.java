package org.jeecg.modules.rec.engine.resource.zlhis.enums;

/**
 * 业务平台交互
 */
public enum BussServiceEnum {
    OutPatient_VisitPrescriptionQuery("Visit.Prescription.Query",
            "OutPatient", "获取就诊记录", "|/GROUP/ITEM|/GROUP/ITEM/GROUP"),

    UserManager_BindCard("BindCard.BindUser.Modify",
            "UserManager", "绑定就诊卡", null),

    UserManager_Create("BindCard.CreateUser.Modify",
            "UserManager", "病人建档", null),

    UserManager_Create_NoneID("OneCard.YY.2117",
            "Custom", "病人儿童无身份证建档", null),

    UserManager_UserCardQuery("BindCard.UserInfoByCardNO.Query",
            "UserManager", "查询就诊卡", "/LIST/JZK"),

    MessagePush_Refund_HISRegCancel("Refund.HISRegCancel.Push",
            "MessagePush", "挂号退款通知", null),

    MessagePush_Refund_HISPayCancel("Refund.HISPayCancel.Push",
            "MessagePush", "HIS退费", null),

    MessagePush_Refund_HISInsure("Refund.HISInsure.Push",
            "MessagePush", "医保二次结算", null),

    MessagePush_Refund_HISInsureCancel("Refund.HISInsureCancel.Push",
            "MessagePush", "医保二次结算退费", null),

    MessagePush_Refund_HISRefPrePay("Refund.HISRefPrePay.Push",
            "MessagePush", "HIS退预交款", null),

    MessagePush_Refund_HISRefPreSettle("Refund.HISRefPreSettle.Push",
            "MessagePush", "HIS结帐退预交款", null),

    OutPatient_Register_CancelCheck("Register.CancelCheck.Query",
            "OutPatient", "检查要退的号是否允许退号", null),

    OutPatient_RegNo_Cancel("Register.Cancel.Modify",
            "OutPatient", "退号", null),

    OutPatient_RegNo_QueryTradeState("Confirm.Swap.JYState",
            "Inside", "查询交易状态", null),

    OutPatient_GuideAdviceReceiptQuery("Guide.AdviceReceipt.Query",
            "OutPatient", "缴费医嘱明细", "/YZLIST/YZ|/YZLIST/YZ/YZMX/MX|/YZLIST/YZ/DJLIST/DJ"),

    OutPatient_RegNo_Lock("Register.Lock.Modify",
            "OutPatient", "锁号", null),


    OutPatient_RegNo_unLock("Register.UnLock.Modify",
            "OutPatient", "解锁号源", null),

    OutPatient_RegNo("Register.Confirm.Modify",
            "OutPatient", "挂号", null),

    OutPatient_RegHisList("Register.RegReceipt.Query",
            "OutPatient", "挂号记录", null),


    OutPatient_HisClinicalList("Guide.RegRecord.Query",
            "OutPatient", "就诊记录(挂号记录)", "/GHLIST/GH"),


    OutPatient_UnPayRecipts("Payment.PayReceipt.Query",
            "OutPatient", "查询缴费记录(待缴费和已缴费)", null),

    OutPatient_RegistCheck("Register.RegisterCheck.Query",
            "OutPatient", "挂号检查,锁号前检查", null),

    OutPatient_cancelRegistCheck("Register.CancelCheck.Query",
            "OutPatient", "退号前检查", null),

    OutPatient_cancelRegist("Register.Cancel.Modify",
            "OutPatient", "退号", null),

    OutPatient_batchPay("Payment.BatchPay.Modify",
            "OutPatient", "批量付款", null),

    OutPatient_cancelPayCheck("Payment.CancelCheck.Query",
            "OutPatient", "退款检查", null),

    OutPatient_cancelPay("Payment.Cancel.Modify",
            "OutPatient", "退款检查", null),


    OutPatient_PreviousVisits("Visit.Record.Query",
            "OutPatient", "历次就诊记录", "/INFOLIST/INFO"),

    OutPatient_reg_source("Register.SignalSource.Query",
            "OutPatient", "可挂号源", null),


    OutPatient_reg_dept("Register.Depart.Query",
            "OutPatient", "可挂科室", null),

    OutPatient_CostHistoryList("Visit.Receipt.Query",
            "OutPatient", "查询费用历史记录", "/GROUP|/GROUP/ITEM|/GROUP/ITEM/MX"),

//    OutPatient_clinicalHistoryList("Visit.Record.Query",
//            "OutPatient", "门诊历史记录", null),


    OutPatient_ReportList("Report.Record.Query",
            "MedicalTechnology", "报告列表(门诊,住院)", null),

    OutPatient_ReportDetail("Report.XMLDetail.Query",
            "MedicalTechnology", "报告详情", "/TPWJ/ITEM"),

    InHos_Record_Query("Information.Record.Query",
            "Hospitalization", "住院记录", "/GROUP/ITEM"),

    InHos_Balance_Query("PrePayment.Balance.Query",
            "Hospitalization", "预缴余额", null),

    InHos_PrePayment_Query("PrePayment.Record.Query",
            "Hospitalization", "预缴记录", "/GROUP/ITEM"),

    InHos_PrePayment_Modify("PrePayment.Pay.Modify",
            "Hospitalization", "预缴款", null),

    InHos_Free_Query("Information.DailyPayDetail.Query",
            "Hospitalization", "一日清单", "/ITEM|/ITEM/FY/MX"),


    Custom_XWPacsReport_List("Custom.Pacsreport.List",
            "Custom", "新网pacs报告列表", null),
    Custom_XWPacsReport_Details("Custom.Pacsreport.Detail",
            "Custom", "新网pacs报告内容", null),

    /*****************************************/
//    Yxzd_ReportList("Custom.Report.Query",
//            "Custom", "报告列表(门诊,住院)", null),
//

    Yxzd_Regist_Limits("Custom.Registlimits.Check",
            "Custom", "挂号规则检查", null),

    Yxzd_Regist_Query("Custom.RegisterSignalSource.Query",
            "Custom", "可挂号源", null),

    Yxzd_OutPatient_UnPayRecipts("Custom.Ali.Getfee",
            "Custom", "(自定义)查询缴费记录(待缴费和已缴费)", null),

    Covid19_Query_DateRemain("hn.jzyy.005", "Custom", "根据日期获取剩余号数", "/Data/List/Item"),

    Covid19_Query_TimeRemain("hn.jzyy.006", "Custom", "获取时段剩余号数", "/Data/List/Item"),

    Covid19_Check("hn.jzyy.007", "Custom", "预约校验", null),

    Covid19_Appointment("hn.jzyy.008", "Custom", "预约", null),

    Covid19_Query_Appointment("hn.jzyy.009", "Custom", "查询预约是否成功", "/Data/List/Item"),

    Covid19_Query_Info("hn.jzyy.1010", "Custom", "检查项目信息", null),
    Covid19_Patient_Update("hn.jzyy.1013", "Custom", "修改病人信息", null),

    Covid19_Refund("hn.jzyy.1015", "Custom", "退款", null),
//
//
//    Yxzd_OutPatient_HisClinicalList("Custom.GuideRegRecord.Query",
//            "Custom", "就诊记录(挂号记录)", null),
//
//
//    Yxzd_OutPatient_clinicalHistoryList("Custom.VisitRecord.Query",
//            "Custom", "门诊历史记录", null),


    MessagePush_Query("hn.jzyy.messageQuery", "Custom", "消息", null),
    MessagePush_Receipt("hn.jzyy.receipt", "Custom", "消息回执", null),

    Business_TransData_Query("Business.TransData.Query", "Information", "交易明细数据", "/JYMXLIST/MX"),

    Other("Other",
            "Other", "Other", null);


    private String serviceName;
    private String description;
    private String moduleName;
    private String parseArrayPath;

    BussServiceEnum(String serviceName, String moduleName, String description, String parseArrayPath) {
        this.serviceName = serviceName;
        this.moduleName = moduleName;
        this.description = description;
        this.parseArrayPath = parseArrayPath;
    }

    public String getDescription() {
        return description;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getParseArrayPath() {
        return parseArrayPath;
    }

    public static BussServiceEnum getType(String serviceName) {
        for (BussServiceEnum constants : values()) {
            if (constants.getServiceName().equalsIgnoreCase(serviceName)) {
                return constants;
            }
        }
        return Other;
    }
}
