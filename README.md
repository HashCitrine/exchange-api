# Exchange-api

Spring Boot를 이용한 트레이딩 API 서버 제작

## Developer

- 👩‍💻 [HashCitrine](https://github.com/HashCitrine) (`webflux, oauth, MSA` 구현 및 개발 흐름 기획)
- 👩‍💻 [Soo-ss](https://github.com/Soo-ss) (`api, oauth, wallet` 구현)

## 사용 기술

- Spring Boot
- PostgreSQL
- Kafka
- Lombok

※ Kafka는 Docker container 이용.

## 개발한 서버 목록

- exchange-api
- exchange-oauth
- exchange-eureka
- exchange-gateway
- exchange-webflux
- exchange-wallet

😀 `exchange-api, exchange-oauth, exchange-wallet, exchange-webflux` 4개의 서버를 관리하기 위해, `exchange-eureka, exchange-gateway`를 통하여 MSA를 구현했습니다.

## 회원가입, 로그인

---

😁 성능상의 이유로 `select`를 최대한 안 하려고 했지만, 이 부분은 반드시 필요해 보여서 예외 처리를 먼저 했습니다.

```java
if (member.getMemberId().equals("") || member.getPassword().equals("")){
    return "아이디와 비밀번호는 반드시 입력해주세요.";
}

if (memberRepository.findByMemberId(member.getMemberId()) != null){
    return "이미 아이디가 존재합니다.";
}
```

😀 bcrypt를 이용한 비밀번호 암호화 후, 유저 wallet과 함께 member를 생성합니다.

```java
Member newMember = Member.builder()
                .memberId(member.getMemberId())
                .password(jwtAndPassword.hashPassword(member.getPassword()))
                .role(member.getRole())
                .useYn(member.getUseYn())
                .regDate(LocalDateTime.now())
                .uptDate(LocalDateTime.now())
                .build();

memberRepository.save(newMember);

Wallet newWallet = Wallet.builder()
                .memberId(member.getMemberId())
                .currency("MONEY")
                .averagePrice(0L)
                .quantity(0L)
                .build();

walletRepository.save(newWallet);
```

## 입출금 및 주문 요청

---

### ❓ Kafka 요청 서비스 흐름

`exchange-api => exchange-oauth => exchange-wallet`

1. exchange-oauth에 토큰 발급을 요청하여 토큰을 발급받습니다.
2. 입출금을 하기 위해, 요청 id와 토큰을 kafka에 전송합니다.
3. exchange-oauth는 토큰을 검증 후, 검증 된 것만 exchange-wallet에 요청 id만 kafka에 전송합니다.
4. exchange-wallet은 요청 id를 kafka를 통해 받아서 입출금을 수행합니다.
5. 주문(order)요청도 동일한 흐름입니다.

---

### ⭕ 요청 성공

✔ `ApiProducer => normalProcess`에서 토픽명을 구분합니다.

- `reqDw (Deposit, Withdraw)`
- `reqOrder`

✔ 구분 후, `sendKafka`를 통해 Kafka에 메시지 전송합니다.

✔ 요청이 성공하면 returnMsg에 해당 오브젝트명과 Request success를 띄워줍니다.

---

### ❌ 요청 실패

✔ 요청 실패시, `ApiProducer => normalProcess`의 catch문으로 진입합니다.

✔ 각종 오류로 인해 정상적으로 DB에 저장되지 않았거나 kafka로 전송되지 못한 경우, '요청실패' 상태로 처리합니다. (id 중복체크 실시하여 insert/update 판단)

✔ 실패 결과 상태조차 DB에 반영하지 못한 경우, 로그로 남깁니다.

✔ 요청이 실패하면 returnMsg에 해당 오브젝트명과 Request Fail을 띄워줍니다.

## 마무리

블라블라
