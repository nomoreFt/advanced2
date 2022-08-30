package hello.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalService {

    private ThreadLocal<String> nameStore = new ThreadLocal<>();


    public String logic(String name) {
        log.info("저장 name={} -> nameStore={}", name, nameStore);
        nameStore.set(name);
        sleep(1000);
        log.info("조회 nameStore={}", nameStore.get());
        return nameStore.get();
    }

    public void sleep(long milli) {
        try {
            Thread.sleep(milli);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
