package com.general.rxbus;

import android.support.annotation.NonNull;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.processors.FlowableProcessor;

/**
 * Author: zml
 * Date  : 2019/1/3 - 20:11
 **/
public class RxBusSubscription<T> {
    private FlowableProcessor<T> processor;
    private List<Subscription> subscriptionList = new ArrayList<>();

    public RxBusSubscription(@NonNull FlowableProcessor<T> processor){
        this.processor = processor;
    }

    @NonNull
    public FlowableProcessor<T> getProcessor(){
        return this.processor;
    }

    public Flowable<T> subscribeOn(Scheduler scheduler){
        return getProcessor().subscribeOn(scheduler);
    }

    public Flowable<T> observeOn(Scheduler scheduler){
        return getProcessor().observeOn(scheduler);
    }

    public void cancel(){
        Iterator<Subscription> iterator = subscriptionList.iterator();
        while (iterator.hasNext()){
            Subscription subscription = iterator.next();
            if (null!=subscription){
                subscription.cancel();
                iterator.remove();
            }
        }
    }


    public abstract class RestrictedSubscriber<X> implements Subscriber<X>{
        private final String TAG = RestrictedSubscriber.class.getSimpleName();
        private int onSubscribeRequest;
        private int onNextRequest;
        private Subscription subscription;

        public RestrictedSubscriber(){
            this(1,1);
        }
        public RestrictedSubscriber(int request){
            this(request,request);
        }
        public RestrictedSubscriber(int onSubscribeRequest,int onNextRequest){
            this.onSubscribeRequest = onSubscribeRequest;
            this.onNextRequest = onNextRequest;
        }

        @Override
        public final void onSubscribe(Subscription sub){
            subscription = sub;
            subscriptionList.add(subscription);
            if (onSubscribeRequest>0){
                subscription.request(onSubscribeRequest);
            }
            try {
                onSubscribeCompat(sub);
            }catch (Throwable throwable){
                Log.i(TAG,throwable.getLocalizedMessage());
            }
        }

        @Override
        public final void onNext(X t){
            if (onNextRequest>0){
                subscription.request(onNextRequest);
            }
            try {
                onNextCompat(t);
            }catch (Throwable throwable){
                Log.e(TAG,throwable.getMessage());
            }
        }

        @Override
        public final void onComplete(){
            try {
                onCompleteCompat();
            }catch (Throwable throwable){
                Log.e(TAG,throwable.getMessage());
            }
        }

        @Override
        public final void onError(Throwable t){
            try {
                onErrorCompat(t);
            }catch (Throwable throwable){
                Log.e(TAG,throwable.getMessage());
            }
        }

        public void onSubscribeCompat(Subscription subscription){
            //ignore
        }

        public void onErrorCompat(Throwable throwable){
            //ignore
        }

        public void onCompleteCompat(){
            //ignore
        }

        public abstract void onNextCompat(X t);

    }

}
