package net.corda.training.flows.schedulable;

import co.paralleluniverse.fibers.Suspendable;
import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.training.schedulable.contracts.AccountContract;
import net.corda.training.schedulable.states.BankAccount;
import net.corda.training.schedulable.states.LoanAccount;

import java.time.Instant;
import java.util.List;

@SchedulableFlow
@InitiatingFlow
@StartableByRPC
public class ScheduledPaymentFlow extends FlowLogic<SignedTransaction> {

    private final long accountNumber;

    public ScheduledPaymentFlow(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    @Suspendable
    public SignedTransaction call() throws FlowException {

        List<StateAndRef<BankAccount>> bankAccountStateAndRefs = getServiceHub().getVaultService().queryBy(BankAccount.class).getStates();

        StateAndRef<BankAccount> inputStateAndRefs0 = bankAccountStateAndRefs.stream().filter(bankAccountStateAndRef -> {
            BankAccount bankAccount = bankAccountStateAndRef.getState().getData();
            return bankAccount.getAccountNumber() == this.accountNumber;
        }).findAny().orElseThrow(() -> new IllegalArgumentException("Bank Account Not Found"));

        List<StateAndRef<LoanAccount>> loanAccountStateAndRefs = getServiceHub().getVaultService().queryBy(LoanAccount.class).getStates();

        StateAndRef<LoanAccount> inputStateAndRefs1 = loanAccountStateAndRefs.stream().filter(loanAccountStateAndRef -> {
            LoanAccount loanAccount = loanAccountStateAndRef.getState().getData();
            return loanAccount.getAccountNumber() == this.accountNumber;
        }).findAny().orElseThrow(() -> new IllegalArgumentException("Loan Account Not Found"));

        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

        BankAccount input0 = inputStateAndRefs0.getState().getData();
        LoanAccount input1 = inputStateAndRefs1.getState().getData();

        long installmentAmount = input1.getLoanAmount() / input1.getTotalInstallments();
        LoanAccount outPut1 = new LoanAccount(input1.getAccountNumber(), input1.getLoanAmount(), input1.getTotalInstallments(),
                input1.getRemainingLoanAmount() - installmentAmount, input1.getRemainingInstallments() - 1,
                input1.getLender(), input1.getBorrower());
        BankAccount outPut0 = new BankAccount(input0.getAccountBalance() - installmentAmount, input0.getAccountNumber(),
                input0.getOwner(), outPut1.getRemainingLoanAmount()==0?null:Instant.now().plusMillis(30000));

        TransactionBuilder transactionBuilder = new TransactionBuilder(notary)
                .addCommand(new Command<>(new AccountContract.Commands.PayLoanInstallment(), getOurIdentity().getOwningKey()))
                .addInputState(inputStateAndRefs0)
                .addInputState(inputStateAndRefs1)
                .addOutputState(outPut0)
                .addOutputState(outPut1);

        transactionBuilder.verify(getServiceHub());

        SignedTransaction selfSignedTx = getServiceHub().signInitialTransaction(transactionBuilder);

        FlowSession cpSession = initiateFlow(input1.getLender());

        return subFlow(new FinalityFlow(selfSignedTx, ImmutableList.of(cpSession)));
    }
}
