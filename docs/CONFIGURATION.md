# 설정

## 기본 속성

```yaml
auditlog:
  enabled: true
  service-name: order-service
  env: prod
  file-path: ./logs/audit.log
  elk-enabled: false
  elk-endpoint: https://log.example.com/audit
  elk-api-key: your-api-key
  async-enabled: true
  async-thread-count: 2
  async-queue-capacity: 1000
  web-enabled: true
  sensitive-keys:
    - password
    - token
    - authorization
    - email
```

## 동작 규칙

- `enabled=false`면 starter 전체가 비활성화됩니다.
- `web-enabled=true`면 서블릿 요청 메타데이터를 자동 수집합니다.
- `elk-enabled=true`면 파일 sink와 함께 HTTP sink로 fan-out 합니다.
- `async-enabled=true`면 최종 sink를 비동기 래퍼로 감쌉니다.
- `sensitive-keys`는 `details` 키 이름에 포함되면 마스킹됩니다.
