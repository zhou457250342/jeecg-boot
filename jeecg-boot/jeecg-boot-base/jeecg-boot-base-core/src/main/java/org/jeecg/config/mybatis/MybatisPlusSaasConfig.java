package org.jeecg.config.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jeecg.common.util.oConvertUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;

/**
 * 单数据源配置（jeecg.datasource.open = false时生效）
 *
 * @Author zhoujf
 */
@Configuration
@MapperScan(value = {"org.jeecg.modules.**.mapper*"})
public class MybatisPlusSaasConfig {
    /**
     * tenant_id 字段名
     */
    private static final String TENANT_FIELD_NAME = "tenant_id";
    /**
     * 哪些表需要做多租户 表需要添加一个字段 tenant_id
     */
    private static final List<String> tenantTable = new ArrayList<String>();

    static {
        tenantTable.add("demo");

//        //角色、菜单、部门
//        tenantTable.add("sys_role");
//        tenantTable.add("sys_permission");
//        tenantTable.add("sys_depart");
    }


    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                String tenant_id = oConvertUtils.getString(TenantContext.getTenant(), "0");
                return new LongValue(tenant_id);
            }

            @Override
            public String getTenantIdColumn() {
                return TENANT_FIELD_NAME;
            }

            // 返回 true 表示不走租户逻辑
            @Override
            public boolean ignoreTable(String tableName) {
                for (String temp : tenantTable) {
                    if (temp.equalsIgnoreCase(tableName)) {
                        return false;
                    }
                }
                return true;
            }
        }));
        interceptor.addInnerInterceptor(dynTableName());
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    @Bean
    public MySqlInjector mySqlInjector() {
        return new MySqlInjector();
    }


    public static ThreadLocal<String> tableModRecModel = new ThreadLocal<>();
    public static ThreadLocal<String> tableModRecComparison = new ThreadLocal<>();

    private DynamicTableNameInnerInterceptor dynTableName() {
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        HashMap<String, TableNameHandler> map = new HashMap<>();
        map.put("mod_rec_model", (sql, tableName) -> tableModRecModel.get());
        map.put("mod_rec_comparison", (sql, tableName) -> tableModRecComparison.get());
        dynamicTableNameInnerInterceptor.setTableNameHandlerMap(map);
        return dynamicTableNameInnerInterceptor;
    }

//    /**
//     * 下个版本会删除，现在为了避免缓存出现问题不得不配置
//     * @return
//     */
//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return configuration -> configuration.setUseDeprecatedExecutor(false);
//    }
//    /**
//     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
//     */
//    @Bean
//    public PerformanceInterceptor performanceInterceptor() {
//        return new PerformanceInterceptor();
//    }

}
