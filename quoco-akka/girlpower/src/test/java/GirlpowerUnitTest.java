import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import org.junit.Test;
import service.actor.Init;
import service.actor.Quoter;
import service.girlpower.GPQService;
import service.core.ClientInfo;
import service.girlpower.GPQService;
import service.messages.QuotationRequests;
import service.messages.QuotationResponse;


public class GirlpowerUnitTest {

    //test quoter
    @Test
    public void testQuoter(){
        try{
            //create a system inside the Test
            ActorSystem system = ActorSystem.create("girlpower");
            ActorRef quoterRef = system.actorOf(Props.create(Quoter.class), "test");
            TestKit probe = new TestKit(system);
            quoterRef.tell(new Init(new GPQService()), null);
            quoterRef.tell(new QuotationRequests(1, new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1")), probe.getRef());
            probe.awaitCond(probe::msgAvailable);
            probe.expectMsgClass(QuotationResponse.class);
        } catch(Exception e) {
            System.out.println("\n\n\n==========\ntestQuoter TROUBLE:\n==========\n" + e + "\n\n\n");
        }
    }

}
