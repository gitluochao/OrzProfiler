namespace java com.zero.profiler.router
exception RouterException{
  1:i16 code,
  2:string reason,
  3:string detail
}
const string LOCAL_HOST = "host";
const string TYPE = "type";
const string RECVWINSIZE = "recvwinsize";
const string TIMEOUT = "TIMEOUT";
const string SUBSCRIBER = "subscriber";

enum ExReason{
    SERVICE_UNAVAILABLE = 100,
    INVALID_USERPWD = 200,
    NOTFOUNT_BROKER = 201,
    UNAUTHORIED_CATEGORY = 202
}

service RouterService{
    string getBroker(1:string user,2:string pwd,3:string topic,4:string apply,5:map<string,string> prop) throws (1:RouterException f) ;
}