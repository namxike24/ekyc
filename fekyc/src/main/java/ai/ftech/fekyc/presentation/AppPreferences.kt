package ai.ftech.fekyc.presentation

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

object AppPreferences {
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences
    private lateinit var gson: Gson

    private val TOKEN_KEY = "TOKEN_KEY"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(context.packageName, MODE)
        gson = Gson()
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    private inline fun SharedPreferences.commit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.commit()
    }

    var token: String?
        get() = preferences.getString(TOKEN_KEY, "")
        set(value) = preferences.edit {
            it.putString(TOKEN_KEY, value)
        }
}
