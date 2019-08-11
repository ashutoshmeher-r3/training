package net.corda.training.flows.oracle;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.identity.Party;

@InitiatingFlow
public class QueryRandomNumber extends FlowLogic<Long> {

    private final Party oracle;

    public QueryRandomNumber(Party oracle) {
        this.oracle = oracle;
    }

    @Override
    @Suspendable
    public Long call() throws FlowException {
        return initiateFlow(oracle).receive(Long.class).unwrap(it -> it);
    }
}
