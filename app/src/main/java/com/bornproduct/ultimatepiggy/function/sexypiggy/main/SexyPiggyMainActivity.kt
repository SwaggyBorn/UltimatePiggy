package com.bornproduct.ultimatepiggy.function.sexypiggy.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bornproduct.ultimatepiggy.R
import com.bornproduct.ultimatepiggy.basic.BaseActivity
import com.bornproduct.ultimatepiggy.basic.log.Logger
import com.bornproduct.ultimatepiggy.expand.setThemeBigTypeFaceStoke
import com.bornproduct.ultimatepiggy.utils.PermissionUtil
import com.bornproduct.ultimatepiggy.function.sexypiggy.utils.PictureSdCardUtil
import kotlinx.android.synthetic.main.activity_sexy_piggy_main.de
import kotlinx.android.synthetic.main.activity_sexy_piggy_main.en

class SexyPiggyMainActivity : BaseActivity() {


  private val mViewModel by lazy {
    ViewModelProvider(this).get(SexyPiggyMainViewModel::class.java)
  }


  override fun getLayoutId(): Int {
    return R.layout.activity_sexy_piggy_main
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    en.setThemeBigTypeFaceStoke()
    en.setOnClickListener{
      mViewModel.startEncryption(true)
    }

    de.setOnClickListener{
      mViewModel.startEncryption(false)
    }

  }

  override fun onResume() {
    super.onResume()
    initData()
  }

  private fun initData() {

    //检查读写权限
    PermissionUtil.getReadWritePermission(this) { isGranted ->
      if (!isGranted) {
        finish()
      }else{
        //有权限了就建立相册并创建默认图
        PictureSdCardUtil(this).checkFileDir()
      }
    }
  }
}