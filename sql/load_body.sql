CREATE OR REPLACE PACKAGE BODY load AS

    PROCEDURE worker (
        sleep   NUMBER,
        cnt     IN OUT NUMBER,
        istruncate NUMBER
    ) IS
        id    NUMBER;
        val   VARCHAR2(100);
    BEGIN
        trunc;
        if istruncate = 1 then trunc; end if;
        id := 0;
        FOR i IN 1..cnt LOOP
            id := id + 1;
            val := '-'
                   || id
                   || '-';
            --dbms_output.put_line(id);
            --dbms_output.put_line(val);
            --dbms_output.put_line(cnt);
            INSERT INTO lm_test VALUES (
                id,
                val
            );

            COMMIT;
            dbms_lock.sleep(sleep);
        END LOOP;
    END worker;

    PROCEDURE trunc IS
    BEGIN
        EXECUTE IMMEDIATE 'truncate table lm_test';
    END trunc;

END load;
/