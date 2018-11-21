package com.ban54.paylib.wx

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * 微信支付回调activity
 */
abstract class WXPayCallbackActivity : Activity(), IWXAPIEventHandler {
    private var api: IWXAPI? = null

    /**
     * 获取微信公众平台注册的APP ID
     *
     * @return
     */
    protected abstract val wxAppId: String

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_Translucent_NoTitleBar)

        api = WXAPIFactory.createWXAPI(this, wxAppId)
        api?.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api?.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {}

    override fun onResp(resp: BaseResp) {
        if (resp.getType() === ConstantsAPI.COMMAND_PAY_BY_WX) {
            val payResp = resp as PayResp
            val extData = payResp.extData
            if (!extData.startsWith(WXPayUtil.WX_PAY_START_STRING)) {
                return
            }
            val intent = Intent(WXPayUtil.ACTION_WX_PAY_RETURN)
            intent.putExtra(WXPayUtil.EXTRA_RESULT_CODE, resp.errCode)
            intent.putExtra(WXPayUtil.EXTRA_RESULT_DESC, resp.errStr)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            finish()
        }
    }
}