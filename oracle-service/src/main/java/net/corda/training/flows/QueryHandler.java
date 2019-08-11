package net.corda.training.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatedBy;
import net.corda.training.flows.oracle.QueryRandomNumber;
import net.corda.training.flows.service.Oracle;

@InitiatedBy(QueryRandomNumber.class)
public class QueryHandler extends FlowLogic<Void> {

    private FlowSession requestSession;

    public QueryHandler(FlowSession requestSession) {
        this.requestSession = requestSession;
    }

    @Override
    @Suspendable
    public Void call() throws FlowException {

        Oracle oracle = getServiceHub().cordaService(Oracle.class);
        requestSession.send(oracle.generateRandomNumber());
        return null;
    }
}
