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
- AllItemList 화면 구현 compose 구현
- AllItemListViewModel 구현
- windowSoftInputMode - adjustResize 제거 : 메인 화면에서 뷰가 깨지는 현상 발생
<br>

### 230329
- ItemEdit 화면 구현
- AllItemList 화면에서 아이템 롱클릭시 삭제 다이얼로그 보여주는 기능 추가
- 아이템 새로 추가시 AllItemList 화면으로 이동하도록 변경
- AllItemList에서 아이템 클릭시 ItemEdit 화면으로 이동하도록 설정 : Navigation Argument 사용
<br>
