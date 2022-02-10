//package org.jeecg.modules.rec.engine.config;
//
//import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
//import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.HashMap;
//
///**
// * @Author: zhou x
// * @Date: 2022/2/10 14:47
// */
//@Configuration
//public class MybatisPlusConfig {
//    private static ThreadLocal<String> table = new ThreadLocal<>();
//
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor() {
//        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
//        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
//        HashMap<String, TableNameHandler> map = new HashMap<>();
//        map.put("mod_rec_model", (sql, tableName) -> table.get());
//        dynamicTableNameInnerInterceptor.setTableNameHandlerMap(map);
//        mybatisPlusInterceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
//        return mybatisPlusInterceptor;
//    }
//
//    public static void setTable(String tableName) {
//        table.set(tableName);
//    }
//    public static void clearTable() {
//        table.remove();
//    }
//}
