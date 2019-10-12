package com.example.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import kotlinx.android.synthetic.main.layout_swipe_view.view.*
import kotlin.math.max
import kotlin.math.min

class SwipeView : ConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, attributeSetId: Int) :
            super(context, attrs, attributeSetId)

    private var scaleList: List<String>? = null

    private var selectedItem: String = ""
        set(value) {
            field = value
            swipeViewTextView.text = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_swipe_view, this, true)
    }

    fun setScaleList(list: List<String>) {
        if (list.isNotEmpty()) {
            scaleList = list
            swipeViewTextView.text = list[list.size / 2]
        }
    }

    fun getSelectedItem(): String {
        return selectedItem
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            dragHelper.cancel()
            return false
        }
        return dragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val offset = width / 2 - swipeViewTextView.width / 2
        swipeViewTextView.offsetLeftAndRight(offset)
        setSelectedItem(offset)
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private fun setSelectedItem(startingPosition: Int) {
        if (scaleList?.isNotEmpty() == true) {
            selectedItem = scaleList!![(startingPosition * scaleList!!.size) / width]
        }
    }

    private val dragHelper = ViewDragHelper.create(this, 1f, object : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child == swipeViewTextView
        }

        override fun getOrderedChildIndex(index: Int): Int {
            return indexOfChild(swipeViewTextView)
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            val leftBound = paddingLeft
            val rightBound = width - swipeViewTextView.width - paddingEnd
            return min(max(left, leftBound), rightBound)
        }

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            setSelectedItem(left + swipeViewTextView.width / 2)
        }
    })
}
