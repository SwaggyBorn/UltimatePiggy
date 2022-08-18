package com.bornproduct.ultimatepiggy.basic.lifecycle.autoclear
/**
 * 创建与此LifecycleOwner关联的[AutoClearValue]
 */
inline fun <reified T> autoClear(): AutoClearValue<T> {
    return AutoClearValue { newInstance(T::class.java) }
}

/**
 * 反射方式创建对象
 */
inline fun <reified T> newInstance(valueClass: Class<T>): T {
    try {
        return valueClass.getConstructor().newInstance()
    } catch (e: Throwable) {
        throw RuntimeException("Cannot create an instance of $valueClass", e)
    }
}

/**
 * 创建与此LifecycleOwner关联的[AutoClearValue]
 */
fun <T> autoClear(value: T) = AutoClearValue { value }

/**
 * 创建与此LifecycleOwner关联的[AutoClearValue]
 */
fun <T> autoClear(valueProvider: () -> T) = AutoClearValue(valueProvider)
