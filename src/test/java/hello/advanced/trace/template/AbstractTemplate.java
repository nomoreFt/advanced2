package hello.advanced.trace.template;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;

public abstract class AbstractTemplate<T> {

    private final LogTrace trace;
    private final Operate operate;

    protected AbstractTemplate(LogTrace trace, Operate operate) {
        this.trace = trace;
        this.operate = operate;
    }

    public T execute(String message, Operate operate) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);
            //로직 호출
            T result = call(operate);

            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

    protected abstract T call(Operate operate);
}
