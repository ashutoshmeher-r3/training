package net.corda.training.schedulable.contracts;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;
import net.corda.training.schedulable.states.LoanAccount;
import org.jetbrains.annotations.NotNull;

public class AccountContract implements Contract {

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        if(tx.getCommand(0).getValue() instanceof Commands.CreateLoanWithOracleData){
            Commands.CreateLoanWithOracleData command = (Commands.CreateLoanWithOracleData) tx.getCommand(0).getValue();
            if(command.getAccountNumber() != ((LoanAccount)tx.getOutput(0)).getAccountNumber()){
                throw new IllegalArgumentException("Corrupt Oracle Data");
            }
        }
    }

    public interface Commands extends CommandData {
        class PayLoanInstallment implements Commands {}
        class CreateAccount implements Commands {}
        class CreateLoan implements Commands {}
        class SettleLoan implements Commands {}

        class CreateLoanWithOracleData implements Commands {
            private Long accountNumber;

            public CreateLoanWithOracleData(Long accountNumber) {
                this.accountNumber = accountNumber;
            }

            public Long getAccountNumber() {
                return accountNumber;
            }
        }
    }
}
