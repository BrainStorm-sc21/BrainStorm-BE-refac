# 집에서 밥 먹장!
<img width="50%" src="https://github.com/BrainStorm-sc21/BrainStorm_sc21/assets/101620252/1ac5bdb0-1177-4111-a0ad-0b0a9c0e156c">

<br/>
<br/>

## 프로젝트 개요

1인가구를 위한 식료품 재고 관리 및 로컬 기반 공유 플랫폼, 집에서 밥 먹장!입니다.
먹장은 크게 다음과 같은 두 가지 주요 기능을 통해 1인가구에게 식재료 관리 및 활용의 편의성을 제공합니다.

1. OCR 기술을 활용하여 영수증 촬영 및 온라인 구매 내역 캡처 후 식료품 목록을 앱 냉장고에 스마트 등록
2. 사용자 위치 기반 1km 반경 내 식료품 나눔/교환/공동구매 게시글을 조회 및 작성, 채팅을 통한 사용자 간 연결

이 레포는 BrainStorm 팀의 "집에서 밥 먹장!" 백엔드 레포입니다.

<br/>

## 팀원 소개

|이름|역할|email|github|
|---|---|---|---|
|정현지|백엔드 개발|wgw777@ajou.ac.kr|https://github.com/Hyeonji-Jung|
|이강민|백엔드 개발|dlrkdals421@ajou.ac.kr|https://github.com/Kangmin909|

<br/>

## 사용 라이브러리 및 개발 환경(Spring Boot)

<img width="70%" src="https://github.com/BrainStorm-sc21/BrainStorm-BE/assets/101620252/dd8de71d-af29-44d2-8e7d-a5c3d7254e87">

<br/>

## 패키지 구조
### 기능별로 Controller, Service, Repository, Domain, DTO를 생성하여 각각 사용
- common: 프로젝트 공통 내용 관리
- user: 유저 관리
- food: 식료품 관리
- deal: 거래 관리
- chat: 채팅 기능
- notice: 알림 기능
  - controller: MVC의 컨트롤러 역할
  - service: 비지니스 로직 구현
  - repository: 데이터베이스 접근(dao)
  - domain: 엔티티 객체
  - dto: 데이터 전달 객체

<br/>

## 코드 컨벤션
[Code, Git Commit Convention](https://github.com/BrainStorm-sc21/BrainStorm-BE/wiki/%08Convention)

<br/>

## 소프트웨어 형상관리
[SCM Strategy](https://github.com/BrainStorm-sc21/BrainStorm-BE/wiki/SCM-Strategy)
