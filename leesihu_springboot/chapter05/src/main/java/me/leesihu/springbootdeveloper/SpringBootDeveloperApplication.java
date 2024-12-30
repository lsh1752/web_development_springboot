package me.leesihu.springbootdeveloper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
5장. '데이터베이스' 조작이 편해지는 'ORM'
    1. 데이터베이스(database)란?
        데이터를 효율적으로 보관하고 꺼내볼 수 있는 곳.

        DBMS(DataBase Management System) : 기본적으로 데이터베이스는 많은 사람이 공유할 수 있어야 하고,
            동시 접근이 가능해야 하는 등 많은 요구 사항이 존재함.
                이를 만족시키면서도 효율적으로 데이터베이스를 관리하는 체계가 DBMS.
                대부분 개발자들이 편하게 DB라고 이야기하는 MySQL, Oracle, DBeaver와 같은 것들은
                실젤로는 DB가 아니라 DBMS에 해당

            관계형 DBMS
                Relational DBMS을 줄여서 RDBMS라고 부름. 테이블 형태로 이루어진 데이터 저장소.
                RDBMS는 테이블('표'를 의미) 형태로 이루어진 테이터 저장소로, 예를 들어
                이하와 같은 회원 테이블이 있다고 가정할 때,
                갹 행은 고유의 키(PK), 즉 ID를 가지고 있고, 이메일, 나이와 같은 회원과
                관련된 값들이 들어감(Java를 기준으로는 user 클래스의 인스턴스를 떠올리면 됩니다)

               회원 테이블
                1열           2열           3열
               +----------------------------------+
               | ID         | 이메일       | 나이  | - header / column
               ------------------------------------
               | 1          | a@test.com  | 10    | - 1행
               | 2          | b@test.com  | 20    | - 2행
               | 3          | c@test.com  | 30    | - 3행
               +----------------------------------+
                기본키(PK) :
                Prime Key

            h2, MySQL
                해당 수업에서 사용할 RDBMS는 H2, MySQL
                H2 - 자바로 작성되어 있는 RDBMS : 스프링 부트가 지원하는 인메모리 관계형 데이터베이스
                    데이터를 다른 공간에 따로 보관하는 것이 아니라 애플리케이션 자체 내부에 데이터를 저장하는 특징
                    그래서 애플리케이션 재실행시 데이터는 초기화됨(서버 재실행을 기준으로).
                    간편하게 사용하기 좋아서 테스트용도로 자주 사용됨.
                    실제 서비스에서는 사용하지 않습니다.
                MySQL - 실제 서비스로 올릴 때 사용할 RDBMS -> 추후 수업 예정

            필수 용어
                1) 테이블 : DB에서 데이터를 구성하기 위한 가장 기본적인 다누이 행과 열로 구성되며
                    '행'은 여러 속성으로 이루어져있습니다.
                2) 행(row) : 테이블의 구성 요소 중 하나이며 테이블의 가로로 배열된 데이터의 집합을 의미
                    행은 반드시 고유한 식별자인 기본키(PK)를 가짐. 행을 레코드(Record)라고 부르기도 함.
                3) 열(column) : 테이블의 구성 요소 중 하나이며, 행에 저장되는 유형의 데이터.
                    예를 들어 회원 테이블이 있다고 할 때, 열은 각 요소에 대한 속성을 나타내며(Java에서는 field),
                    무결성을 보장함. 이상의 표를 기준으로 할 때는, 이메일은 문자열일 것, 나이는 숫자 유형을 가짐.
                    이메일 열에 숫자가 들어가거나, 나이 열에 문자열이 들어갈 수 없기 때문에 데이터에 대한 무결성을 보장
                4) 기본키(Primary Key) : 행을 구분할 수 있는 식별자. 이 값은 테이블에서 유일해아 하며 '중복값을 가질 수 없음'
                    보토 데이터를 수정하거나 삭제하고, 조회할 때 사용되며 다른 테이블과 관계를 맺어 데이터를 가져올 수도 있음(JOIN개념)
                    또한 기본키의 값은 수정되어서는 안 되며 유효한 값이어야 함. 즉, NULL이 될 수 없음
                        -> 이전 수업에서 nullable = false
                5) 쿼리(query) : 데이터베이스에서 데이터를 조회하거나 삭제, 생성, 수정과 같은 처리를 하기 위해 사용하는 명령문
                    SQL이라는 데이터베이스 전용 언어를 사용하여 작성함.

                    SQL(Structured Query Language) : 구조화된 질의 언어

    2. ORM이란?
        Object-Relational Mapping이라고 하며 자바의 객체와 데이터베이스를 연결하는 프로그래밍 기법
            예를 들어 데이터베이스에 age, name 컬럼에 20, 홍길동이라는값이 있다고 가정했을 때, 이것을 자바에서 사용하려면
            SQL을 이요하여 데이터를 꺼내 사용하지만, ORM이 있다면 데이터베이스의 값을 마치 객체처럼 사용할 수 있음.
            즉, SQL에 어려움을 겪는다고 하더라도 자바 언어로만 데이터베이스에 접근해서 원하는 데이터를 받아올 수 있는 방식.
            즉, 객체와 데이터베이스를 연결해 자바 언어로만 데이터베이스를 다룰 수 있도록 하는 도구를 ORM이라고 함.

        장접 :
            1) SQL을 직접 작성하지 않고 사용하는 언어(여기서는 Java)로 데이터베이스에 접근 가능
            2) 객체지향적으로 코드를 작성할 수 있기 때문에 비지니스 ㄹ로직에만 집중 가능
            3) 데이터베이스 시스템이 추상화되어있기 때문에 MySQL에서 PostgreSQL로 전환을 하더라도 추가로 드는 작업이 거의 없음
            4) 매핑하는 정보가 명확하기 때문에(DB 값과 인스턴스 변수의 값), ERD에 대한 의존도를 낮출 수 있고 유지보수시 유리

        단점 :
            1) 프로젝트의 복잡서잉 커질 수록 사용 난이도가 올라감.
            2) 복잡하고 무거운 쿼리는 ORM으로 해결이 불가능한 경우가 있음.(JOIN이 여러 번 들어가거나 / 서브쿼리가 복잡한 경우)

        스프링 데이터 JPA에서 제공하는 메서드 사용
            MemberRepository.java 파일에서 create test 해주세요.
 */
@SpringBootApplication
public class SpringBootDeveloperApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDeveloperApplication.class, args);
    }
}