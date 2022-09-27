package ai.ftech.ekyc.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.FragmentActivity

class KeyboardUtility {
    companion object {
        fun showKeyBoard(context: Context, editText: EditText) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, 0)
        }

        fun dontShowKeyboard(activity: Activity) {
            try {
                activity.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                hideSoftKeyboard(activity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun hideSoftKeyboard(activity: Activity) {
            try {
                val inputMethodManager: InputMethodManager =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun hideSoftKeyboard(activity: FragmentActivity) {
            try {
                val inputMethodManager: InputMethodManager =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun hideSoftKeyboard(context: Context, editText: EditText) {
            try {
                val inputMethodManager: InputMethodManager =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        fun hideSoftKeyboard(activity: Activity, view: View) {
            try {
                if (view !is EditText) {
                    view.setOnTouchListener { _, _ ->
                        hideSoftKeyboard(activity)
                        false
                    }
                }

                if (view is ViewGroup) {
                    for (i in 0 until view.childCount) {
                        hideSoftKeyboard(activity, view.getChildAt(i))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
