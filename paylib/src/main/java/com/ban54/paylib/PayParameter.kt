package com.ban54.paylib

/**
 * 支付参数
 */
abstract class PayParameter {
    companion object {
        /**
         * 支付宝支付
         */
        const val ALIPAY = 1
        /**
         * 微信支付
         */
        const val WECHAT_PAY = 2
        /**
         * 银联支付
         */
        const val UNION_PAY = 3
    }

    public abstract fun getPaymentId(): Int
}
