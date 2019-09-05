package com.mustafa.foodApp.util;

import com.mustafa.foodApp.AppExecutors;
import com.mustafa.foodApp.requests.responses.ApiResponse;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

// CacheObject: Type for the Resource data. (database cache)
// RequestObject: Type for the API response. (network request) retrieved from network
public abstract class NetworkBoundResource<CacheObject, RequestObject> {

    private static final String TAG = "NetworkBoundResource";

    private AppExecutors appExecutors;

    // remember we are always looking at the cache getting the data form there even we are fetching the data from the network
    private MediatorLiveData<Resource<CacheObject>> results = new MediatorLiveData<>();

    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        init();
    }

    private void init(){

        // update LiveData for loading status
        results.setValue(Resource.loading(null));

        // observe LiveData source from local db
        final LiveData<CacheObject> dbSource = loadFromDb(); // dbSource has the data from Local database

        results.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(@Nullable CacheObject cacheObject) {

                results.removeSource(dbSource);

                if(shouldFetch(cacheObject)){
                    // get data from the network
                    fetchFromNetwork(dbSource);

                } else{
                    results.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(@Nullable CacheObject cacheObject) {
                            setValue(Resource.success(cacheObject));
                        }
                    });
                }
            }
        });
    }

    /**
     * 1) observe the local database
     * 2) if the <condition/> query the network
     * 3) else stop observing the local db
     * 4) insert new data into Local database
     * 5) begin observing local db again to see the refreshed data from network
     * @param dbSource the data loaded form database
     */
    @SuppressWarnings("unchecked")
    private void fetchFromNetwork(final LiveData<CacheObject> dbSource) {
        //update liveData for loading status
        results.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(CacheObject cacheObject) {
                setValue(Resource.loading(cacheObject));
            }
        });

        final LiveData<ApiResponse<RequestObject>> apiResponse = createCall();// data From network

        results.addSource(apiResponse, new Observer<ApiResponse<RequestObject>>() {
            @Override
            public void onChanged(ApiResponse<RequestObject> requestObjectApiResponse) {
                results.removeSource(dbSource);
                results.removeSource(apiResponse);
                // 3 cases:
                /**
                 * 1) ApiSuccess response
                 * 2) ApiError response
                 * 3) ApiEmpty response
                 */

                if (requestObjectApiResponse instanceof ApiResponse.ApiSuccessResponse){

                    // it is a background worker cuz Room sql operations require so
                    appExecutors.getDiskIO().execute(() -> {
                        //save the response ot the local db on background threat
                        saveCallResult((RequestObject) processResponse((ApiResponse.ApiSuccessResponse) requestObjectApiResponse));

                        // I have used the main threat here cuz I'm using SetValue to immediately set the value
                        appExecutors.getMainThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                results.addSource(loadFromDb(), new Observer<CacheObject>() {
                                    @Override
                                    public void onChanged(CacheObject cacheObject) {
                                        setValue(Resource.success(cacheObject));
                                    }
                                });

                            }
                        });
                    });

                }else if (requestObjectApiResponse instanceof ApiResponse.ApiErrorResponse){
                    appExecutors.getMainThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            results.addSource(loadFromDb(), new Observer<CacheObject>() {
                                @Override
                                public void onChanged(CacheObject cacheObject) {
                                    setValue(Resource.success(cacheObject));

                                }
                            });

                        }
                    });


                } else if (requestObjectApiResponse instanceof ApiResponse.ApiEmptyResponse){
                    results.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(CacheObject cacheObject) {
                            //noinspection ConstantConditions
                            setValue(
                                    Resource.error(
                                            ((ApiResponse.ApiErrorResponse) requestObjectApiResponse).getErrorMessage(),
                                             cacheObject));
                        }
                    });

                }

            }
        });

    }

    @SuppressWarnings("unchecked")
    private CacheObject processResponse (ApiResponse.ApiSuccessResponse response) {
        return (CacheObject)  response.getBody();

    }

    private void setValue(Resource<CacheObject> newValue){
        if(results.getValue() != newValue){
            results.setValue(newValue);
        }
    }

    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestObject item);

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable CacheObject data);

    // Called to get the cached data from the database.
    @NonNull @MainThread
    protected abstract LiveData<CacheObject> loadFromDb();

    // Called to create the API call.
    @NonNull @MainThread
    protected abstract LiveData<ApiResponse<RequestObject>> createCall();

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    public final LiveData<Resource<CacheObject>> getAsLiveData(){
        return results;
    };
}