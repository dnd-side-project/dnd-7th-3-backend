# dnd-7th-3-backend

## 목차
* **[Architecture](#Architecture)**
* **[개발 환경](#개발-환경)**
* **[Git 컨벤션과 브랜치 전략](#Git-컨벤션과-브랜치-전략)**

<br><hr><br>

# Architecture
![image](https://user-images.githubusercontent.com/97106584/179395355-c3f98ac1-7983-4a39-9397-f7ebb0831d3c.png)

# 개발 환경
* Java 8
* Maven
* Spring Boot (v2.5.6)
* Spring Data JPA
* Spring Security
* JWT
* Querydsl
* Redis
* Lombok
* MariaDB
* jUnit
* github actions
* aws ec2, s3

# Git 컨벤션과 브랜치 전략
## 브랜치 이름
`브랜치타입/이슈ID`
- 브랜치를 생성하고나서 PR 이후 merge된 브랜치는 삭제하자.
## 브랜치 타입과 전략
- `main` : 배포 가능한 상태만을 관리
- `develop` : 해당 브랜치를 기반으로 개발을 진행하며 모든 기능이 추가되고 버그가 수정되어 배포 가능한 상태라면 'master' 브랜치에 merge
- `feature` : 새로운 기능 개발 및 버그 수정이 필요할 때마다 'develop' 브랜치로부터 분기
- `hotfix` : 배포한 버전에 긴급하게 수정을 해야 할 필요가 있을 경우, 'master' 브랜치에서 분기하는 브랜치

![gitflow](https://github.com/dnd-side-project/dnd-7th-3-backend/blob/develop/gitflow.PNG)

## 커밋 메시지
- `feat`: 사용자를 위한 새로운 기능
- `fix`: 빌드 스크립트에 대한 수정이 아닌 사용자를 위한 버그 수정
- `docs`: 문서에 대한 변경 사항
- `style`: 형식 지정, 세미콜론 누락 등
- `refactor`: 코드 리팩토링, 예를 들어 변수 이름 바꾸기
- `test`: 누락된 테스트 추가, 테스트 리팩토링
- `chore`: 그런트 작업 업데이트 등
