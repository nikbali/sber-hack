CREATE OR REPLACE PACKAGE load AS
    PROCEDURE worker (
        sleep   NUMBER,
        cnt     IN OUT NUMBER,
        istruncate NUMBER--1=truncate
    );

    PROCEDURE trunc;

END load;
/