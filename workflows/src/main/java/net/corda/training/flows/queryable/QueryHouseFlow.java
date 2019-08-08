package net.corda.training.flows.queryable;

import net.corda.training.queryable.schema.PersistentHouse;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.identity.CordaX500Name;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@InitiatingFlow
@StartableByRPC
public class QueryHouseFlow extends FlowLogic<List<PersistentHouse>> {

    private int option = 1;

    public QueryHouseFlow(int option) {
        this.option = option;
    }

    @Override
    public List<PersistentHouse> call() throws FlowException {
        if(option == 1) {
            return getServiceHub().withEntityManager((EntityManager entityManager) -> {
                CriteriaQuery<PersistentHouse> query = entityManager.getCriteriaBuilder().createQuery(PersistentHouse.class);
                Root<PersistentHouse> type = query.from(PersistentHouse.class);
                query.select(type);
                return entityManager.createQuery(query).getResultList();
            });
        }else{
            String nativeQuery = "SELECT * FROM HOUSE_DETAIL";
            try {
                List<PersistentHouse> resultList  = new ArrayList<>();
                ResultSet rs = getServiceHub().jdbcSession().prepareStatement(nativeQuery).executeQuery();
                while (rs.next()){
                    PersistentHouse house = new PersistentHouse(rs.getString(3), rs.getInt(4),
                            rs.getInt(5), rs.getInt(7),
                            getServiceHub().getNetworkMapCache().getPeerByLegalName(CordaX500Name.parse(rs.getString(6))));
                    resultList.add(house);
                }

                return resultList;
            }catch (SQLException se){
                throw new FlowException(se.getCause());
            }
        }
    }

}
