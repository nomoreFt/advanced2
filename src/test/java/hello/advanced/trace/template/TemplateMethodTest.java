package hello.advanced.trace.template;

import hello.advanced.trace.logtrace.LogTrace;
import hello.advanced.trace.logtrace.template.code.AbstractTemplate;
import hello.advanced.trace.logtrace.template.code.SubClassLogic1;
import hello.advanced.trace.logtrace.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class TemplateMethodTest {

    @Test
    void templateMethodV0() {
        logic1();
        logic2();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        log.info("비즈니스 로직1 실행");
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = startTime - endTime;
        log.info("resultTime={}", resultTime);
    }
    private void logic2() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        log.info("비즈니스 로직1 실행");
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = startTime - endTime;
        log.info("resultTime={}", resultTime);
    }

    /*
    템플릿 메서드 패턴 적용
     */
    @Test
    void templateMethodV1() {
        AbstractTemplate template1 = new SubClassLogic1();
        template1.execute();
        AbstractTemplate template2 = new SubClassLogic2();
        template2.execute();
    }

    @Test
    void templateMethodV2() {
        AbstractTemplate template = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("추상 클래스로 바로 구현 가변 로직 1");
            }
        };

        template.execute();
        AbstractTemplate template2 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("추상 클래스로 바로 구현 가변 로직 2");
            }
        };
        template2.execute();
    }

    @Autowired
    LogTrace logTrace;

    @Test
    void templateMethodV3() {
        new hello.advanced.trace.template.AbstractTemplate<String>(logTrace, () -> log.info("test")) {
            @Override
            protected String call(Operate operate) {
                return null;
            }
        };
    }
}
