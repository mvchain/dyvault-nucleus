package com.mvc.dyvault.console.service;

import com.mvc.dyvault.common.swaggermock.IgnoreUpdate;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Properties;

@Component
@Intercepts({
        @Signature(type = Executor.class, method = DataChangeLastTimeInterceptor.METHOD_UPDATE, args = {
                MappedStatement.class, Object.class})})
public class DataChangeLastTimeInterceptor implements Interceptor {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String METHOD_UPDATE = "update";

    /**
     * mycat当分片规则为一致性hash时不支持 分片字段=分片字段的sql,如 update app_user set id = id where id = 1,因此在该字段不会被修改的情况下要手动去除类似语句
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        if (METHOD_UPDATE.equalsIgnoreCase(invocation.getMethod().getName()) && METHOD_UPDATE.equalsIgnoreCase(ms.getSqlCommandType().name()) && ms.getId().indexOf("updateByPrimary") > 0) {
            Object parameter = args[1];
            Executor executor = (Executor) invocation.getTarget();
            BoundSql boundSql = ms.getBoundSql(parameter);
            String sql = boundSql.getSql();
            IgnoreUpdate ann = parameter.getClass().getAnnotation(IgnoreUpdate.class);
            if (null != ann) {
                sql = sql.replace(ann.value() + " = " + ann.value() + ",", "");
            }
            PreparedStatement stat = executor.getTransaction().getConnection().prepareStatement(sql);
            for (int i = 0; i < boundSql.getParameterMappings().size(); i++) {
                Object value = FieldUtils.readDeclaredField(parameter, boundSql.getParameterMappings().get(i).getProperty(), true);
                stat.setObject(i + 1, value);
            }
            return stat.executeUpdate();
        } else {
            return invocation.proceed();
        }

    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

}