package hello.advanced.app.v4;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV4 {

    private final LogTrace trace;
    private final OrderRepositoryV4 orderRepository;

    public void orderItem(String itemId)
    {
        TraceStatus status = trace.begin("OrderService.orderItem()");
        orderRepository.save(itemId);
        trace.end(status);
    }
}
