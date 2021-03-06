package com.ban54.quickpay.test

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.ban54.quickpay.*
import com.ban54.quickpay.alipay.AlipayParameter
import com.ban54.quickpay.unpay.UnionPayParameter
import com.ban54.quickpay.wx.WXPayParameter

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val mPayResultCallback by lazy {
        object : PayResultCallback {
            val resultView = findViewById<TextView>(R.id.result)

            override fun onSuccess(payParameter: PayParameter, payResult: PayResult) {
                resultView.text = "支付成功"
                clearResult()
            }

            override fun onFail(payParameter: PayParameter, payResult: PayResult) {
                resultView.text = "支付失败: " + payResult.description
                clearResult()
            }

            override fun onCancel(payParameter: PayParameter, payResult: PayResult) {
                resultView.text = "支付取消"
                clearResult()
            }

            override fun onWait(payParameter: PayParameter, payResult: PayResult) {
                resultView.text = "支付等待"
                clearResult()
            }

            private fun clearResult() {
                resultView.postDelayed({ resultView.text = null }, 3000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.wechat -> wechatPay()
            R.id.alipay -> alipay()
            R.id.unionpay -> unionPay()
        }
    }

    private fun unionPay() {
        val payParameter = UnionPayParameter()
        payParameter.tn = "qewrqewr13534523452345"
        PayHelper.pay(this, payParameter, mPayResultCallback)
    }

    private fun wechatPay() {
        val payParameter = WXPayParameter()
        payParameter.appId = "1111111111"
        payParameter.sign = "1E624954B31F67325E4B04D35C723231"
        payParameter.partnerId = "3425222375101"
        payParameter.prepayId = "wx03110180330e234276c3c0454792366"
        payParameter.packageValue = "Sign=WXPay"
        payParameter.nonceStr = "xp7ayusbjp8jwb1e4gxq3xsnwe346la8"
        payParameter.timeStamp = "1543615347"
        PayHelper.pay(this, payParameter, mPayResultCallback)
    }

    private fun alipay() {
        val payParameter = AlipayParameter("人2332345234做鞋adsfasdfasdfasdf")
        PayHelper.pay(this, payParameter, mPayResultCallback)
    }
}
