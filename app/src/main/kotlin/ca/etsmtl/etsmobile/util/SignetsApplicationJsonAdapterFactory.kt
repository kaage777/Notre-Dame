package ca.etsmtl.etsmobile.util

import ca.etsmtl.etsmobile.data.model.SignetsData
import ca.etsmtl.etsmobile.data.model.SignetsModel
import ca.etsmtl.etsmobile.data.model.SignetsModelAdapter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Sonphil on 26-04-18.
 */

class SignetsApplicationJsonAdapterFactory : ApplicationJsonAdapterFactory() {
    companion object {
        private val KOTSHI_APPLICATION_ADAPTER_FACTORY_INSTANCE = KotshiApplicationJsonAdapterFactory()
    }

    override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
        val rawType = Types.getRawType(type)

        return if (rawType == SignetsModel::class.java && type is ParameterizedType) {
            val subType = type.actualTypeArguments.first()
            val adapter: JsonAdapter<out SignetsData> = moshi.adapter(subType)

            SignetsModelAdapter(adapter)
        } else {
            KOTSHI_APPLICATION_ADAPTER_FACTORY_INSTANCE.create(type, annotations, moshi)
        }
    }
}