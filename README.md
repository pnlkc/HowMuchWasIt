# HowMuchWasIt
<br>

## 얼마였더라 앱 (제작중)
- 이전에는 얼마에 물건을 구매했는지 기억이 나지 않을 때 사용할 수 있는 앱
<br>

## 기술 사항
- 전체적인 앱의 UI 는 Compose를 사용해서 구현
- DI(의존성 주입)을 활용하여 앱 제작
- MVVM 패턴을 사용하여 앱 제작
- 로컬 db는  Room 활용
- 앱의 설정 값은 DataStore를 사용해서 구현
<br>

## 개발일지

### 230322
- 메인 화면 compose 구현
- 아이템 추가 화면 이동 구현 : Compose Navigation 사용
- 아이템 추가 화면에서 날짜 선택 기능 구현 : DatePickerDialog() 사용
<br>

### 230324
- 아이템 추가 화면 compose 구현
- HowMuchWasIt앱의 Room Database 구현
<br>

### 230327
- 아이템 추가 화면 수정 : TextField 에러, 키보드 옵션, 키보드 액션, 길이 제한
- ItemRepository 구현
- ItemAddViewModel 및 AppViewModelProvider 구현 
- ItemUiState 구현
- DI 활용을 위한 Container, Application 구현
<br>

### 230328
- AllItemList 화면 compose 구현
- AllItemListViewModel 구현
- windowSoftInputMode - adjustResize 제거 : 메인 화면에서 뷰가 깨지는 현상 발생
<br>

### 230329
- ItemEdit 화면 구현
- AllItemList 화면에서 아이템 롱클릭시 삭제 다이얼로그 보여주는 기능 추가
- 아이템 새로 추가시 AllItemList 화면으로 이동하도록 변경
- AllItemList에서 아이템 클릭시 ItemEdit 화면으로 이동하도록 설정 : Navigation Argument 사용
- 아이템 삭제 시 애니메이션 추가 : items의 lazy item scope 안에서 modifier.animateItemPlacement() 설정
<br>

### 230330
- RecentItemList 화면 compose 구현
- RoundedCornerShape한 DatePickerDialog으로 변경
- 발생한 문제들
1. 기존의 DatePickerDialog는 RoundedCornerShape로 변경할 수 없음
- 해결 방법 : RoundedCornerShape한 커스텀 DatePickerDialog 컴포저블을 만들어서 사용
2. [스택오버플로우](https://stackoverflow.com/questions/60417233/jetpack-compose-date-time-picker) 링크를 참고해서 datePickerDialog 만들었는데, CalendarView에 테마를 적용하는 코드에서 에러 발생  
- 해결 방법 : themes.xml 파일에서 `parent="ThemeOverlay.MaterialComponents.MaterialCalendar"`이 아니라 `parent="@android:style/Widget.CalendarView"`로 설정하고, `<item name="colorAccent"><day selection color></item>`가 아니라 `<item name="android:colorAccent"><day selection color></item>`로 설정하면 됨  
<br>
