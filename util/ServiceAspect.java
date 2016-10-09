/**
 * 
 */
package com.zjcds.da.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;

/**
 * service层动态横切的日子记录。 <br>
 * 动态横切是通过切入点和连接点在一个方面中创建行为的过程， 连接点可以在执行时横向地应用于现有对象。 动态横切通常用于帮助向对象层次中的各种方法添加日志记录或身份认证。 在很多应用场景中，动态横切技术基本上代表了AOP。
 * 
 * @author wikimore
 * 
 */
public final class ServiceAspect {

    private final Log log = LogFactory.getLog("com.zjcds.da.service.impl");

    public void before(JoinPoint joinPoint) {
        String joinpoint = joinPoint.toString();
        //String tempmsg = joinpoint.substring(joinpoint.length() - 1);
        //StringBuffer msg = new StringBuffer(tempmsg);
        StringBuffer msg = new StringBuffer(joinpoint);
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            msg.append(" | ").append("args[").append(i).append("]=").append(args[i]);
        }
        log.info(msg);
    }

    public void after(JoinPoint joinPoint) {
        log.info("execute success.");
    }

}
