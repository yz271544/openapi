package akkaApp;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * Created by Administrator on 2016/3/25.
 */
public class HelloWorld extends UntypedActor {

    private ActorRef greeter;

    @Override
    public void preStart() throws Exception {

        greeter = getContext().actorOf(Props.create(Greeter.class),"greeter");
        System.out.println("Greeter Actor Path:" + greeter.path());
        greeter.tell(Greeter.Msg.GREET,getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if(message == Greeter.Msg.DONE){
            greeter.tell(Greeter.Msg.GREET,getSelf());
            getContext().stop(getSelf());
        }else
            unhandled(message);

    }
}
