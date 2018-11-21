package com.ban54.quickpay.wx

import com.ban54.quickpay.PayParameter
import com.tencent.mm.opensdk.modelpay.PayReq

/**
 * 微信支付参数
 */
class WXPayParameter : PayParameter() {
    var appId: String? = null

    var partnerId: String? = null

    var prepayId: String? = null

    var nonceStr: String? = null

    var timeStamp: String? = null

    var packageValue: String? = null

    var sign: String? = null

    var extData: String? = null // Optional

    fun fillWXPayReq(payReq: PayReq?) {
        payReq?.apply {
            appId = appId
            partnerId = partnerId
            prepayId = prepayId
            nonceStr = nonceStr
            timeStamp = timeStamp
            packageValue = packageValue
            sign = sign
            extData = extData
        }
    }

    override fun getPaymentId(): Int {
        return WECHAT_PAY
    }
}
