//package net.corda.training.flows.service;
//
//import co.paralleluniverse.fibers.Suspendable;
//import com.google.common.collect.ImmutableList;
//import net.corda.core.contracts.Command;
//import net.corda.core.contracts.StateAndRef;
//import net.corda.core.flows.*;
//import net.corda.core.identity.Party;
//import net.corda.core.transactions.SignedTransaction;
//import net.corda.core.transactions.TransactionBuilder;
//import net.corda.training.queryable.contracts.HouseContract;
//import net.corda.training.schedulable.contracts.AccountContract;
//import net.corda.training.schedulable.states.LoanAccount;
//
//public class LoanFlow {
//
//    private LoanFlow(){}
//
//    @InitiatingFlow
//    @StartableByRPC
//    public static class CreateLoanFlow extends FlowLogic<SignedTransaction>{
//
//        private final long loanAmount;
//        private final long accountNumber;
//        private final int installments;
//        private final Party lender;
//
//        public CreateLoanFlow(long loanAmount, long accountNumber, int installments, Party lender) {
//            this.loanAmount = loanAmount;
//            this.accountNumber = accountNumber;
//            this.installments = installments;
//            this.lender = lender;
//        }
//
//        @Override
//        @Suspendable
//        public SignedTransaction call() throws FlowException {
//            Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
//            Party issuer = getOurIdentity();
//
//
//            TransactionBuilder transactionBuilder = new TransactionBuilder(notary)
//                    .addOutputState(new LoanAccount(accountNumber, loanAmount, installments, loanAmount, installments, lender, getOurIdentity()))
//                    .addCommand(new Command<>(new AccountContract.Commands.CreateAccount(), issuer.getOwningKey()));
//
//            transactionBuilder.verify(getServiceHub());
//
//            SignedTransaction selfSignedTx = getServiceHub().signInitialTransaction(transactionBuilder);
//            FlowSession cpSession = initiateFlow(lender);
//
//            return subFlow(new FinalityFlow(selfSignedTx, ImmutableList.of(cpSession)));
//        }
//    }
//
//    @InitiatedBy(CreateLoanFlow.class)
//    public static class CreateLoanResponder extends FlowLogic<SignedTransaction>{
//
//        private FlowSession counterPartySession;
//
//        public CreateLoanResponder(FlowSession counterPartySession) {
//            this.counterPartySession = counterPartySession;
//        }
//
//        @Override
//        @Suspendable
//        public SignedTransaction call() throws FlowException {
//            return subFlow(new ReceiveFinalityFlow(counterPartySession));
//        }
//    }
//
//
//    @InitiatingFlow
//    @StartableByRPC
//    public static class SettleLoanFlow extends FlowLogic<SignedTransaction>{
//
//        private final long accountNumber;
//
//        public SettleLoanFlow(long accountNumber) {
//            this.accountNumber = accountNumber;
//        }
//
//        @Override
//        @Suspendable
//        public SignedTransaction call() throws FlowException {
//            Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
//            Party issuer = getOurIdentity();
//
//            LoanService loanService = getServiceHub().cordaService(LoanService.class);
//            StateAndRef<LoanAccount> inputStateAndRef = loanService.getLoanAccountByLoanId(accountNumber);
//            LoanAccount inputState = inputStateAndRef.getState().getData();
//
//            TransactionBuilder transactionBuilder = new TransactionBuilder(notary)
//                    .addInputState(inputStateAndRef)
//                    .addOutputState(new LoanAccount(accountNumber, inputState.getLoanAmount(), inputState.getTotalInstallments(),
//                            0, 0, inputState.getLender(), getOurIdentity()))
//                    .addCommand(new Command<>(new HouseContract.Commands.IssueRegistration(), issuer.getOwningKey()));
//
//            transactionBuilder.verify(getServiceHub());
//
//            SignedTransaction selfSignedTx = getServiceHub().signInitialTransaction(transactionBuilder);
//            FlowSession cpSession = initiateFlow(inputState.getLender());
//
//            return subFlow(new FinalityFlow(selfSignedTx, ImmutableList.of(cpSession)));
//        }
//    }
//
//    @InitiatedBy(SettleLoanFlow.class)
//    public static class SettleLoanResponder extends FlowLogic<SignedTransaction>{
//
//        private FlowSession counterPartySession;
//
//        public SettleLoanResponder(FlowSession counterPartySession) {
//            this.counterPartySession = counterPartySession;
//        }
//
//        @Override
//        @Suspendable
//        public SignedTransaction call() throws FlowException {
//            return subFlow(new ReceiveFinalityFlow(counterPartySession));
//        }
//    }
//
//}
