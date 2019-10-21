package org.chmodke.ipview.common.core.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;


/**
 * 标记控制器
 *
 * @author kehao
 * @email kehao@asiainfo.com
 * @date 2019/10/20 23:18
 */
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {

}
