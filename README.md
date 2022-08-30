# 동시성 문제 해결(ThreadLocal)

## 동시성 문제 발생 예시

주로 싱글톤에서 static Field / 인스턴스 Field 등 작업단위에서 공용으로
변수를 접근하여 수정할 경우 동시성 문제가 발생한다.

```java
private int id;//인스턴스 변수

public int logic(int uniqueId) {
        log.info("저장 id={} -> uniqueId={}", id,uniqueId);
        id = uniqueId;
        sleep(1000);
        log.info("조회 uniqueId={}", id);
        return id;
        }
```

이 `logic` 이란 메서드를 동시에 Thread가 접근하여 실행하면, 

> 정상 동작

ThreadA -> id 3으로 저장
ThreadA -> id = 3 출력
ThreadB -> id 4로 저장
ThreadB -> id = 4 출력

3, 4 출력

> 동시에 접근시 문제

ThreadA -> id 3으로 저장
ThreadB -> id 4로 저장
ThreadA -> id 4로 출력 (같은 저장소 변수)
ThreadB -> id 4로 출력

4가 2번 출력된다.

## 해결법 - ThreadLocal

`ThreadLocal` : 쓰레드를 구분하여 쓰레드 별 변수 필드 저장을 하는 것 (동시에 같은 변수를 건드리지 않는다)
* 각 쓰레드마다 별도의 내부 저장소를 사용하기 때문.
* ThreadLocal은 thread 전용 보관소 허브같은 역할 (A - A의 보관소)

### 

### 주의 
해당 쓰레드가 쓰레드 로컬 저장소를 모두 사용하게 되면, ThreadLocal.remove()를 사용해서 저장된 값을 해제해야 한다.


