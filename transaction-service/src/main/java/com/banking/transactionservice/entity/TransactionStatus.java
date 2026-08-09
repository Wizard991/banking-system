package com.banking.transactionservice.entity;

/*
    Transaction Life Cycle

    PENDING -> PROCESSING -> COMPLETED (clean transaction)
                          -> PENDING VERIFICATION(suspicious detected)
                                -> COMPLETED (verified)
                                -> flagged (SAGA REFUND)
                          -> FAILED
                          -> FLAGGED

 */
public enum TransactionStatus {
    PENDING,
    PROCESSING,
    PENDING_VERIFICATION,
    COMPLETED,
    FAILED,
    FLAGGED
}


