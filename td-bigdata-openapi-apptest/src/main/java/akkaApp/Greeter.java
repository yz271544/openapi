package akkaApp;

import akka.actor.UntypedActor;

/**
 * Created by Administrator on 2016/3/25.
 */
public class Greeter extends UntypedActor {

    public static enum Msg{
        GREET,DONE;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message == Msg.GREET){
            System.out.println("Hello World!");
            getSender().tell(Msg.DONE,getSelf());
        }else{
            unhandled(message);
        }

    }
}
