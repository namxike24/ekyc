package ai.ftech.fekyc.common.widget.searchview

import ai.ftech.fekyc.base.extension.setOnSafeClick
import ai.ftech.fekyc.R
import ai.ftech.fekyc.utils.KeyboardUtility
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat

class SearchView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null
) : LinearLayout(ctx, attrs) {

    private lateinit var etSearchViewItm: AppCompatEditText
    private lateinit var ivSearchViewItmClear: AppCompatImageView
    private lateinit var llSearchView: LinearLayoutCompat

    private var textChangeListener: ((String) -> Unit)? = null
    private var submitListener: ((String) -> Unit)? = null
    private var clearListener: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.fekyc_search_view_layout, this, true)
        initView(attrs)
    }

    fun setTextChangeListener(textChangeListener: (String) -> Unit) {
        this.textChangeListener = textChangeListener
    }

    fun setSubmitListener(submitListener: (String) -> Unit) {
        this.submitListener = submitListener
    }

    fun setOnClearListener(clearListener: () -> Unit) {
        this.clearListener = clearListener
    }

    fun setHint(value: String) {
        etSearchViewItm.hint = value
    }

    private fun initView(attrs: AttributeSet?) {
        etSearchViewItm = findViewById(R.id.etSearchViewItm)
        ivSearchViewItmClear = findViewById(R.id.ivSearchViewItmClear)
        llSearchView = findViewById(R.id.llSearchView)

        attrs?.let {
            val ta = context.theme.obtainStyledAttributes(it, androidx.appcompat.R.styleable.SearchView, 0, 0)
            if (ta.hasValue(androidx.appcompat.R.styleable.SearchView_queryHint)) {
                val searchHint = ta.getString(androidx.appcompat.R.styleable.SearchView_queryHint)
                etSearchViewItm.hint = searchHint
            }
        }
        etSearchViewItm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {}

            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textChangeListener?.invoke(charSequence.toString())
                setVisibleButtonClear()
            }
        })

        //submit listener
        etSearchViewItm.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_SEARCH) {
                submitListener?.invoke(etSearchViewItm.text.toString())
                KeyboardUtility.hideSoftKeyboard(context, etSearchViewItm)
                true
            }
            false
        }

        etSearchViewItm.setOnFocusChangeListener { _, hasFocus ->
            llSearchView.setBackgroundResource(if (hasFocus) R.drawable.fekyc_shape_rectange_white_bg_blue_stroke_corner_8 else R.drawable.fekyc_shape_rectange_white_bg_gray_stroke_corner_8)
        }

        ivSearchViewItmClear.setOnSafeClick {
            onClickClear()
        }

        setVisibleButtonClear()
    }

    private fun setFocusNoDelay(context: Context, textView: AppCompatEditText?) {
        textView?.apply {
            requestFocus()
            val inputMethodManager: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            textView.apply {
                setSelection(text?.length ?: 0)
            }

        }
    }

    private fun setVisibleButtonClear() {
        ivSearchViewItmClear.visibility = if (etSearchViewItm.text.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
    }

    private fun onClickClear() {
        // Xóa nội dung
        etSearchViewItm.setText("")
        ivSearchViewItmClear.visibility = View.GONE

//         Hiển thị lại bàn phím hoặc comment lại nếu không muốn hiển thị bàn phím
        setFocusNoDelay(context, etSearchViewItm)
        clearListener?.invoke()
    }
}
