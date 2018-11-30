package studio.inprogress.yace.app.ui.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import studio.inprogress.yace.app.YACEApp
import studio.inprogress.yace.app.di.ComponentManager
import studio.inprogress.yace.app.di.CreateComponentException

abstract class BaseActivity : MvpAppCompatActivity() {

    protected val componentManager: ComponentManager
        get() = YACEApp.app!!.componentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            initComponents()
        } catch (e: CreateComponentException) {
            e.printStackTrace()
            finish()
            return
        }

        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        if (isFinishing) {
            releaseComponents()
        }
        super.onDestroy()
    }

    @Throws(CreateComponentException::class)
    protected abstract fun initComponents()

    protected abstract fun releaseComponents()

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showToast(@StringRes resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }


    protected fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}
