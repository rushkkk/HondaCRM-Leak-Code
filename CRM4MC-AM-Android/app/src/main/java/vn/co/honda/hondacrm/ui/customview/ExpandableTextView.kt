package vn.co.honda.hondacrm.ui.customview

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.widget.TextView
import vn.co.honda.hondacrm.R


/**
 * Created by TienTM13 on 25/06/2019.
 */

class ExpandableTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
        TextView(context, attrs) {

    var originalText: CharSequence? = null
        private set
    private var trimmedText: CharSequence? = null
    private var bufferType: BufferType? = null
    private var trim = true
    private var trimLength: Int = 0

    private val displayableText: CharSequence?
        get() = if (trim) trimmedText else originalText

    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView)
        this.trimLength = typedArray.getInt(R.styleable.ExpandableTextView_trimLength, DEFAULT_TRIM_LENGTH)
//        typedArray.recycle()


//        setOnClickListener {
//            trim = !trim
//            setText()
//            requestFocusFromTouch()
//        }
    }


    fun setTrim(isTrim: Boolean) {
        trim = isTrim
    }

    fun setOnClick() {
        trim = !trim
        setText()
        requestFocusFromTouch()
    }

    fun getTrim(): Boolean {
        return trim
    }

    fun getLengtTrim(): Int {
        return trimLength
    }


    private fun setText() {
        super.setText(displayableText, bufferType)
    }

    override fun setText(text: CharSequence, type: BufferType) {
        originalText = text
        trimmedText = getTrimmedText()
        bufferType = type
        setText()
    }

    private fun getTrimmedText(): CharSequence? {
        return if (originalText != null && originalText!!.length > trimLength) {
            SpannableStringBuilder(originalText, 0, trimLength).append(ELLIPSIS)
        } else {
            originalText
        }
    }

//    fun setTrimLength(trimLength: Int) {
//        this.trimLength = trimLength
//        trimmedText = getTrimmedText()
//        setText()
//    }

//    fun getTrimLength(): Int {
//        return trimLength
//    }

    companion object {
        private const val DEFAULT_TRIM_LENGTH = 0
        private const val ELLIPSIS = ""
    }
}