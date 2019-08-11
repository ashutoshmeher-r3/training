package net.corda.training.flows.oracle;

import co.paralleluniverse.fibers.Suspendable;
import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.crypto.TransactionSignature;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.FilteredTransaction;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.training.schedulable.contracts.AccountContract;
import net.corda.training.schedulable.states.LoanAccount;

@InitiatingFlow
@StartableByRPC
public class CreateLoanWithOracleData extends FlowLogic<SignedTransaction>{

    private final long loanAmount;
    private final int installments;
    private final Party lender;
    private final Party oracle;

    public CreateLoanWithOracleData(long loanAmount, int installments, Party lender, Party oracle) {
        this.loanAmount = loanAmount;
        this.installments = installments;
        this.lender = lender;
        this.oracle = oracle;
    }

    @Override
    @Suspendable
    public SignedTransaction call() throws FlowException {
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
        Party issuer = getOurIdentity();

        Long accountNumber = subFlow(new QueryRandomNumber(oracle));

        CommandData cmdData = new AccountContract.Commands.CreateLoanWithOracleData(accountNumber);
        TransactionBuilder transactionBuilder = new TransactionBuilder(notary)
                .addOutputState(new LoanAccount(accountNumber, loanAmount, installments, loanAmount, installments, lender, getOurIdentity()))
                .addCommand(new Command<>(cmdData, ImmutableList.of(issuer.getOwningKey(), oracle.getOwningKey())));

        transactionBuilder.verify(getServiceHub());

        SignedTransaction selfSignedTx = getServiceHub().signInitialTransaction(transactionBuilder);


        FilteredTransaction filteredTransaction = selfSignedTx.buildFilteredTransaction(it -> it instanceof Command &&
                (((Command) it).getSigners().contains(oracle.getOwningKey()) && ((Command) it).getValue() instanceof CreateLoanWithOracleData));


        TransactionSignature oracleSignature = subFlow(new SignOracleData(oracle, filteredTransaction));
        SignedTransaction signedTransaction = selfSignedTx.withAdditionalSignature(oracleSignature);

        FlowSession cpSession = initiateFlow(lender);
        return subFlow(new FinalityFlow(signedTransaction, ImmutableList.of(cpSession)));
    }
}
