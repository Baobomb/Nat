package com.baobomb.nlautouitest

class TestingLogger(val testName: String) {

    companion object {
        private val sInstanceMap: HashMap<String, TestingLogger> = HashMap()

        public fun get(testName: String): TestingLogger {
            synchronized(sInstanceMap) {
                return if (sInstanceMap.containsKey(testName)) {
                    val testingLogger = sInstanceMap[testName]
                    if (testingLogger != null && !testingLogger.isEnd) {
                        testingLogger
                    } else {
                        sInstanceMap.remove(testName)
                        val testingLoggerInstance = TestingLogger(testName)
                        sInstanceMap[testName] = testingLoggerInstance
                        testingLoggerInstance
                    }
                } else {
                    val testingLoggerInstance = TestingLogger(testName)
                    sInstanceMap[testName] = testingLoggerInstance
                    testingLoggerInstance
                }
            }
        }
    }

    var logs: ArrayList<String> = ArrayList()
    var isEnd: Boolean = false

    fun startRecordTestingLog() {
        isEnd = false
    }

    fun stopRecordTestingLog() {
        isEnd = true
    }

    fun writeLogIntoCache(logTag: String, logText: String) {
        val log = "[$logTag] : $logText"
        logs.add(log)
    }

    fun flush() {

    }
}