package com.dansiapa.mvvmkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dansiapa.mvvmkotlin.repo.RetrofitInstance
import com.dansiapa.mvvmkotlin.model.LoginResponseModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class RegisViewModel(): ViewModel() {

    private val loginResponseModel = MutableLiveData<LoginResponseModel>()
    private val errorListener = MutableLiveData<Boolean>()
    private val compositeDisposable = CompositeDisposable()

    fun login(email: String, password: String){
        compositeDisposable.add(
            RetrofitInstance.apiInterface.login(email, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LoginResponseModel>() {
                    override fun onSuccess(t: LoginResponseModel) {
                        if("success" == t.status){
                            loginResponseModel.value = t
                        }else{
                            errorListener.value = true
                        }
                    }

                    override fun onError(e: Throwable) {
                        errorListener.value = true
                    }
                }))
    }

    fun getLoginResponseModel(): MutableLiveData<LoginResponseModel>{
        return loginResponseModel
    }

    fun getErrorListener(): MutableLiveData<Boolean>{
        return errorListener
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}