package com.huanhong.mashineshop.activity

import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import com.huanhong.mashineshop.AppApplication
import com.huanhong.mashineshop.BaseActivity
import com.huanhong.mashineshop.R
import com.huanhong.mashineshop.utils.SharedPreferencesUtils
import com.tcn.latticelpstkboard.control.TcnVendEventID
import com.tcn.latticelpstkboard.control.TcnVendEventResultID
import com.tcn.latticelpstkboard.control.TcnVendIF
import com.tcn.latticelpstkboard.control.VendEventInfo
import com.tencent.smtt.export.external.interfaces.ConsoleMessage
import com.tencent.smtt.sdk.WebChromeClient
import kotlinx.android.synthetic.main.activity_main.*
import latticelpstkdemo.TcnUtilityUI

class MainActivity : BaseActivity() {
    override fun getContentViewId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        webview.setLayerType(View.LAYER_TYPE_HARDWARE,null)
//        webview.loadUrl("http://kouhong.8126f.com/mobile.php?s=/index/index/platid/1.html")
        webview.loadUrl("https://img.aiairy.com/game/index.html")


        webview.setWebChromeClient(object : WebChromeClient() {
            override fun onConsoleMessage(p0: ConsoleMessage?): Boolean {
                if (p0 != null) {
                    if (p0.message() == "game_success") {
                        skipSuccess(SharedPreferencesUtils.readData("box_no"))
                    }
                    if (p0.message() == "game_fail") {
                        skipFailure()
                    }
                }
                return super.onConsoleMessage(p0)
            }
        })

        btn_1.setOnClickListener {
            skipSuccess(SharedPreferencesUtils.readData("box_no"))
        }

        btn_2.setOnClickListener {
            skipFailure()
        }
    }


    override fun onBackPressed() {
        startActivity(Intent(this@MainActivity, GoodsActivity::class.java))
    }

    // 游戏成功
    fun skipSuccess(number: String) {
        startActivity(Intent(this@MainActivity, SuccessActivity::class.java))
        // todo  根据number打开箱子
        open(number)
    }

    // 游戏失败
    fun skipFailure() {
        startActivity(Intent(this@MainActivity, FailureActivity::class.java))
    }

    // 打开箱子
    private fun open(number: String) {
        TcnVendIF.getInstance().reqWriteDataShipTest(Integer.valueOf(number), Integer.valueOf(number))
    }

    override fun onResume() {
        super.onResume()
        AppApplication.mediaPlayer.pause()
        TcnVendIF.getInstance().registerListener(m_vendListener)
    }

    override fun onPause() {
        super.onPause()
        TcnVendIF.getInstance().unregisterListener(m_vendListener)
    }


    private var m_vendListener: VendListener? = VendListener()

    private inner class VendListener : TcnVendIF.VendEventListener {
        override fun VendEvent(cEventInfo: VendEventInfo?) {
            if (null == cEventInfo) {
                TcnVendIF.getInstance().LoggerError(TAG, "VendListener cEventInfo is null")
                return
            }
            when (cEventInfo.m_iEventID) {
                TcnVendEventID.COMMAND_SYSTEM_BUSY -> TcnUtilityUI.getToast(this@MainActivity, cEventInfo.m_lParam4, 20).show()

                TcnVendEventID.SERIAL_PORT_CONFIG_ERROR -> {
                }
                TcnVendEventID.SERIAL_PORT_SECURITY_ERROR -> {
                }
                TcnVendEventID.SERIAL_PORT_UNKNOWN_ERROR -> {
                }
                TcnVendEventID.COMMAND_SELECT_GOODS  //选货成功
                -> TcnUtilityUI.getToast(this@MainActivity, "选货成功")
                TcnVendEventID.COMMAND_INVALID_SLOTNO -> TcnUtilityUI.getToast(this@MainActivity, getString(R.string.ui_base_notify_invalid_slot), 22).show()
                TcnVendEventID.COMMAND_SOLD_OUT -> if (cEventInfo.m_lParam1 > 0) {
                    TcnUtilityUI.getToast(this@MainActivity, getString(R.string.ui_base_aisle_name) + cEventInfo.m_lParam1 + getString(R.string.ui_base_notify_sold_out))
                } else {
                    TcnUtilityUI.getToast(this@MainActivity, getString(R.string.ui_base_notify_sold_out))
                }
                TcnVendEventID.COMMAND_FAULT_SLOTNO -> TcnUtilityUI.getToast(this@MainActivity, cEventInfo.m_lParam4)
                TcnVendEventID.COMMAND_SHIPPING    //正在出货
                -> {
                    TcnUtilityUI.getToast(this@MainActivity, "正在出货")
                }

                TcnVendEventID.COMMAND_SHIPMENT_SUCCESS    //出货成功
                -> {
                    TcnUtilityUI.getToast(this@MainActivity, "出货成功")
                }
                TcnVendEventID.COMMAND_SHIPMENT_FAILURE    //出货失败
                -> {
                    TcnUtilityUI.getToast(this@MainActivity, "出货失败")
                }
                TcnVendEventID.PROMPT_INFO -> TcnUtilityUI.getToast(this@MainActivity, cEventInfo.m_lParam4)

                TcnVendEventID.CMD_TEST_SLOT -> {
                    TcnVendIF.getInstance().LoggerDebug(TAG, "VendListener CMD_TEST_SLOT m_lParam3: " + cEventInfo.m_lParam3)

                }
                TcnVendEventID.CMD_SCAN_LIGHT_SET -> TcnUtilityUI.getToast(this@MainActivity, getString(R.string.background_lat_set_success))
                TcnVendEventID.CMD_CABINETNO_OFF -> if (cEventInfo.m_lParam1 == TcnVendEventResultID.OFF_CLOSING) {

                } else if (cEventInfo.m_lParam1 == TcnVendEventResultID.OFF_SUCCESS) {

                } else {

                }
                TcnVendEventID.CMD_REQ_PERMISSION     //授权访问文件夹
                -> {
                    TcnUtilityUI.getToast(this@MainActivity, "请选择确定")
                    ActivityCompat.requestPermissions(this@MainActivity, TcnVendIF.getInstance().getPermission(cEventInfo.m_lParam4), 126)
                }
                else -> {
                }
            }//TcnUtilityUI.getToast(m_MainActivity, getString(R.string.error_seriport));

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (webview != null) {
            webview.clearHistory()
            webview.clearCache(true)
            webview.loadUrl("about:blank")
        }
    }
}
