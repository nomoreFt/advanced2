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

이 `logic` 이란 메서드를 동시에 Thread가 접근하여 실행하면,(여러 사용자가 동시에 사용)


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

### 사용 예시

동시성 문제가 있을 변수에 ThreadLocal로 쓰레드 별, 각각 값을 저장하게 한다.

```java
    private ThreadLocal<Long> threadLocalId = new ThreadLocal<>();
    
threadLocalId.set(3 or 4)//threadA는 3으로 저장, threadB는 4로 저장
threadLocalId.get();//threadA는 자신의 저장소에서 3을 꺼내고, B는 4를 꺼낸다.
threadLocalId.remove();//사용이 끝나면 꼭 remove()
```

### 주의사항
해당 쓰레드가 쓰레드 로컬 저장소를 모두 사용하게 되면, ThreadLocal.remove()를 사용해서 저장된 값을 해제해야 한다.

그렇지 않을 시, WAS(톰캣) 처럼 쓰레드 풀을 사용하는 경우에는 Danger 문제 발생한다.

> why?

* WAS는 절약을 위해 쓰레드 풀에 쓰레드를 미리 여러개 생성해 놓고, 사용 한 뒤 반환하는 재사용방식으로 사용하는데,
ThreadLocal에 데이터를 remove 하지 않았다면, 해당 쓰레드의 값이 유지가 되어서 다른 요청임에도
이전 사용된 값이 그대로 호출되어 사용된다.

꼭 사용이 끝나면 ThreadLocal remove()를 사용해서 사용이 끝나면 지워줘야한다.


---

# Spring 디자인 패턴

## 템플릿 메서드 패턴 (Template Method Pattern)

핵심 비즈니스 로직을 제외하고, 동일한 구조를 가진 부가 기능들을 템플릿처럼 변환해서 <br>
핵심 로직만 변환해서 찍어내는 패턴이다.

<br>

> 변하는 것과 변하지 않는 것을 분리

<br>

* 핵심 기능은 변하고 로그남기기, 트랜잭션 기능 등은 반복되고 변하지 않는다. 이 둘을 분리해서 모듈화 해야 좋은 코드다.

<br>


<img width="438" alt="스크린샷 2022-09-05 오후 9 57 34" src="https://user-images.githubusercontent.com/37995817/188454774-731ee2fa-448b-4c46-b3ee-bb4f2cff2872.png">
<br>
---
<br>

### 구현

```java
@Slf4j
public abstract class AbstractTemplate {
    public void execute() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        call();//상속
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    protected abstract void call();
}
```

* `execute()` : 가변 실행 메서드 + 앞 뒤로 넣고 싶은 공통 로직을 담은 메서드다.
*  `call()` : 교체해가면서 다른 기능을 구현하여 실행할 메서드이다. (abstract Method) 

---

```java
@Slf4j
public class SubClassLogic1 extends AbstractTemplate {

    @Override
    protected void call() {
        log.info("나에게 돈 입금");
    }
}
```

<br>

```java
@Slf4j
public class SubClassLogic2 extends AbstractTemplate {

    @Override
    protected void call() {
        log.info("내 돈 인출");
    }
}
``` 

<br>

* `내 돈 인출 & 나에게 돈 입금` : 두 가지 가변적인 비즈니스 로직을 `AbstractTemplate`의 추상 메서드인 call을 구현하여
AbstractMethod의 형변환으로 SubClassLogic1, SubClassLogic2의 execute를 각각 호출하면, call만 싹 교체된 메서드가 실행된다.

---

### 테스트

```java
@Test
    void templateMethodV1() {
        AbstractTemplate template1 = new SubClassLogic1();
        template1.execute();
        AbstractTemplate template2 = new SubClassLogic2();
        template2.execute();
    }
```

* 추상클래스를 상속받은 자식들을 부모로 형변환하여 execute를 호출한다. 자연스럽게 오버라이딩 된 최 하단의 자식 call() 메서드가 실행되기 때문에, 각각의 로직이 실행된다.

```java
22:07:19.099 [Test worker] INFO.SubClassLogic1 - 비즈니스 로직1 실행
22:07:19.109 [Test worker] INFO AbstractTemplate - resultTime=12
22:07:19.116 [Test worker] INFO SubClassLogic2 - 비즈니스 로직 2 실행
22:07:19.117 [Test worker] INFO AbstractTemplate - resultTime=1
```

<br>

---

## 콜백 패
