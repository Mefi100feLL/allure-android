package ru.tinkoff.allure.android

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import ru.tinkoff.allure.io.TEXT_PLAIN
import ru.tinkoff.allure.io.TXT_EXTENSION
import ru.tinkoff.allure.utils.createAttachmentFile

class LogcatRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    clearLogcat()
                    base.evaluate()
                } catch (t: Throwable) {
                    dumpLogcat()
                    throw t
                }
            }
        }
    }

    private fun clearLogcat() {
        with(UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())) {
            executeShellCommand("logcat -c")
        }
    }

    private fun dumpLogcat() {
        val file = createAttachmentFile()
        with(UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())) {
            val logcatResult = executeShellCommand("logcat -d")
            file.writeText(logcatResult)
        }
        AllureAndroidLifecycle.addAttachment(name = "logcat", type = TEXT_PLAIN,
                fileExtension = TXT_EXTENSION, file = file)
    }
}