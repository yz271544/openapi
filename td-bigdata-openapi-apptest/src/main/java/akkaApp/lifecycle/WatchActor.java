package akkaApp.lifecycle;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by Administrator on 2016/3/25.
 */
public class WatchActor extends UntypedActor{

    private final LoggingAdapter log = Logging.getLogger(getContext().system(),this);

    public WatchActor(ActorRef ref){
        getContext().watch(ref);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof Terminated){
            System.out.println(String.format("%s has terminated,shutting down system",
                    ((Terminated) message).getActor().path()));
            getContext().system().shutdown();
        }else
            unhandled(message);

    }
}
