package akkaApp.lifecycle;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by Administrator on 2016/3/25.
 */
public class MyWorker extends UntypedActor{

    private final LoggingAdapter log = Logging.getLogger(getContext().system(),this);
    public static enum Msg{
        WORKING,DONE,CLOSE;
    }


    @Override
    public void preStart() throws Exception {
        System.out.println("MyWorker is starting");
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("MyWorker is stopping");
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if(message == Msg.WORKING){
            System.out.println("I am working");
        }

        if(message == Msg.DONE){
            System.out.println("Stop working");
        }

        if(message == Msg.CLOSE){
            System.out.println("I will shutdown");
            getSender().tell(Msg.CLOSE,getSelf());
            getContext().stop(getSelf());
        }else
            unhandled(message);

    }
}
