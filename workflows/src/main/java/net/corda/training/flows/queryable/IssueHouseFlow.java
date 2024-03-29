package net.corda.training.flows.queryable;

import co.paralleluniverse.fibers.Suspendable;
import com.google.common.collect.ImmutableList;
import net.corda.training.queryable.contracts.HouseContract;
import net.corda.training.queryable.states.HouseState;
import net.corda.core.contracts.Command;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;

// ******************
// * IssueHouseFlow flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class IssueHouseFlow extends FlowLogic<SignedTransaction> {

    private final HouseData houseData;
    private final Party owner;

    public IssueHouseFlow(HouseData houseData, Party owner) {
        this.houseData = houseData;
        this.owner = owner;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {

        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
        Party issuer = getOurIdentity();


        TransactionBuilder transactionBuilder = new TransactionBuilder(notary)
                .addOutputState(new HouseState(houseData.getAddress(), houseData.getTotalAreaInSqft(),
                        houseData.getCarpetAreaInSqft(), houseData.getFloor(), owner, issuer))
                .addCommand(new Command<>(new HouseContract.Commands.IssueRegistration(), issuer.getOwningKey()));

        transactionBuilder.verify(getServiceHub());

        SignedTransaction selfSignedTx = getServiceHub().signInitialTransaction(transactionBuilder);

        FlowSession flowSession = initiateFlow(owner);

        return subFlow(new FinalityFlow(selfSignedTx, ImmutableList.of(flowSession)));
    }
}
