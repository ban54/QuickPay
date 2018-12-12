## 一款快速接入微信/支付宝/银联三种支付方式的库，帮助项目快速接入支付功能

接入流程（kotlin版，java改成java语法即可）：

1. 在项目根目录的build.gradle中添加
>     allprojects {
>         repositories {
>             ...
>             maven { url 'https://jitpack.io' }
>          }
>     }
2. 在project的build.gralde中增加依赖
>     dependencies {
>         implementation 'com.github.ban54:QuickPay:v0.0.4'
>     }
3. 微信支付对接
* 在项目根package下建立的wxapi的子package, 并在这个package下新建一个类名为WXPayEntryActivity的类，继承自com.ban54.quickpay.wx.WXPayCallbackActivity，同时实现wxAppId的get()方法，返回接入app中微信开放平台注册的app id
* 在AndroidManifest.xml中注册创建的WXPayEntryActivity类，并设置android:exported="true"
>     <activity android:name=".wxapi.WXPayEntryActivity" android:exported="true" />
* 调用微信支付接口，代码如下（WXPayParameter中的参数根据实际下单情况填充）：
>     val payParameter = WXPayParameter()
>     payParameter.appId = "xxx"
>     payParameter.sign = "xxx"
>     payParameter.partnerId = "xxx"
>     payParameter.prepayId = "xxx"
>     payParameter.packageValue = "Sign=WXPay" // 固定值
>     payParameter.nonceStr = "xxx"
>     payParameter.timeStamp = "xxx"
>     PayHelper.pay(this, payParameter, mPayResultCallback)
mPayResultCallback的定义和使用请见_“6. mPayResultCallback的定义和使用”_。

4. 支付宝支付对接
代码如下（AlipayParameter中的参数根据实际下单情况填充）：
>     val payParameter = AlipayParameter("xxx")
>     PayHelper.pay(this, payParameter, mPayResultCallback)
mPayResultCallback的定义和使用请见_“6. mPayResultCallback的定义和使用”_。

5. 银联支付对接
代码如下（UnionPayParameter中的参数根据实际下单情况填充）：
>     val payParameter = UnionPayParameter()
>     payParameter.tn = "xxx"
>     PayHelper.pay(this, payParameter, mPayResultCallback)
mPayResultCallback的定义和使用请见_“6. mPayResultCallback的定义和使用”_。

6. mPayResultCallback的定义和使用
* 作用：mPayResultCallback为PayResultCallback类实例，用于接收支付结果的回调（在主线程中执行）
* 定义：代码如下
>     private val mPayResultCallback by lazy {
>           object : PayResultCallback {
>                override fun onSuccess(payParameter: PayParameter, payResult: PayResult) {
>                    // "支付成功"
>                }
>
>               override fun onFail(payParameter: PayParameter, payResult: PayResult) {
>                    // "支付失败: " + payResult.description
>                }
>
>               override fun onCancel(payParameter: PayParameter, payResult: PayResult) {
>                    // "支付取消"
>                }
>
>               override fun onWait(payParameter: PayParameter, payResult: PayResult) {
>                    // "支付等待"
>                }
>          }
>     }

7. 其它
* PayResult类：PayResult用于保存返回的支付结果，其中code代表支付结果状态，description代表支付结果描述，主要用于支付失败情况，subCode为错误细分码，日常使用可忽略
* 微信支付调不起：排除接入问题和参数问题，微信支付很容易在这个场景卡住->“当前手机微信账号在另外一个手机上登录后“，这种只能手动去打开一下微信，属于微信自身问题。如果还不行，可以尝试在系统app设置中清除微信APP应用数据再试。
