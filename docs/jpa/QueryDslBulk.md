# Bulk 연산
JPA에서는 제공하는 interface를 살펴보면 별도로 update 메서드를 제공하지 않는다. 기본적으로 영속성 컨텍스트에서 동작하는 변경감지(dirtyChecking)가 있기 때문이다. 
자세한 내용은 여기를 보고 간단히 이해하고 오면 좋을것 같다. 트랜잭션의 동작 안에서 영속상태인 객체의 값을 setter 함수로 바꾸게 되면 처음에 저장되어 있던 
스냅샷과 비교하여 변경된 부분에 대한 update 쿼리가 쓰기 지연 저장소에 저장되어 있다가 DB에 전달된다. 

특정 객체 하나만을 수정한다면 기존처럼 변경감지 기능을 활용해도 좋겠지만, 특정 조건에 해당하는 여러 명의 사람에 대한 수정 작업이 필요하다면 
한 번의 update 쿼리로 처리하는 것이 효율적이다. 

테스트를 위해서 구성된 환경을 미리 확인해보자. 
<img width="80%" src="https://github.com/yhn032/JUNIOR_DEVELOPER_TECH/assets/87313203/52cf926d-52af-4d4b-9d6b-b98549e59015"/>
<br> 위의 이미지 처럼 단순한 엔티티 구조 하나에 16개의 테스트 데이터가 들어가 있다. 스크립트 기반 초기화를 사용했고, SpringBootTest를 진행하기 때문에 매 테스트 마다 위의 이미지 처럼 기본 데이터로 시작할 것이다. 
테스트 코드를 통해서 변경감지를 이용한 수정 방법과 bulk 연산을 사용하는 수정방법을 모두 확인해보자 

## 1. 변경감지 (Dirty Checking)
```java
    @Test
    @Transactional
    public void dirtycheck(){
        //변경 전 이름을 조회 -> 트랜잭션 내에 있기 때문에 조회된 엔티티는 영속 상태이다. 
        List<Sawon> sawonList = queryFactory
                .selectFrom(sawon)
                .where(sawon.sawonId.lt(3))
                .fetch();

        sawonList.forEach(sawon -> System.out.println(sawon.getSawonName()));

        /**
         * 서비스단에 트랜잭션을 별도로 추가한다.(트랜잭션 안에 트랜잭션이 있는 구조)
         * 트랜잭션이 끝나야만 쓰기지연 저장소에 있던 쿼리가 DB에 날아가기 때문에 
         * 테스트 코드의 트랜잭션이 끝나기 전에 update 쿼리가 날아가는지 확인하기 위함이다.
         */
        sawonService.changeName(new Long[]{sawonList.get(0).getSawonId(), sawonList.get(1).getSawonId()}, new String[]{"비회원1", "비회원2"});

        sawonList = queryFactory
                .selectFrom(sawon)
                .where(sawon.sawonId.lt(3))
                .fetch();
        sawonList.forEach(sawon -> System.out.println(sawon.getSawonName()));
    }
```
<img width="80%" src="https://github.com/yhn032/JUNIOR_DEVELOPER_TECH/assets/87313203/791274a8-ccca-4fd2-b821-ad5aaf7b3c2b"/>
<br>
코드에  주석달린 바와 같이 다음의 동작순서를 따라가게 된다. 또한 결과를 보면 알겠지만 update쿼리가 변경된 객체수 만큼 날아간다. 

- 조회를 하면서 영속상태가 된 Sawon 객체들이 영속성 컨텍스트에 저장되고, 최초의 상태를 snapshot으로 남긴다.
- Service단에서 이름을 수정하면, 최초 스냅샷과 변경된 부분을 비교하여 update 쿼리를 쓰기지연 저장소에 저장한다.
- 트랜잭션이 종료되면 저장소에 있던 update 쿼리가 데이터베이스에 날라간다.
- 새롭게 조회를 한경우엔 변경된 정보를 조회할 수 있다.

