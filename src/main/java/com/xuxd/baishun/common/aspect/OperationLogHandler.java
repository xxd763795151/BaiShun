package com.xuxd.baishun.common.aspect;

import com.xuxd.baishun.beans.OperationType;
import com.xuxd.baishun.beans.OutObject;
import com.xuxd.baishun.beans.UserInfoOperationLog;
import com.xuxd.baishun.common.OperationLog;
import com.xuxd.baishun.service.IUserInfoOperationLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.PropertyPlaceholderHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: 许晓东
 * @Date: 20-3-29 22:05
 * @Description:
 */
@Component
@Aspect
public class OperationLogHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationLogHandler.class);

    @Autowired
    private IUserInfoOperationLogService logService;

    @Pointcut(value = "@annotation(com.xuxd.baishun.common.OperationLog)")
    public void pointcut() {
    }

    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    @AfterReturning(pointcut = "pointcut()", returning = "returnVal")
    public void afterReturning(JoinPoint joinPoint, Object returnVal) {

        executorService.execute(new LogTask(logService, joinPoint, returnVal));
    }

    /**
     * 解析 {@link Param} ,将Param的param字段（被拦截的方法的入参）解析为 {@link Properties}
     */
    private Properties parseParams(List<Param> params, int complexCounts) {
        Properties properties = new Properties();
        int index = 1;
        for (Param p :
                params) {
            if (p.isComplex) {
                Object param = p.getParam();
                Field[] fields = param.getClass().getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    // 静态字段不处理
                    if (Modifier.isStatic(fields[i].getModifiers())) {
                        continue;
                    }
                    boolean isAccessible = fields[i].isAccessible();
                    fields[i].setAccessible(true);
                    try {
                        if (fields[i].get(param) == null) {
                            fields[i].setAccessible(isAccessible);
                            continue; //值为空,就不处理了
                        }
                        if (complexCounts > 1) {
                            properties.setProperty(param.getClass().getName() + fields[i].getName(), fields[i].get(param) != null ? fields[i].get(param).toString() : null);
                        } else {
                            properties.setProperty(fields[i].getName(), fields[i].get(param) != null ? fields[i].get(param).toString() : null);
                        }
                    } catch (IllegalAccessException ignore) {
                    }
                    fields[i].setAccessible(isAccessible);
                }
            } else {
                properties.setProperty(String.valueOf(index), String.valueOf(p.getParam()));
            }
            index++;
        }
        return properties;
    }

    // 解析字符串的#{}
    private String parsePlaceholder(String str, Properties properties) {
        PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("#{", "}");
        return propertyPlaceholderHelper.replacePlaceholders(str, properties);
    }

    class Param {
        private Object param;
        // 如果param不是8种基本类型及其封装类型的实例，则为true
        private boolean isComplex;

        public Param(Object param, boolean isComplex) {
            this.param = param;
            this.isComplex = isComplex;
        }

        public Object getParam() {
            return param;
        }

        public boolean isComplex() {
            return isComplex;
        }
    }

    // 解析被拦截方法的参数，转化为property存储
    private Properties praseArgs(Object[] args) {
        int complexCounts = 0;
        List<Param> params = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if (null != args[i]) {
                if (args[i].getClass().isPrimitive()) {
                    //params.add(new Param(args[i], false));
                    throw new IllegalArgumentException("参数不能是基本类型");
                } else if ((args[i] instanceof Integer)
                        || (args[i] instanceof Double)
                        || (args[i] instanceof Character)
                        || (args[i] instanceof Boolean)
                        || (args[i] instanceof Byte)
                        || (args[i] instanceof Short)
                        || (args[i] instanceof Long)
                        || (args[i] instanceof Float)
                        || (args[i] instanceof String)) {
                    params.add(new Param(args[i], false));
                } else if (args[i] instanceof BigDecimal) {
                    params.add(new Param(String.class, false));
                } else {
                    complexCounts++;
                    params.add(new Param(args[i], true));
                }
            }
        }
        return parseParams(params, complexCounts);
    }


    class LogTask implements Runnable {

        private IUserInfoOperationLogService logService;
        private JoinPoint joinPoint;
        private Object returnVal;

        public LogTask(IUserInfoOperationLogService logService, JoinPoint joinPoint, Object returnVal) {
            this.logService = logService;
            this.joinPoint = joinPoint;
            this.returnVal = returnVal;
        }

        @Override
        public void run() {
            Object target = joinPoint.getTarget();
            Object[] args = joinPoint.getArgs();
            Class<?>[] argTypes = new Class[args.length];
            for (int i = 0; i < argTypes.length; i++) {
                argTypes[i] = args[i].getClass();
            }

            try {
                // 解析参数
                Properties properties = praseArgs(args);
                Method method = target.getClass().getMethod(joinPoint.getSignature().getName(), argTypes);
                // 解析注解
                OperationLog annotation = method.getAnnotation(OperationLog.class);
                String id = parsePlaceholder(annotation.id(), properties);
                String content = annotation.content();
                OperationType type = annotation.type();
                // 替换content的#{}
                content = parsePlaceholder(content, properties);
                if (type == OperationType.recharge) {
                    // 特殊处理
                    BigDecimal diffMoney = (BigDecimal) args[1];
                    boolean isRecharge = (boolean) args[2];
                    content = "卡号为" + id + "的用户，" + (isRecharge ? "充值操作。 充值" : "扣费操作。 扣费");
                    content += "金额/次数: " + diffMoney.toString();
                    if (!isRecharge) {
                        type = OperationType.deduction;
                    }
                }

                UserInfoOperationLog log = new UserInfoOperationLog();
                log.setUserId(id);
                log.setType(type);
                if (returnVal instanceof OutObject) {
                    OutObject outObject = (OutObject) returnVal;
                    content = outObject.getRtnCode() == 0 ? "[操作成功]  " + content : "[操作失败]  " + content;
                } else {
                    content = "该操作日志不支持记录";
                }
                log.setLog(content);
                logService.saveLog(log);
                LOGGER.info("记录一条操作日志：" + log);
            } catch (Exception e) {
                LOGGER.error("[OperateRecord:ERROR]获取需要记录的操作信息时发生异常", e);
            }
        }
    }
}
