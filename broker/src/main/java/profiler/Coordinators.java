package profiler;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: luochao
 * Date: 13-10-25
 * Time: 涓嫔崃4:02
 */
public class Coordinators {
    public Coordinators() {
    }
    private static Coordinator coordinator(final  int valume){
        return  new InnerCoordinator(valume);
    }
    public final static class  InnerCoordinator implements  Coordinator{
        private  final  int valume;
        private final AtomicInteger arriveds = new AtomicInteger();
        private final AtomicInteger tracks = new AtomicInteger();
        //绛夋墍链夌殑绾跨▼閮?鍒拌揪浜嗗紑濮嬩笅涓€涓秷璐规爣璁版槸鍚﹁兘澶熼吨缃?
        private final AtomicBoolean flag = new AtomicBoolean();

        public InnerCoordinator(final int valume) {
            this.valume = valume;
        }

        public Track track(String name) {
            return new InnerTrack(name);
        }
        boolean flag(){
            return flag.get();
        }
        void arrived(Track track){
            arriveds.incrementAndGet();
            startNextRoundWhileAllTrackCompeled();
        }

        void reverseFlag(){
            flag.set(!flag());
        }
        void  startNextRoundWhileAllTrackCompeled(){
            int arrivedCount = arriveds.get();
            if(arrivedCount < tracks.get())
                return;
            if(!arriveds.compareAndSet(arrivedCount,0))
                return;
            reverseFlag();
        }
        public final class InnerTrack implements Track{
            private final  String name ;
            private int steps;
            private AtomicBoolean destory = new AtomicBoolean();
            private boolean flag;
            private State state;
            public InnerTrack(String name) {
                this.name = name;
            }
            void init(){
                steps = 0;
                flag = flag();
                state = State.RUNING;
            }
            public void destory() {
                if(destory.get()) return;
                destory.set(false);

            }

            public boolean move() {
                steps ++;
                return true;
            }
            public boolean pause(){
                arrived(this);
                state = State.WAITING;
                startNextRoundWhileAllTrackCompeled();
                return  false;
            }
            boolean isNotArrived(){
                return  steps < valume;
            }
            boolean canResum(){
                return flag != flag();
            }
            boolean resume(){
                init();
                return  false;
            }
            boolean await(){
                return  false;
            }

        }
         enum State{
            RUNING{
                @Override
                boolean move(InnerTrack track) {
                    return track.isNotArrived()?track.move():track.pause();
                }
            },
            WAITING{
                @Override
                boolean move(InnerTrack track) {
                    return track.canResum()?track.resume():track.await();
                }
            };
            abstract boolean move(InnerTrack track);
        }

    }
}
