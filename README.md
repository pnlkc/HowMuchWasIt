# HowMuchWasIt
<br>

## 얼마였더라 앱 (제작중)
- 이전에는 얼마에 물건을 구매했는지 기억이 나지 않을 때 사용할 수 있는 앱
<br>

## 개발 계획
- 전체적인 앱의 UI 는 Compose를 사용해서 구현
- DI(의존성 주입)을 활용하여 앱 제작
- MVVM 패턴을 사용하여 앱 제작
- 로컬 db는  Room 활용
- 앱의 설정 값은 DataStore를 사용해서 구현 (예정)
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

### 230403
- 홈화면 검색 기능 추가 : flow - debounce 기능으로 구현
- 전체 목록에서 아이템들의 이름만 보이는 리스트로 변경, 전체 목록의 아이템을 클릭하면 아이템들의 구매 내역들이 보이도록 변경
- 홈화면 뒤로가기 토스트 메세지 기능 구현
- windowSoftInputMode - adjustResize 추가 : 메인 화면에서 뷰가 깨지는 현상 해결
<br>

### 230405
- 홈화면 검색 후 클릭시 화면 이동 구현
- 홈화면 검색어 삭제 버튼 추가
- 아이템 리스트에서 최저가가 아이템 보여주는 기능 추가
- Screen 컴포저블과 ViewModedl 클래스 파일 패키지 분리
<br>

### 230410
- DI를 Hilt를 사용하여 구현하도록 변경
- -> 이유: 수동 DI와 Hilt DI를 모두 경험해보기 위해서
- 아이템 리스트 화면에서 항목이 1개일 때 삭제하면 `E/Perf: getFolderSize() : Exception_1 = java.lang.NullPointerException: Attempt to get length of null array` 발생하는 오류 수정
- -> 원인: Room DAO에서 쿼리를 통한 결과가 없을 때, Null 처리를 제대로 안해서 빈 Array에 접근하게 되는 경우가 발생
- -> 해결: Flow<Item?>을 반환하도록 수정 후 viewmodel에 null일 경우 처리 
<br>

### 230411
- 아이템을 추가할 때 구매한 항목들의 이름을 선택할 수 있도록 변경 : Dialog를 보여주고 선택하도록 함
- MainActivity에서 화면의 세로 dp를 측정하고 그에 맞춰 Dialog 최대 높이를 제한
<br>

### 230510
- 아이템 목록 화면에서 구매일 기준으로 정렬이 되지 않던 문제 해결
- -> 원인 : `ItemDAO`에서 `getItemsList` 메소드의 쿼리에 `ORDER BY`절을 작성하지 않음
- -> 해결 : 메소드의 쿼리에 `ORDER BY`절을 하여 해결
