package hello.advanced.app.v3;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV3 {

    private final LogTrace trace;
    private final OrderRepositoryV3 orderRepository;

    public void orderItem(String itemId)
    {
        TraceStatus status = trace.begin("OrderService.orderItem()");
        orderRepository.save(itemId);
        trace.end(status);
    }
}
