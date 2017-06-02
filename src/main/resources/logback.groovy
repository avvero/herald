//
// Built on Wed Oct 07 09:44:43 UTC 2015 by logback-translator
// For more information on configuration files in Groovy
// please see http://logback.qos.ch/manual/groovy.html

// For assistance related to this tool or configuration files
// in general, please contact the logback user mailing list at
//    http://qos.ch/mailman/listinfo/logback-user

// For professional support please see
//   http://www.qos.ch/shop/products/professionalSupport
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import com.avvero.flow.support.logback.MarkerSocketAppender

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.ERROR
import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.TRACE
import static ch.qos.logback.classic.Level.WARN

appender("flow", MarkerSocketAppender) {
    remoteHost = "FLOW-22088A04"
    port = 4561
    marker = "herald"
}

appender("file", RollingFileAppender) {
    file = "/var/log/service/herald/service.log"
    encoder(PatternLayoutEncoder) {
        charset = java.nio.charset.StandardCharsets.UTF_8
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} %5p %t %c{0}:%M:%L - %m%n"
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "/var/log/service/herald/service.log.%d{yyyy-MM-dd}"
    }
}

appender("stdout", ConsoleAppender) {
    target = "System.out"
    encoder(PatternLayoutEncoder) {
        charset = java.nio.charset.StandardCharsets.UTF_8
        pattern = "%d{yyyy-MM-dd HH:mm:ss} %5p \\(%mdc{userLogin},%mdc{sessionId}\\) %t %c{0}:%M:%L - %m%n"
    }
}

logger("org", ERROR, ["flow", "file", "stdout"])
logger("org.springframework", ERROR, ["flow", "file", "stdout"])
logger("org.springframework.web", INFO, ["flow", "file", "stdout"])
logger("org.springframework.web.filter", DEBUG, ["flow", "file", "stdout"])
logger("com.avvero", TRACE, ["flow", "file", "stdout"])
root(ERROR, ["flow", "file", "stdout"])