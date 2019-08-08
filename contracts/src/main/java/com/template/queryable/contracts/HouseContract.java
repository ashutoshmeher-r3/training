package com.template.queryable.contracts;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

// ************
// * Contract *
// ************
public class HouseContract implements Contract {
    // This is used to identify our contracts when building a transaction.
    public static final String ID = "com.template.queryable.contracts.HouseContract";

    // A transaction is valid if the verify() function of the contracts of all the transaction's input and output states
    // does not throw an exception.
    @Override
    public void verify(LedgerTransaction tx) {}

    // Used to indicate the transaction's intent.
    public interface Commands extends CommandData {
        class IssueRegistration implements Commands {}
    }
}