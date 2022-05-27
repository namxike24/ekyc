package ai.ftech.dev.base.common.bindingadapter

import ai.ftech.dev.base.extension.*
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("app:rv_set_adapter")
fun <T : RecyclerView.ViewHolder> RecyclerView.applyAdapter(applyAdapter: RecyclerView.Adapter<T>?) {
    applyAdapter?.apply {
        adapter = applyAdapter
    }
}

@BindingAdapter("app:rv_set_fix_size")
fun RecyclerView.setFixSize(set: Boolean) {
    setHasFixedSize(set)
}

@BindingAdapter("app:on_safe_click")
fun View.onSafeClick(listener: View.OnClickListener?) {
    listener?.let {
        this.setOnSafeClick { _ ->
            it.onClick(this)
        }
    }
}

@BindingAdapter("app:on_safe_global_click")
fun View.onSafeGlobalClick(listener: View.OnClickListener?) {
    listener?.let {
        this.setOnSafeGlobalClick { _ ->
            it.onClick(this)
        }
    }
}

@BindingAdapter("app:text_html")
fun TextView.setTextHtml(htmlText: String) {
    htmlText.let {
        this.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(htmlText)
        }
    }
}
