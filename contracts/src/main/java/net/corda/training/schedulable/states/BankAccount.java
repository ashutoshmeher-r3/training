package net.corda.training.schedulable.states;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.*;
import net.corda.core.flows.FlowLogicRef;
import net.corda.core.flows.FlowLogicRefFactory;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.training.schedulable.contracts.AccountContract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;

@BelongsToContract(AccountContract.class)
public class BankAccount implements SchedulableState {

    private final long accountBalance;
    private final long accountNumber;
    private final Party owner;

    private final Instant scheduledTime;

    public BankAccount(long accountBalance, long accountNumber, Party owner, Instant scheduledTime) {
        this.accountBalance = accountBalance;
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.scheduledTime = scheduledTime;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(owner);
    }

    @Nullable
    @Override
    public ScheduledActivity nextScheduledActivity(@NotNull StateRef thisStateRef, @NotNull FlowLogicRefFactory flowLogicRefFactory) {
        FlowLogicRef flowLogicRef = flowLogicRefFactory.create("net.corda.training.flows.schedulable.ScheduledPaymentFlow", accountNumber);
        return new ScheduledActivity(flowLogicRef, scheduledTime);
    }

    public Instant getScheduledTime() {
        return scheduledTime;
    }

    public long getAccountBalance() {
        return accountBalance;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public Party getOwner() {
        return owner;
    }
}