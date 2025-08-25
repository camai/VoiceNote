# VoiceNote 🎤

안드로이드 음성 녹음 및 재생 앱입니다.

## 📱 주요 기능

- **음성 녹음**: 간편한 음성 메모 녹음
- **녹음 재생**: 저장된 녹음 파일 재생 및 관리
- **설정 관리**: 오디오 품질 및 저장 설정
- **포그라운드 서비스**: 백그라운드에서도 안정적인 녹음

## 🛠 기술 스택

- **언어**: Kotlin
- **UI**: Jetpack Compose
- **아키텍처**: Clean Architecture + MVVM
- **의존성 주입**: Hilt
- **데이터베이스**: Room
- **비동기 처리**: Coroutines + Flow
- **빌드 시스템**: Gradle (Kotlin DSL)

## 📁 프로젝트 구조

```
VoiceNote/
├── app/                    # 메인 앱 모듈
├── core/                   # 공통 모듈
│   ├── data/              # 데이터 레이어
│   ├── database/          # Room 데이터베이스
│   ├── datastore/         # 설정 저장소
│   ├── domain/            # 도메인 레이어
│   └── model/             # 데이터 모델
├── feature/               # 기능 모듈
│   └── voice/             # 음성 녹음 기능
│       ├── recorder/      # 오디오 녹음/재생
│       ├── service/       # 포그라운드 서비스
│       └── ui/            # 사용자 인터페이스
└── build-logic/           # 빌드 로직
```

## 🚀 시작하기

### 필수 요구사항

- Android Studio Hedgehog | 2023.1.1 이상
- JDK 17
- Android SDK 36
- 최소 API 레벨: 27 (Android 8.1)

### 권한

앱 실행 시 다음 권한이 필요합니다:
- **녹음 권한**: 음성 녹음을 위해 필요
- **알림 권한** (Android 13+): 포그라운드 서비스 알림용
- **저장소 권한** (Android 10 이하): 파일 저장용

## 📋 주요 컴포넌트

### VoiceScreen
메인 화면으로 녹음, 재생, 설정 탭을 제공합니다.

### AudioRecorder
오디오 녹음 및 재생을 담당하는 핵심 컴포넌트입니다.

### RecordingService
포그라운드 서비스로 백그라운드에서 안정적인 녹음을 지원합니다.

### VoiceRecordRepository
녹음 파일의 저장, 조회, 삭제를 관리합니다.

### 주요 의존성
- Jetpack Compose BOM
- Hilt (의존성 주입)
- Room (데이터베이스)
- Coroutines (비동기 처리)
- Material 3 (UI 컴포넌트)
---

**개발자**: camai  
**버전**: 1.0  
**최종 업데이트**: 2025년 8월 25일
