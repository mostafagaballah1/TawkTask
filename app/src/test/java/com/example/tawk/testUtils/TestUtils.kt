package com.example.tawk.testUtils

import android.os.Build
import java.lang.reflect.Field
import java.lang.reflect.Modifier



object TestUtils {

    fun useSDK(sdk: Int) {
        setStaticField(
            Build.VERSION::class.java,
            "SDK_INT",
            sdk
        )
    }

    fun useManufacturer(name: String) {
        setStaticField(
            Build::class.java,
            "MANUFACTURER",
            name
        )
    }

    fun setProperty(instance: Any, name: String, param: Any?) {

        val field = instance.javaClass.getDeclaredField(name)
        field.isAccessible = true
        field.set(instance, param)
    }

    fun setSuperProperty(instance: Any, name: String, param: Any?) {

        instance.javaClass.superclass?.let { clazz ->
            val field = clazz.getDeclaredField(name)
            field.isAccessible = true
            field.set(instance, param)
        }?:kotlin.run {
            throw NoSuchFieldException()
        }
    }

    fun<T> getProperty(instance: Any, name: String): T {

        val field = instance.javaClass.getDeclaredField(name)
        field.isAccessible = true
        return field.get(instance) as T
    }

    fun<T> getSuperClassProperty(instance: Any, name: String): T {

        instance.javaClass.superclass?.let { clazz ->
            val field = clazz.getDeclaredField(name)
            field.isAccessible = true
            return field.get(instance) as T
        }?:kotlin.run {
            throw NoSuchFieldException()
        }
    }



    fun invokeMethod(instance: Any, methodName: String, vararg args: Any ): Any? {
        val method = instance.javaClass.getDeclaredMethod(
            methodName
        )

        method.isAccessible = true
        return method.invoke(instance, *args)
    }

    fun invokeMethod(instance: Any, methodName: String, classes: List<Class<*>>, args: List<Any?>): Any? {
        val clazz = classes.toTypedArray()
        val arguments = args.toTypedArray()

        val method = instance.javaClass.getDeclaredMethod(methodName, *clazz)
        method.isAccessible = true
        return method.invoke(instance, *arguments)
    }


    fun setStaticField(clazz: Class<*>, fieldName: String, fieldNewValue: Any) {
        try {
            setStaticField(clazz.getDeclaredField(fieldName), fieldNewValue)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    private fun setStaticField(field: Field, fieldNewValue: Any) {
        try {
            makeFieldVeryAccessible(field)
            field.set(null, fieldNewValue)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    private fun makeFieldVeryAccessible(field: Field) {
        field.isAccessible = true

        try {
            val modifiersField = Field::class.java.getDeclaredField("modifiers")
            modifiersField.isAccessible = true
            try {
                modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            }

        } catch (e: NoSuchFieldException) {
            // ignore missing fields
        }

    }
}

inline fun <reified T : Any> T.setProperty(propertyName: String, value: Any?) =
    T::class.java.getDeclaredField(propertyName)
        .apply { isAccessible = true }
        .set(this, value)

