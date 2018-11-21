package com.ban54.quickpay.unpay

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.content.LocalBroadcastManager
import java.lang.Error

/**
 * 银联支付结果回调类
 */
class UnionPayCallbackActivity : Activity() {

    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_FINISH_SELF -> finish()
            }
        }
    }
    // 是否已发送支付结果
    private var mSentPayResult = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_Translucent_NoTitleBar)

        dealPay()
    }

    private fun dealPay() {
        try {
            val parameter = intent.getParcelableExtra<UnionPayParameter>(UnionPayUtil.EXTRA_PAY_PARAMS)
            if (parameter == null) {
                finish()
            } else {
                UnionPayUtil.startRealPay(this, parameter.tn, parameter.phonePayType)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            finish()
        } catch (e1: Error) {
            e1.printStackTrace()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        mHandler.sendEmptyMessageDelayed(MSG_FINISH_SELF, 500)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.apply {
            if (data.hasExtra(UnionPayUtil.EXTRA_PAY_RESULT)) {
                mSentPayResult = true
                val mIntent = Intent(UnionPayUtil.ACTION_UNION_PAY_RETURN)
                mIntent.putExtras(data)
                LocalBroadcastManager.getInstance(this@UnionPayCallbackActivity).sendBroadcast(mIntent)
            }
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
        if (!mSentPayResult) {
            val mIntent = Intent(UnionPayUtil.ACTION_UNION_PAY_RETURN)
            LocalBroadcastManager.getInstance(this@UnionPayCallbackActivity).sendBroadcast(mIntent)
        }
    }

    companion object {
        private const val MSG_FINISH_SELF = 0
    }
}
