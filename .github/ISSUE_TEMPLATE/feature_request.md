---
name: Feature request
about: Suggest an idea for this project
title: "[FEAT]"
labels: ''
assignees: ''

---

### $\bf{\normalsize{\color{yellow}POST}}$ `/api/store/{storeId}/menu`

### 담당자
- 누구누구

## 기능 이름
- 게시글 생성

## 기능 내용
- PostRequestDto 요청 시 게시글 생성한다.

예외 처리
- [RuntimeException] : 무슨 무슨 작업을 하면 RuntimeException이 발생한다.


<table>
    <tr>
        <th>Requqest Body</th>
        <th>Response Body</th>
    </tr>
    <tr>
        <td><pre lang="json">{
  "name" : "빅맥",
  "price" : "10000",
  "userId" : 1 // JWT 개발 전 임시 userId
}</pre></td>
        <td><pre lang="json">{
  "id" : 1,
  "name" : "빅맥",
  "price" : 10000,
  "state" : "SALE" // SALE(판매중), SALE_STOP(판매중지)
}</pre></td>
    </tr>
</table>
