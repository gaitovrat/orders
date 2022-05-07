CREATE OR REPLACE PROCEDURE PBuyCloth(p_cloth_id IN INTEGER, p_count IN INTEGER, p_user_id IN INTEGER)
AS
v_order "ORDER"%rowtype;
v_order_id "ORDER".ID%type;
BEGIN
    SELECT * INTO v_order FROM "ORDER" WHERE USER_ID = p_user_id AND ORDER_STATUS_ID = 1 AND ROWNUM < 2;
    INSERT INTO MANY_ORDER_TO_MANY_CLOTH(order_id, cloth_id, count) VALUES (v_order.ID, p_cloth_id, p_count);
    COMMIT;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        INSERT INTO "ORDER"(user_id, worker_id, order_status_id) VALUES (p_user_id, 1, 1);
        SELECT MAX(ID) INTO v_order_id FROM "ORDER";
        INSERT INTO MANY_ORDER_TO_MANY_CLOTH(order_id, cloth_id, count) VALUES (v_order_id, p_cloth_id, p_count);
        COMMIT;
    WHEN OTHERS THEN
        ROLLBACK;
END;

CREATE OR REPLACE PROCEDURE PSendOrder(p_order_id IN INTEGER, p_worker_id IN INTEGER)
AS
BEGIN
    UPDATE "ORDER" SET ORDER_STATUS_ID = 4, WORKER_ID = 1 WHERE ID = p_order_id;
    UPDATE "USER" SET STATUS_ID = 2 WHERE ID = p_worker_id;
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
END;

CREATE OR REPLACE PROCEDURE PApproveOrder(p_order_id IN INTEGER, p_worker_id IN INTEGER)
AS
BEGIN
    UPDATE "USER" SET STATUS_ID = 3 WHERE ID = p_worker_id;
    UPDATE "ORDER" SET ORDER_STATUS_ID = 2, WORKER_ID = p_worker_id WHERE ID = p_order_id;
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
END;
