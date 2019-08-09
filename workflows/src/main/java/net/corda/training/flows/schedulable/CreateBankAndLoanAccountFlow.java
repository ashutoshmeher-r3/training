package net.corda.training.flows.schedulable;

import co.paralleluniverse.fibers.Suspendable;
import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.Command;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.training.queryable.contracts.HouseContract;
import net.corda.training.schedulable.states.BankAccount;
import net.corda.training.schedulable.states.LoanAccount;

import java.time.Instant;
import java.util.HashSet;


public class CreateBankAndLoanAccountFlow {

    private CreateBankAndLoanAccountFlow() {}

    @InitiatingFlow
    @StartableByRPC
    public static class CreateAccountsFlow extends FlowLogic<SignedTransaction> {

        private final long accountBalance;
        private final long loanAmount;
        private final long accountNumber;
        private final int installments;
        private final Party lender;

        public CreateAccountsFlow(long accountNumber,  long accountBalance, long loanAmount, int installments, Party lender) {
            this.accountNumber = accountNumber;
            this.accountBalance = accountBalance;
            this.loanAmount = loanAmount;
            this.installments = installments;
            this.lender = lender;
        }

        @Override
        @Suspendable
        public SignedTransaction call() throws FlowException {
            Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
            Party issuer = getOurIdentity();


            TransactionBuilder transactionBuilder = new TransactionBuilder(notary)
                    .addOutputState(new BankAccount(accountBalance, accountNumber, getOurIdentity(), Instant.now().plusMillis(30000)))
                    .addOutputState(new LoanAccount(accountNumber, loanAmount, installments, loanAmount, installments, lender, getOurIdentity()))
                    .addCommand(new Command<>(new HouseContract.Commands.IssueRegistration(), issuer.getOwningKey()));

            transactionBuilder.verify(getServiceHub());

            SignedTransaction selfSignedTx = getServiceHub().signInitialTransaction(transactionBuilder);
            FlowSession cpSession = initiateFlow(lender);

            return subFlow(new FinalityFlow(selfSignedTx, ImmutableList.of(cpSession)));
        }
    }

    @InitiatedBy(CreateAccountsFlow.class)
    public static class CreateAccountsFlowResponder extends FlowLogic<SignedTransaction>{

        private FlowSession counterPartySession;

        public CreateAccountsFlowResponder(FlowSession counterPartySession) {
            this.counterPartySession = counterPartySession;
        }

        @Override
        @Suspendable
        public SignedTransaction call() throws FlowException {
            return subFlow(new ReceiveFinalityFlow(counterPartySession));
        }
    }
}
