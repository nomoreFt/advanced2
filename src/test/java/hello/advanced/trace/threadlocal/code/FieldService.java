package hello.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

@Slf4j
public class FieldService {

    private String nameStore;

    public String logic(String name) {
        log.info("저장 name={} -> nameStore={}", name, nameStore);
        nameStore = name;
        sleep(1000);
        log.info("조회 nameStore={}", nameStore);
        return nameStore;
    }

    public void sleep(long milli) {
        try {
            Thread.sleep(milli);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
