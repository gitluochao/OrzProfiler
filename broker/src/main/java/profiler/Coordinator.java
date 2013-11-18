package profiler;

/**
 * User: luochao
 * Date: 13-10-25
 * Time: 涓嫔崃4:01
 */
public interface Coordinator {
    /**
     * @param name
     * @return a new {@link Track} with name
     */
    Track track(String name);
    /**
     * 淇濆瓨褰揿墠姣忎釜绾跨▼镄勬秷璐圭姸镐?
     */
    public  interface  Track{
        void destory();
        boolean move();
    }
}
