package net.corda.training.flows.queryable;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

// ******************
// * IssueHouseFlowResponder flow *
// ******************
@InitiatedBy(IssueHouseFlow.class)
public class IssueHouseFlowResponder extends FlowLogic<SignedTransaction> {
    private FlowSession counterpartySession;

    public IssueHouseFlowResponder(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // IssueHouseFlowResponder flow logic goes here.

        return subFlow(new ReceiveFinalityFlow(counterpartySession));
    }
}
