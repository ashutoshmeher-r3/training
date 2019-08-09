package net.corda.training.flows.schedulable;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

@InitiatedBy(ScheduledPaymentFlow.class)
public class PaymentResponseFlow extends FlowLogic<SignedTransaction>{

    private final FlowSession cpflowSession;

    public PaymentResponseFlow(FlowSession cpflowSession) {
        this.cpflowSession = cpflowSession;
    }

    @Override
    @Suspendable
    public SignedTransaction call() throws FlowException {
        return subFlow(new ReceiveFinalityFlow(cpflowSession));
    }
}
