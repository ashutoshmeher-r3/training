package net.corda.training.schedulable.contracts;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

public class AccountContract implements Contract {

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {

    }

    public interface Commands extends CommandData {
        class PayLoanInstallment implements Commands {}
    }
}
