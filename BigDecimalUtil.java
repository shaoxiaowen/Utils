package com.mmall.util;

import java.math.BigDecimal;

/**
 * @description: TODO
 * @author: xiaowen
 * @create: 2018-12-04 09:35
 **/
public class BigDecimalUtil {

    private BigDecimalUtil() {

    }
    private static BigDecimal add(double v1, double v2){
        BigDecimal b1=new BigDecimal(Double.toString(v1));
        BigDecimal b2=new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    private static BigDecimal sub(double v1, double v2){
        BigDecimal b1=new BigDecimal(Double.toString(v1));
        BigDecimal b2=new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    private static BigDecimal mul(double v1, double v2){
        BigDecimal b1=new BigDecimal(Double.toString(v1));
        BigDecimal b2=new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    private static BigDecimal div(double v1, double v2){
        BigDecimal b1=new BigDecimal(Double.toString(v1));
        BigDecimal b2=new BigDecimal(Double.toString(v2));
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);//四舍五入，保留两位小数
    }
}
