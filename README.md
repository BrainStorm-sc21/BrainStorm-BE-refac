# 집에서 밥 먹장!
<img width="50%" src="https://github.com/BrainStorm-sc21/BrainStorm_sc21/assets/101620252/1ac5bdb0-1177-4111-a0ad-0b0a9c0e156c">

<br/>
<br/>

## 프로젝트 개요

'집에서 밥 먹장!'(이하 '먹장')은 1인가구를 위한 식료품 재고 관리 및 로컬 기반 공유 플랫폼입니다.
'먹장'은 크게 다음과 같은 두 가지 주요 기능을 통해 1인가구에게 식재료 관리 및 활용의 편의성을 제공합니다.  

**1. 식료품 재고 관리 서비스**
  - 가지고 있는 식료품을 등록하여 언제 어디서나 손쉽게 재고 파악 및 관리가 가능합니다.
  - 영수증 또는 온라인 장보기 서비스 구매 내역 사진을 업로드하면, OCR 기술을 통해 사진 속 텍스트를 인식하여 한 번에 여러 식료품을 등록할 수 있습니다.
  - 소비기한이 임박한 식료품에 대해 푸시알림을 드립니다.
  
**2. 로컬 기반 식료품 공유 서비스**
  - 반경 1km 내 식료품 나눔/교환/공동구매 게시글을 조회하거나 직접 등록할 수 있습니다.
  - 채팅으로 상세 거래 내용을 결정하고 거래를 진행합니다.
  - 거래 후기 시스템으로 신뢰성 있는 거래 문화를 만듭니다.

<br/>

## 팀원 소개

|이름|역할|email|github|
|---|---|---|---|
|정현지|백엔드 개발|wgw777@ajou.ac.kr|https://github.com/Hyeonji-Jung|
|이강민|백엔드 개발|dlrkdals421@ajou.ac.kr|https://github.com/Kangmin909|

<br/>

## 사용 라이브러리 및 개발 환경(Spring Boot)

<img width="100%" src="https://github.com/BrainStorm-sc21/BrainStorm-BE/assets/101620252/dd8de71d-af29-44d2-8e7d-a5c3d7254e87">

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
