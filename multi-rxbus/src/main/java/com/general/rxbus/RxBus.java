package com.general.rxbus;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * Author: zml
 * Date  : 2019/1/3 - 20:10
 **/
public final class RxBus {
    private static final String TAG = RxBus.class.getSimpleName();
    private static boolean debug = false;

    private ConcurrentHashMap<Object,List<RxBusSubscription>> flowableProcessorMapper = new ConcurrentHashMap<>();

    private RxBus(){

    }

    public static void setDebug(boolean debug){
        RxBus.debug = debug;
    }
    public static synchronized RxBus get(){
        return Holder.instance;
    }

    public <T> RxBusSubscription<T> Register(@NonNull Class<T> clazz){
        return register(clazz.getName(),clazz);
    }

    private  <T> RxBusSubscription<T> register(@NonNull Object object,@NonNull Class<T> clazz){
        List<RxBusSubscription> rxBusSubscriptions = flowableProcessorMapper.get(object);
        if (null == rxBusSubscriptions){
            rxBusSubscriptions = new ArrayList<>();
            flowableProcessorMapper.put(object,rxBusSubscriptions);
        }
        FlowableProcessor<T> processor = PublishProcessor.<T>create().toSerialized();
        Flowable empty = Flowable.empty();
        processor.onErrorResumeNext(empty);
        processor.onExceptionResumeNext(empty);

        RxBusSubscription<T> rxBusSubscription = new RxBusSubscription<>(processor);
        rxBusSubscriptions.add(rxBusSubscription);
        if (debug){
            Log.d(TAG,"[register]flowableProcessorMapper: " + flowableProcessorMapper.size() + ":" + object);
        }
        return rxBusSubscription;
    }

    public <T> void unRegister(@NonNull Class clazz,@NonNull RxBusSubscription<T> rxBusSubscription){
        unRegister(clazz.getName(),rxBusSubscription);
    }

    private <T> void unRegister(@NonNull Object tag, @NonNull RxBusSubscription<T> rxBusSubscription) {
        List<RxBusSubscription> rxBusSubscriptionList = flowableProcessorMapper.get(tag);
        if (null != rxBusSubscriptionList) {
            rxBusSubscriptionList.remove(rxBusSubscription);
            rxBusSubscription.cancel();
            if (rxBusSubscriptionList.size() > 0) {
                flowableProcessorMapper.remove(tag);
            }
        }
        if (debug) {
            Log.d(TAG, "[unregister]flowableProcessorMapper: " + flowableProcessorMapper.size() + ":" + tag);
        }
    }

    public void post(@NonNull Object content){
        post(content.getClass().getName(),content);
    }

    private void post(@NonNull Object tag,@NonNull Object content){
        List<RxBusSubscription> subjectList = flowableProcessorMapper.get(tag);
        if (subjectList != null && subjectList.size() > 0){
            for (RxBusSubscription rxBusSubscription:subjectList){
                rxBusSubscription.getProcessor().onNext(content);
            }
        }
        if (debug){
            Log.d(TAG,"[send]flowableProcessorMapper: " + flowableProcessorMapper.size() + ":" + tag);
        }
    }

    public void clear(){
        flowableProcessorMapper.clear();
    }


    private static class Holder{
        private static RxBus instance = new RxBus();
    }
}