## 2. 벌크 연산
```java
    @Test
    @Transactional
    public void updateBulk() throws Exception{

        //before bulk update
        List<Sawon> result = queryFactory
                .selectFrom(sawon)
                .where(sawon.sawonId.lt(3))
                .fetch();

        result.forEach(sawon -> System.out.println("sawonName = " + sawon.getSawonName()));

        long executeCnt = queryFactory
                .update(sawon)
                .set(sawon.sawonName, "비회원")
                .where(sawon.sawonId.lt(3))
                .execute();

        em.flush();
        em.clear();
        System.out.println("executeCnt = " + executeCnt);

        //after bulk update
        result = queryFactory
                .selectFrom(sawon)
                .where(sawon.sawonId.lt(3))
                .fetch();

        result.forEach(sawon -> System.out.println("sawonName = " + sawon.getSawonName()));
    }
```
![image](https://github.com/yhn032/JUNIOR_DEVELOPER_TECH/assets/87313203/3502eb9c-ac86-405c-ab5d-2c38f6a1a1d2)
<br>

1번의 변경감지와 비교했을 때, update쿼리가 하나만 날아간 것을 확인할 수 있다. 다중 row에 대해서 수정 or 삭제처리가 필요한 경우 
변경감지 보다는 벌크 연산을 하는 것이 훨씬 효율적이라는 것을 확인할 수 있다. 
벌크연산의 결과로 받은 executeCnt는 처리가 수행된 전체 row의 개수임을 기억하면 된다. 

그런데 한 가지 눈에 띄는 부분이 보이는데 바로 아래 코드 부분이다. 
```java
em.flush();
em.clear();
```

벌크연산과 변경감지를 구분하는 가장 중요한 차이점은 쿼리가 날아가는 시점이라고 볼 수 있다. 
위에서 설명했듯이 변경감지는 수정 or 삭제 작업이 일어나는 트랜잭션이 종료되는 시점에서 쿼리가 날아간다. 
하지만 벌크연산은 무수히 떠들었던 영속성 컨텍스트를 무시하고 곧바로 DB에 쿼리가 날아간다.

이 말은 즉, 벌크 연산 이후에는 트랜잭션이 종료되지 않아도 쿼리가 DB에 날아가기 때문에 영속성 컨텍스트와 DB의 데이터 상태가 달라지게 된다는 뜻이다.
말로만 들으면 이해가 어려울 수 있으니 간단한 예시를 통해서 비교해보자. 
결론부터 말하자면 특정 데이터를 조회할 경우 해당 데이터가 영속성 컨텍스트에서 관리 중인 영속상태라면 DB데이터보다 우선권을 가진다. 
위의 코드에서 영속성 컨텍스트를 플러시 및 초기화 하는 부분만 자리를 옮겨 보았다. 

```java
    @Test
    @Transactional
    public void updateBulk() throws Exception{

        //before bulk update
        List<Sawon> result = queryFactory
                .selectFrom(sawon)
                .where(sawon.sawonId.lt(3))
                .fetch();

        result.forEach(sawon -> System.out.println("sawonName = " + sawon.getSawonName()));

        long executeCnt = queryFactory
                .update(sawon)
                .set(sawon.sawonName, "비회원")
                .where(sawon.sawonId.lt(3))
                .execute();

        result = queryFactory
                .selectFrom(sawon)
                .where(sawon.sawonId.lt(3))
                .fetch();

        result.forEach(sawon -> System.out.println("sawonName = " + sawon.getSawonName()));

        em.flush();
        em.clear();
        System.out.println("executeCnt = " + executeCnt);

        //after bulk update
        result = queryFactory
                .selectFrom(sawon)
                .where(sawon.sawonId.lt(3))
                .fetch();

        result.forEach(sawon -> System.out.println("sawonName = " + sawon.getSawonName()));
    }
```
![image](https://github.com/yhn032/JUNIOR_DEVELOPER_TECH/assets/87313203/db6d1664-a705-468a-89c3-bafddd9c7af4) <br>

분명 update 쿼리가 날아갔지만 조회를 했을 때는 변경 전의 이름이 나오는 것을 확인할 수 있다. 영속성컨텍스트를 초기화 하면서 DB와 동기화가 이뤄진 후에야 
변경된 이름이 나오는 것을 볼 수 있다. 
즉, 상황을 보자면
- 벌크 연산을 함으로써 영속성컨텍스트를 무시하고 바로 DB의 데이터를 수정했다.
- 같은 데이터를 조회했지만, DB까지 가기도 전에 이미 영속성 컨텍스트가 관리하는 엔티티이기 때문에 영속성 컨텍스트가 관리하는 데이터를 가져온다.(=우선권을 갖는다.)
- 초기화 후에야 DB데이터와 동기화가 이루어지고 변경된 데이터를 조회할 수 있다.

## 3. 변경 감지 + 벌크 연산
보통 이러한 경우는 많이 없겠지만, 한 비즈니스 로직에서 특정 데이터를 조작하고, 다중 연산을 처리해야 하는데 이 두 연산의 조건이 달라서 하나는 변경감지로,
하나는 벌크 연산으로 해야 하는 경우 연산의 동작은 어떤 순서로 시작될까?
소스 코드를 먼저보고 결과를 예상해보자. 

```java
    @Test
    @Transactional
    public void dirtyAndBulk(){
        List<Sawon> sawonListUnder30 = queryFactory
                .selectFrom(sawon)
                .where(sawon.sawonAge.lt(30))
                .fetch();

        System.out.println("sawonListUnder30 = " + sawonListUnder30.size());

        //dirty checking
        sawonService.changeName(new Long[]{1L}, new String[]{"first sawon"});

        Sawon firstSawon = sawonRepository.findById(1L);
        System.out.println("firstSawon.getSawonName() = " + firstSawon.getSawonName());

        //bulk(위에서 수정한 id가 1인 사원 또한 30살 미만이다.)
        long executeCnt = queryFactory
                .update(sawon)
                .set(sawon.sawonName, "bulkSawon")
                .where(sawon.sawonAge.lt(30))
                .execute();
        em.flush();
        em.clear();
        System.out.println("executeCnt = " + executeCnt);

        firstSawon = sawonRepository.findById(1L);
        System.out.println("firstSawon.getSawonName() = " + firstSawon.getSawonName());
        Assertions.assertThat(firstSawon.getSawonName().equals("bulkSawon"));
    }
```
Return 
```sql
Hibernate: 
    select
        s1_0.sawon_id,
        s1_0.sawon_age,
        s1_0.sawon_name,
        s1_0.sawon_salary,
        s1_0.team_id 
    from
        Sawon s1_0 
    where
        s1_0.sawon_age<?
sawonListUnder30 = 9
firstSawon.getSawonName() = first sawon
Hibernate: 
    update
        Sawon 
    set
        sawon_age=?,
        sawon_name=?,
        sawon_salary=?,
        team_id=? 
    where
        sawon_id=?
Hibernate: 
    update
        Sawon 
    set
        sawon_name=? 
    where
        sawon_age<?
executeCnt = 9
Hibernate: 
    select
        s1_0.sawon_id,
        s1_0.sawon_age,
        s1_0.sawon_name,
        s1_0.sawon_salary,
        s1_0.team_id 
    from
        Sawon s1_0 
    where
        s1_0.sawon_id=?
firstSawon.getSawonName() = bulkSawon
```
결과를 보니 변경감지가 먼저 일어난 후에 벌크 연산이 진행되었다. 그 이유를 간단하게 짚어보자. 
JPA가 제공하는 객체지향 쿼리 언어인 JPQL은 실행 직전에 자동으로 플러시가 된다. 
이 말은 즉, 쓰기지연 저장소에 있던 쿼리가 DB에 날아가면서 영속성 컨텍스트와 DB의 상태가 동기화 된다는 뜻이다. 

벌크 연산을 진행하기 전에 변경감지로 생성된 update쿼리가 쓰기지연 저장소에 저장되어 있었을텐데, 
벌크 연산 또한 JPQL을 실행하는 것이기 때문에 변경감지로 생성된 쿼리가 적용된 상태에서 벌크 연산이 적용되는 것이다. 


말 장난 같지만 위의 논리를 그대로 적용하여 벌크 연산을 먼저 진행후에 변경감지를 적용시켜보면 반대로 변경감지로 변경된 이름이 저장된다. 
이와 같이 두 연산의 순서를 섞어보고, 벌크연산 이후 영속성 컨텍스트를 초기화하지 않으면서 다양한 경우의 수에 대한 연산 순서를 정리해보길 바란다.
