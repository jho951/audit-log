# Modules

## 모듈 목록

- `audit-log-api`
- `audit-log-core`

## 모듈 책임

- `audit-log-api`: 감사 이벤트 모델, logger 계약, sink 계약, context / masking SPI를 제공한다.
- `audit-log-core`: 기본 logger, 파일/HTTP sink, composite sink, async sink, masking utility를 제공한다.

## 모듈 규칙

- 공개 좌표와 `settings.gradle` include 목록을 일치시킨다.
- API/SPI 계약은 `audit-log-api`에 둔다.
- 프레임워크 독립 구현은 `audit-log-core`에 둔다.
- Spring/starter/platform 조립 책임은 1계층 OSS에 두지 않는다.
