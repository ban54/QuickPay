package com.ban54.quickpay.test.wxapi

import com.ban54.quickpay.wx.WXPayCallbackActivity

/**
 * <Class Description>
 *
 * Created by LIHAO on 2018/12/3.
 */
class WXPayEntryActivity : WXPayCallbackActivity() {
    override val wxAppId: String
        get() = "1111111111111"
}