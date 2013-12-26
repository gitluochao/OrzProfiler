package com.zero.orzprofiler.session;

/**
 * User: luochao
 * Date: 13-12-26
 * Time: 下午1:39
 */
public interface Session {
    /**
     * session　id
     * @return
     */
    String id();

    /**
     * jurge session is inValid
     * @return
     */
    boolean isInvalid();
    /**
     * String type attribute from json
     * @param attribute
     * @return
     */
    String stringAttribute(Attribute attribute);

    /**
     *  long type attribute from json
     * @param attribute
     * @return
     */
    long longAttribute(Attribute attribute);

    /**
     * int type attribute from json
      * @param attribute
     * @return
     */
    int intAttribute(Attribute attribute);

    Type type();


    void addListener(InvalidListener listener);

    void removeListener(InvalidListener listener);

    /**
     * useing the listener pattern
     */
    public interface InvalidListener{
        void onInvalid();
    }


}
