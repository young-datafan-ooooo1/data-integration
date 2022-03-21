package com.youngdatafan.portal.system.management.util;

/**
 * 注解常量类.
 *
 * @author gavin
 * @since 2018-01-16 20:37
 **/
public interface ValidatorConstans {

    /**
     * 姓名正则表达式.
     */
    String NAME = "[\\u4e00-\\u9fa5]{2,30}";

    /**
     * 身份证验证表达式.
     */
    String IDENTIFIED_CARD = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$|^[1-9]\\d{5}\\d{2}"
        +
        "((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$";

    /**
     * 手机号验证正则表达式.
     */
    String PHONE_NUMBER = "1[0-9]{10}";

    /**
     * 邮箱验证表达式.
     */
    String EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

    /**
     * 密码验证正则表达式.
     */
    String PWD = "^(?![a-z]+$)(?![A-Z]+$)(?!\\d+$)(?![$@%_]+$)[a-zA-Z][\\w$@%]{6,15}$";

    /**
     * void.
     */
    void get();
}
