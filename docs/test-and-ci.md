# Test and CI

## 로컬 테스트 실행

```bash
./gradlew clean test
```

## 모듈 단위 테스트

```bash
./gradlew :audit-log-api:test
./gradlew :audit-log-core:test
```

## GitHub Actions

- `.github/workflows/build.yml`: branch / pull request 검증
- `.github/workflows/publish.yml`: tag 기반 publish

## 기준

- build workflow는 Maven Central credential과 signing secret을 요구하지 않는다.
- publish workflow는 tag push에서만 publish한다.
- 생성 산출물은 source tree에 포함하지 않는다.
