package com.example.customview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
    }

    private fun initUi() {
        swipeView.setScaleList(itemList)
    }

    companion object {
        private const val ITEM_COUNT = 10
        private val itemList = mutableListOf<String>().apply {
            repeat(ITEM_COUNT) {
                add(it.toString())
            }
        }
    }
}
