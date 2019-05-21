package com.baobomb.nlautouitest

import android.util.Log
import java.lang.reflect.Method

object ReflectionHelper {

    val LOG_TAG = ReflectionHelper::class.java.simpleName

    var operationMethodsClass: Class<*>? = null
    var prepareMethodsClass: Class<*>? = null
    var assertionMethodsClass: Class<*>? = null
    var viewClasses: Array<Class<*>>? = null

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class TestOperateMethod(val chineseName: String, val engName: String)

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class TestPrepareMethod(val chineseName: String, val engName: String)

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class TestAssertMethod(val chineseName: String, val engName: String)

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.CLASS)
    annotation class TestView(val chineseName: String, val engName: String)

    fun invokeOperationAnnotatedWithOperationName(operation: String) {
        var operationMethod: Method? = null
        var operationDetail: String? = null
        // iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation
        val allMethods = operationMethodsClass?.methods ?: OperationMethods::class.java.methods
        for (method in allMethods) {
            if (method.isAnnotationPresent(TestOperateMethod::class.java)) {
                val annotationInstance = method.getAnnotation(TestOperateMethod::class.java)
                if (operation.contains(annotationInstance.chineseName)) {
                    operationDetail = operation.substringAfter("${annotationInstance.chineseName} ", "")
                    operationMethod = method
                    break
                } else if (operation.contains(annotationInstance.engName)) {
                    operationDetail = operation.substringAfter("${annotationInstance.engName} ", "")
                    operationMethod = method
                    break
                }
            }
        }
        if (operationMethod != null) {
            val operationClassInstance = operationMethodsClass?.newInstance()
            if (!operationDetail.isNullOrEmpty()) {
                operationMethod.invoke(operationClassInstance, operationDetail)
            } else {
                operationMethod.invoke(operationClassInstance)
            }
        }
    }

    fun invokePrepareMethodsAnnotatedWithOperationName(name: String) {
        val methods = ArrayList<Method>()
        // iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation
        val allMethods = prepareMethodsClass?.methods ?: PrepareMethods::class.java.methods
        for (method in allMethods) {
            if (method.isAnnotationPresent(TestPrepareMethod::class.java)) {
                val annotationInstance = method.getAnnotation(TestPrepareMethod::class.java)
                if (annotationInstance.chineseName == name || annotationInstance.engName == name) {
                    methods.add(method)
                    break
                }
            }
        }
        if (methods.isNotEmpty()) {
            val prepareClassInstance = prepareMethodsClass?.newInstance()
            methods[0].invoke(prepareClassInstance)
        }
    }

    fun invokeAssertMethodsAnnotatedWithOperationName(assertion: String) {
        var assertionMethod: Method? = null
        var assertionTarget: String? = null
        // iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation
        val allMethods = assertionMethodsClass?.methods ?: AssertMethods::class.java.methods
        for (method in allMethods) {
            if (method.isAnnotationPresent(TestAssertMethod::class.java)) {
                val annotationInstance = method.getAnnotation(TestAssertMethod::class.java)
                if (assertion.contains(annotationInstance.chineseName)) {
                    assertionTarget = assertion.substringBefore(" ${annotationInstance.chineseName}", "")
                    assertionMethod = method
                    break
                } else if (assertion.contains(annotationInstance.engName)) {
                    assertionTarget = assertion.substringBefore(" ${annotationInstance.engName}", "")
                    assertionMethod = method
                    break
                }
            }
        }
        if (assertionMethod != null) {
            val assertionClassInstance = assertionMethodsClass?.newInstance()
            if (!assertionTarget.isNullOrEmpty()) {
                assertionMethod.invoke(assertionClassInstance, assertionTarget)
            } else {
                assertionMethod.invoke(assertionClassInstance)
            }
        }
    }

    fun invokeViewsMethodWithOperation(viewName: String, operationName: String, operationDetail: String? = null) {
        var view: Class<*>? = null
        val allViews = viewClasses ?: Views::class.java.classes
        for (viewClass in allViews) {
            if (viewClass.isAnnotationPresent(TestView::class.java)) {
                val annotationInstance = viewClass.getAnnotation(TestView::class.java)
                if (annotationInstance != null) {
                    if (viewName.contains(annotationInstance.chineseName)) {
                        view = viewClass
                        break
                    } else if (viewName.contains(annotationInstance.engName)) {
                        view = viewClass
                        break
                    }
                }
            }
        }
        view?.apply {
            try {
                if (!operationDetail.isNullOrEmpty()) {
                    val viewMethods = getMethod(operationName, String::class.java)
                    val viewInstance = view.newInstance()
                    viewMethods.invoke(viewInstance, operationDetail)
                } else {
                    val viewMethods = getMethod(operationName)
                    val viewInstance = view.newInstance()
                    viewMethods.invoke(viewInstance)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(LOG_TAG, e.stackTrace.toString())
            }
        }
    }
}