package hello.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadLocalServiceTest {
    private ThreadLocalService fieldService = new ThreadLocalService();

    @Test
    void field() {
        log.info("main start");
        Runnable userA = () -> {
                fieldService.logic("userA");
        };
        Runnable userB = () -> {
            fieldService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");


        threadA.start();
        sleep(100);
        threadB.start();//동시성 문제 발생
        sleep(2000);
        log.info("main exit");
    }
    public void sleep(long milli) {
        try {
            Thread.sleep(milli);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
