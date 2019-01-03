package com.general.code;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Author: zml
 * Date  : 2019/1/2 - 19:10
 **/
@Aspect
public class TraceAspect {

    private static final String POINTCUT_METHOD = "execution(@com.general.code.DebugTrace * *(..))";

    private static final String POINTCUT_CONSTRUCTOR = "execution(@com.general.code.DebugTrace *.new(..))";

    /*
    @Before("methodAnnotatedWithDebugTrace()")
    public void beforeDebugTraceMethod(JoinPoint joinPoint){
        Log.e("zml","注解这个方法执行了："+joinPoint.getSignature().toLongString());
    }
    */

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotatedWithDebugTrace(){}

    @Pointcut(POINTCUT_CONSTRUCTOR)
    public void constructAnnotatedDebugTrace(){}



    //@Around("methodAnnotatedWithDebugTrace() || constructorAnnotatedDebugTrace()")//两个加在一起不行
    //@Before("methodAnnotatedWithDebugTrace()")
    @Around("methodAnnotatedWithDebugTrace()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();


        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 被注解的方法在这一行代码被执行
        Object result = joinPoint.proceed();
        stopWatch.stop();

        Log.i(className, buildLogMessage(methodName, stopWatch.getTotalTimeMillis()));
        return result;
    }


    private static String buildLogMessage(String methodName, long methodDuration) {
        StringBuilder message = new StringBuilder();
        message.append("zml --> ");
        message.append(methodName);
        message.append(" --> ");
        message.append("[");
        message.append(methodDuration);
        message.append("ms");
        message.append("]");

        return message.toString();
    }
}
