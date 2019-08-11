package net.corda.training.flows.oracle;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

@InitiatedBy(CreateLoanWithOracleData.class)
public class CreateLoanwithOracleResponder extends FlowLogic<SignedTransaction> {

    private FlowSession cpSession;

    public CreateLoanwithOracleResponder(FlowSession cpSession) {
        this.cpSession = cpSession;
    }

    @Override
    @Suspendable
    public SignedTransaction call() throws FlowException {
        return subFlow(new ReceiveFinalityFlow(cpSession));
    }
}
