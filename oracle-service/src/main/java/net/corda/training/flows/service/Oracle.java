package net.corda.training.flows.service;

import net.corda.core.crypto.TransactionSignature;
import net.corda.core.node.AppServiceHub;
import net.corda.core.node.services.CordaService;
import net.corda.core.serialization.SingletonSerializeAsToken;
import net.corda.core.transactions.FilteredTransaction;
import net.corda.core.transactions.FilteredTransactionVerificationException;
import org.apache.commons.lang.math.NumberUtils;

import java.util.Random;

@CordaService
public class Oracle extends SingletonSerializeAsToken {

    private AppServiceHub serviceHub;

    public Oracle(AppServiceHub serviceHub) {
        this.serviceHub = serviceHub;
    }

    public Long generateRandomNumber(){
        Random random = new Random();
        return random.nextLong();
    }

    public TransactionSignature sign(FilteredTransaction transaction) throws FilteredTransactionVerificationException {

        transaction.verify();

        boolean isValid = true; // Additional verification logic.

        if(isValid){
            return serviceHub.createSignature(transaction, serviceHub.getMyInfo().getLegalIdentities().get(0).getOwningKey());
        }else{
            throw new IllegalArgumentException();
        }
    }
}
