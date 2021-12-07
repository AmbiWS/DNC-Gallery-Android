package com.dnc.androidgallery.core.providers

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.dnc.androidgallery.core.extensions.htmlFormat
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

interface ResourceProvider {
    fun getString(@StringRes res: Int, vararg args: Any): String

    fun getHtml(@StringRes res: Int, vararg args: Any): CharSequence

    fun getStringArray(@ArrayRes res: Int): Array<out String>

    fun getColor(@ColorRes color: Int): Int

    fun getDrawable(@DrawableRes icon: Int): Drawable?

    fun getRaw(@RawRes raw: Int): InputStream

    fun getRawString(@RawRes raw: Int): String

    fun getSP(value: Float): Float

    fun getDP(value: Float): Float

    fun getTypedValue(value: Float, unit: Int): Float

    fun getDrawableId(resName: String, packageName: String): Int

    fun getJsonDataFromAsset(fileName: String): String?
}

@Suppress("unused")
class ResourceProviderImpl(private val context: Context) : ResourceProvider {

    override fun getString(@StringRes res: Int, vararg args: Any) = context.getString(res, *args)

    override fun getHtml(res: Int, vararg args: Any): CharSequence {
        return String.htmlFormat(context.getString(res), *args)
    }

    override fun getStringArray(@ArrayRes res: Int): Array<String> = context.resources.getStringArray(res)

    @ColorInt
    override fun getColor(@ColorRes color: Int) = ContextCompat.getColor(context, color)

    override fun getDrawable(@DrawableRes icon: Int) = AppCompatResources.getDrawable(context, icon)

    override fun getRaw(@RawRes raw: Int) = context.resources.openRawResource(raw)

    override fun getRawString(@RawRes raw: Int): String {
        val inputReader = InputStreamReader(getRaw(raw))
        val bufferedReader = BufferedReader(inputReader)
        return bufferedReader.use {
            it.readText()
        }
    }

    override fun getSP(value: Float): Float = getTypedValue(value, TypedValue.COMPLEX_UNIT_SP)

    override fun getDP(value: Float): Float = getTypedValue(value, TypedValue.COMPLEX_UNIT_DIP)

    override fun getTypedValue(value: Float, unit: Int) =
        TypedValue.applyDimension(unit, value, context.resources.displayMetrics)

    override fun getDrawableId(resName: String, packageName: String) =
        context.resources.getIdentifier(resName, "drawable", packageName)

    override fun getJsonDataFromAsset(fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}
