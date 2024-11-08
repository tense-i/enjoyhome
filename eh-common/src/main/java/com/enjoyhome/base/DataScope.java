package com.enjoyhome.base;

import java.lang.annotation.*;

/**
 * DataScope
 *
 * @describe: 数据权限过滤注解
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 部门表的别名
     *
     * @return
     */
    String deptAlias() default "";

    /**
     * 用户表的别名
     *
     * @return
     */
    String userAlias() default "";
}
