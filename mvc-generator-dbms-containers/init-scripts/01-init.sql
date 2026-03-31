-- 1. C## 없이 유저 생성을 가능하게 세션 설정 변경
ALTER SESSION SET "_ORACLE_SCRIPT"=true;

-- 2. CNB 테이블스페이스 생성 (파일 경로와 용량은 환경에 맞게 조정 가능)
-- 'cnb_data.dbf' 파일이 생성됩니다.
CREATE TABLESPACE cnb
DATAFILE 'cnb_data.dbf' SIZE 100M
AUTOEXTEND ON NEXT 20M MAXSIZE UNLIMITED;

-- 3. dino 유저 생성 (비밀번호는 'dino123'으로 예시)
CREATE USER dino IDENTIFIED BY "dino123"
DEFAULT TABLESPACE cnb;

-- 4. 유저에게 cnb 테이블스페이스 사용 권한(Quota) 부여
ALTER USER dino QUOTA UNLIMITED ON cnb;

-- 5. 기본 접속 및 개발 권한 부여
GRANT CONNECT, RESOURCE TO dino;

-- 6. (선택사항) 필요시 DBA 권한 부여
-- GRANT DBA TO dino;

COMMIT;
