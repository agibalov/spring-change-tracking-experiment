package me.loki2302;

import me.loki2302.changelog.ChangeLog;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SaveChangeLogWhenControllerMethodReturnsAspect {
    @Autowired
    private ChangeLog changeLog;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void withinController() {
    }

    @Pointcut("execution(* *(..))")
    public void anyMethod() {
    }

    @AfterReturning("withinController() && anyMethod()")
    public void whenControllerMethodReturns() {
        changeLog.saveLog();
    }
}
