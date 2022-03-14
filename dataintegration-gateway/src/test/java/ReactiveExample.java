import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.util.function.Consumer;

/**
 * @author gavin
 * @since 2020/9/24 10:27 上午
 */
public class ReactiveExample {

    public static void main(String[] args) throws InterruptedException {
        final Mono<String> a = Mono.just("a");

        a.doFinally(new Consumer<SignalType>() {
            @Override
            public void accept(SignalType signalType) {
                System.out.println("doFinally: " + signalType);
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                System.out.println("doOnError");
            }
        }).doOnSuccess(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println("doOnSuccess");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println("接收到：" + a);
            }
        });

        a.then(Mono.fromRunnable(new Runnable() {
            @Override
            public void run() {
                System.out.println("then");
            }
        })).subscribe();


        Thread.sleep(3_000);

    }
}
