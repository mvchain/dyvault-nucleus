package com.mvc.dyvault.console.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author qyc
 */
@Getter
@Setter
public class TransactionResponse<T> {

    private String transactionHash;
    private T event;

    TransactionResponse() {
    }

    public TransactionResponse(String transactionHash) {
        this(transactionHash, null);
    }

    public TransactionResponse(String transactionHash, T event) {
        this.transactionHash = transactionHash;
        this.event = event;
    }
}
