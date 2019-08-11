//package net.corda.training.flows.service;
//
//import net.corda.core.contracts.StateAndRef;
//import net.corda.core.node.AppServiceHub;
//import net.corda.core.node.services.CordaService;
//import net.corda.core.serialization.SingletonSerializeAsToken;
//import net.corda.training.schedulable.states.LoanAccount;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//@CordaService
//public class LoanService extends SingletonSerializeAsToken {
//
//    private Log log = LogFactory.getLog(LoanService.class);
//
//    private AppServiceHub serviceHub;
//
//    public LoanService(AppServiceHub serviceHub) {
//        this.serviceHub = serviceHub;
//        trackLoanSettlement();
//    }
//
//    public AppServiceHub getServiceHub() {
//        return serviceHub;
//    }
//
//    public StateAndRef<LoanAccount> getLoanAccountByLoanId(long loanId){
//        return getServiceHub().getVaultService().queryBy(LoanAccount.class).getStates().stream().filter(loanAccountStateAndRef -> {
//            LoanAccount loanAccount = loanAccountStateAndRef.getState().getData();
//            return loanAccount.getAccountNumber() == loanId;
//        }).findAny().orElseThrow(() -> new IllegalArgumentException("Loan Account Not Found"));
//    }
//
//    private void trackLoanSettlement(){
//        getServiceHub().getVaultService().trackBy(LoanAccount.class).getUpdates().subscribe( loanAccountUpdate -> {
//            loanAccountUpdate.getProduced().forEach(loanAccountStateAndRef -> {
//                if(loanAccountStateAndRef.getState().getData().getRemainingInstallments() == 0)
//                    log.info("********* Handle Loan Settlement Trigger Here ************");
//            });
//        });
//    }
//}
