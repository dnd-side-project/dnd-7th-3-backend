# dnd-7th-3-backend

## ๐ป Developer

<div align="center">
<table>
  <tr>
      <td align="center"><a href="https://github.com/anjeongkyun"><img src="./images/anjeongkyun.jpeg" width="150x;" alt=""/><br /><p><b>์์ ๊ท </b></p></a><small>๐ Back-End Developer</small></td>
    <td align="center"><a href="https://github.com/haeyonghahn"><img src="./images/haeyonghahn.jpeg" width="150x;" alt=""/><br /><p><b>ํํด์ฉ</b></p></a><small>๐ Back-End Developer</small></td>
    <td align="center"><a href="https://github.com/Amenable-C"><img src="./images/Amenable-C.jpeg" width="150px;" alt=""/><br /><p><b>์ต์ฐ์ฌ</b></p></a><small>๐ Back-End Developer</small></td>
  </tr> 
</table>
</div>
<br><hr><br>

## ๋ชฉ์ฐจ
* **[Architecture](#Architecture)**
* **[๊ฐ๋ฐ ํ๊ฒฝ](#๊ฐ๋ฐ-ํ๊ฒฝ)**
* **[์ฝ๋ฉ ์ปจ๋ฒค์](#์ฝ๋ฉ-์ปจ๋ฒค์)**
* **[์ฝ๋ ํฌ๋งทํ](#์ฝ๋-ํฌ๋งทํ)**
* **[Git ์ปจ๋ฒค์๊ณผ ๋ธ๋์น ์ ๋ต](#Git-์ปจ๋ฒค์๊ณผ-๋ธ๋์น-์ ๋ต)**

<br><hr><br>

# Architecture
![image](https://user-images.githubusercontent.com/97106584/179395355-c3f98ac1-7983-4a39-9397-f7ebb0831d3c.png)

# ๊ฐ๋ฐ ํ๊ฒฝ
* Java 8
* Maven
* Spring Boot (v2.5.6)
* Spring Data JPA
* Spring Security
* Querydsl
* MariaDB
* jUnit
* github actions
* aws ec2, s3

# ์ฝ๋ฉ ์ปจ๋ฒค์
## Naming
1. ๋ณ์๋ CamelCase๋ฅผ ๊ธฐ๋ณธ์ผ๋ก ํ๋ค.
- `userEmail`, `userCellPhone` ...
2. URL, ํ์ผ๋ช ๋ฑ์ kebab-case๋ฅผ ์ฌ์ฉํ๋ค.
- `/user-email-page` ...
3. ํจํค์ง๋ช์ ๋จ์ด๊ฐ ๋ฌ๋ผ์ง๋๋ผ๋ ๋ฌด์กฐ๊ฑด ์๋ฌธ์๋ฅผ ์ฌ์ฉํ๋ค.
- `frontend`, `useremail` ...
4. ENUM์ด๋ ์์๋ ๋๋ฌธ์๋ก ๋ค์ด๋ฐํ๋ค.
- `NORMAL_STATUS` ...
5. ํจ์๋ช์ ์๋ฌธ์๋ก ์์ํ๊ณ  ๋์ฌ๋ก ๋ค์ด๋ฐํ๋ค.
- `getUserId()`, `isNormal()` ...
6. ํด๋์ค๋ช์ ๋ช์ฌ๋ก ์์ฑํ๊ณ  UpperCamelCase๋ฅผ ์ฌ์ฉํ๋ค.
- `UserEmail`, `Address` ...
7. ์ปฌ๋ ์์ ๋ณต์ํ์ ์ฌ์ฉํ๊ฑฐ๋ ์ปฌ๋ ์์ ๋ช์ํด์ค๋ค.
- `List ids`, `Map<User, Int> userToIdMap` ...
## Structure
1. ํจํค์ง๋ ๋ชฉ์ ๋ณ๋ก ๋ฌถ๋๋ค.
- `domain(domain ๊ด๋ จ ํจํค์ง)`, `common(๊ณตํต ๊ด๋ จ ํจํค์ง)`
2. ํ๋์ ๋ฉ์๋์ ํด๋์ค๋ ํ๋์ ๋ชฉ์ ์ ๋๊ฒ ๋ง๋ ๋ค.
- ํ๋์ ๋ฉ์๋ ์์์ ํ๊ฐ์ง ์ผ๋ง ํด์ผํ๋ค.
- ํ๋์ ํด๋์ค ์์์๋ ๊ฐ์ ๋ชฉ์ ์ ๋ ์ฝ๋๋ค์ ์งํฉ์ด์ฌ์ผํ๋ค.
## Programming
1. ๋ฐ๋ณต๋๋ ์ฝ๋๋ฅผ ์์ฑํ์ง ์๋๋ค.
2. ๋ณ์๋ ์ต๋ํ ์ฌ์ฉํ๋ ์์น์ ๊ฐ๊น๊ฒ ์ฌ์ฉํ๋ค.
3. ์กฐ๊ฑด๋ฌธ์ ๋ถ์ ์กฐ๊ฑด์ ๋ฃ๋ ๊ฒ์ ํผํ๋ค.
```java
if(status.isNormal()) (O) / if(!status.isAbnormal()) (X)
```

# ์ฝ๋ ํฌ๋งทํ
- tab size : 4
- indent : 4

# Git ์ปจ๋ฒค์๊ณผ ๋ธ๋์น ์ ๋ต
## ๋ธ๋์น ์ด๋ฆ
`๋ธ๋์นํ์/์ด์ID`
- ๋ธ๋์น๋ฅผ ์์ฑํ๊ณ ๋์ PR ์ดํ merge๋ ๋ธ๋์น๋ ์ญ์ ํ๋ค.
## ๋ธ๋์น ํ์๊ณผ ์ ๋ต
- `main` : ๋ฐฐํฌ ๊ฐ๋ฅํ ์ํ๋ง์ ๊ด๋ฆฌ
- `develop` : ํด๋น ๋ธ๋์น๋ฅผ ๊ธฐ๋ฐ์ผ๋ก ๊ฐ๋ฐ์ ์งํํ๋ฉฐ ๋ชจ๋  ๊ธฐ๋ฅ์ด ์ถ๊ฐ๋๊ณ  ๋ฒ๊ทธ๊ฐ ์์ ๋์ด ๋ฐฐํฌ ๊ฐ๋ฅํ ์ํ๋ผ๋ฉด 'master' ๋ธ๋์น์ merge
- `feature` : ์๋ก์ด ๊ธฐ๋ฅ ๊ฐ๋ฐ ๋ฐ ๋ฒ๊ทธ ์์ ์ด ํ์ํ  ๋๋ง๋ค 'develop' ๋ธ๋์น๋ก๋ถํฐ ๋ถ๊ธฐ
- `hotfix` : ๋ฐฐํฌํ ๋ฒ์ ์ ๊ธด๊ธํ๊ฒ ์์ ์ ํด์ผ ํ  ํ์๊ฐ ์์ ๊ฒฝ์ฐ, 'main' ๋ธ๋์น์์ ๋ถ๊ธฐํ๋ ๋ธ๋์น

![gitflow](https://github.com/dnd-side-project/dnd-7th-3-backend/blob/develop/gitflow.PNG)

## ์ปค๋ฐ ๋ฉ์์ง
```
type: ๋ด์ฉ
^--^  ^---^
|     |
|
+-------> Type: chore, docs, feat, fix, refactor, style, or test.
```
- `feat`: ์๋ก์ด ๊ธฐ๋ฅ ์ถ๊ฐ
- `fix`: ๋ฒ๊ทธ ์์ 
- `docs`: ๋ฌธ์ ์์ 
- `style`: ์ฝ๋ ํฌ๋งทํ, ์ธ๋ฏธ์ฝ๋ก  ๋๋ฝ, ์ฝ๋ ๋ณ๊ฒฝ์ด ์๋ ๊ฒฝ์ฐ
- `refactor`: ์ฝ๋ ๋ฆฌํฉํ ๋ง
- `test`: ํ์คํธ ์ฝ๋, ๋ฆฌํํ ๋ง ํ์คํธ ์ฝ๋ ์ถ๊ฐ
- `chore`: ๋น๋ ์๋ฌด ์์ , ์์กด์ฑ ํ์ผ ์์ 
