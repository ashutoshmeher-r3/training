package net.corda.training.schedulable.states;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.training.schedulable.contracts.AccountContract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@BelongsToContract(AccountContract.class)
public class LoanAccount implements ContractState {

    private final long accountNumber;
    private final long loanAmount;
    private final int totalInstallments;
    private final long remainingLoanAmount;
    private final int remainingInstallments;

    private final Party lender;
    private final Party borrower;

    public LoanAccount(long accountNumber, long loanAmount, int totalInstallments, long remainingLoanAmount, int remainingInstallments,
                       Party lender, Party borrower) {
        this.accountNumber = accountNumber;
        this.loanAmount = loanAmount;
        this.totalInstallments = totalInstallments;
        this.remainingLoanAmount = remainingLoanAmount;
        this.remainingInstallments = remainingInstallments;
        this.lender = lender;
        this.borrower = borrower;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(lender, borrower);
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public long getLoanAmount() {
        return loanAmount;
    }

    public int getTotalInstallments() {
        return totalInstallments;
    }

    public long getRemainingLoanAmount() {
        return remainingLoanAmount;
    }

    public int getRemainingInstallments() {
        return remainingInstallments;
    }

    public Party getLender() {
        return lender;
    }

    public Party getBorrower() {
        return borrower;
    }
}
