package hello.advanced.app.v4;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static java.lang.Thread.sleep;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV4 {

    private final LogTrace trace;

    public void save(String itemId) {
        TraceStatus status = trace.begin("OrderRepository.save()");
        //저장 로직
        if (itemId.equals("ex")) {
            trace.exception(status, new IllegalStateException("예외 발생!"));
            throw new IllegalStateException("예외 발생!");
        }
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        trace.end(status);
    }

}
