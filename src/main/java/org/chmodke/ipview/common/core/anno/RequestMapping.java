package org.chmodke.ipview.common.core.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * 标记url
 *
 * @author kehao
 * @email kehao@asiainfo.com
 * @date 2019/10/20 23:20
 */
@Target({TYPE, METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    /**
     * 路径映射值
     *
     * @return
     * @author kehao
     * @email kehao@asiainfo.com
     * @date 2019/10/20 23:20
     */
    String value() default "";
}
