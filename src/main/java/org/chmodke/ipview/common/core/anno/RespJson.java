package org.chmodke.ipview.common.core.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * 标记Josn响应
 *
 * @author kehao
 * @email kehao1158115@outlok.com
 * @date 2019/10/20 23:20
 */
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RespJson {
}
