package vn.co.honda.hondacrm.btu.Utils;

import android.util.Log;

public class LogUtils {

    private static boolean DEBUG = true;

    private static final String TRACE_METHOD = "trace";
    private static final String START_END_LOG_METHOD = "startEndMethodLog";

    private static final int CLASS_NAME_INDEX = 0;
    
    private static final int METHOD_NAME_INDEX = 1;

    public static void setDebugMode(boolean debugMode) {
        DEBUG = debugMode;
    }

    public static void i(String content) {
        if (DEBUG) {
            String[] msg = trace();
            if (msg != null) {
                i(msg[CLASS_NAME_INDEX], msg[METHOD_NAME_INDEX] + content);
            }
        }
    }

    private static void i(String tag, String content) {
        if (DEBUG) {
            Log.i(tag, content);
        }
    }

    public static void e(String content) {
        if (DEBUG) {
            String[] msg = trace();
            if (msg != null) {
                e(msg[CLASS_NAME_INDEX], msg[METHOD_NAME_INDEX] + content);
            }
        }
    }

    private static void e(String tag, String content) {
        if (DEBUG) {
            Log.e(tag, content);
        }
    }

    public static void d(String content) {
        if (DEBUG) {
            String[] msg = trace();
            if (msg != null) {
                d(msg[CLASS_NAME_INDEX], "[ " + msg[METHOD_NAME_INDEX] + " ] " + content);
            }
        }
    }

    public static void w(String content) {
        if (DEBUG) {
            String[] msg = trace();
            if (msg != null) {
                w(msg[CLASS_NAME_INDEX], "[ " + msg[METHOD_NAME_INDEX] + " ] " + content);
            }
        }
    }

    private static void d(String tag, String content) {
        if (DEBUG) {
            Log.d(tag, content);
        }
    }

    private static void w(String tag, String content) {
        if (DEBUG) {
            Log.w(tag, content);
        }
    }

//    public static void d(String tag, String method, String content) {
//        if (DEBUG) {
//            Log.d(tag, "[ " + method + " ] "+ content);
//        }
//    }

    public static void startEndMethodLog(boolean isStart) {
        if (DEBUG) {
            if (isStart) {
                d("=============START=============");
            } else {
                d("=============END===============");
            }
        }
    }

    private static String[] trace() {
        int index = 0;
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements == null) {
            return  null;
        }
        for (int i = 0; i < stackTraceElements.length ; i++)
        {
            StackTraceElement ste = stackTraceElements[i];
            if (ste.getClassName().equals(LogUtils.class.getName()))
            {
                if (ste.getMethodName().contains(TRACE_METHOD)) {
                    index = i + 2; // index for startEndMethodLog method
                    if (index < stackTraceElements.length && stackTraceElements[index].getMethodName().contains(START_END_LOG_METHOD)) {
                        break;
                    } else {
                        index = i + 1; // index for d method
                        break;
                    }
                }
            }
        }

        index++; // index for method call d or startEndMethodLog method

        if (stackTraceElements.length >= index) {
            final StackTraceElement s = stackTraceElements[index];
            if (s != null) {
                return new String[]{
                        stackTraceElements[index].getFileName(), stackTraceElements[index].getMethodName() + "[" + stackTraceElements[index].getLineNumber() + "] "
                };
            }
        }
        return null;
    }
}