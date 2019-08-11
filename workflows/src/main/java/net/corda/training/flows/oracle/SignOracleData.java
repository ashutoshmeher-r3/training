package net.corda.training.flows.oracle;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.crypto.TransactionSignature;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.identity.Party;
import net.corda.core.transactions.FilteredTransaction;

@InitiatingFlow
public class SignOracleData extends FlowLogic<TransactionSignature> {

    private final Party oracle;
    private final FilteredTransaction transaction;

    public SignOracleData(Party oracle, FilteredTransaction transaction) {
        this.oracle = oracle;
        this.transaction = transaction;
    }

    @Override
    @Suspendable
    public TransactionSignature call() throws FlowException {

        FlowSession session = initiateFlow(oracle);
        return session.sendAndReceive(TransactionSignature.class, transaction).unwrap(data -> data);
    }
}
