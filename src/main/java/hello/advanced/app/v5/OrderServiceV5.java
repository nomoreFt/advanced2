package hello.advanced.app.v5;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.callback.TraceTemplat4e;
import hello.advanced.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceV5 {

    private final TraceTemplat4e template;
    private final OrderRepositoryV5 orderRepository;

    public OrderServiceV5(LogTrace trace, OrderRepositoryV5 orderRepository) {
this.template = new TraceTemplat4e(trace);
        this.orderRepository = orderRepository;
    }

    public void orderItem(String itemId)
    {
        template.execute("OrderServiceV5.orderItem()", () -> {
            orderRepository.save(itemId);
            return null;
        });
    }
}
