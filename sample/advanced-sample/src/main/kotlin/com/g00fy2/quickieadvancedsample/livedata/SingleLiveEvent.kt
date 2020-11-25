package com.g00fy2.quickieadvancedsample.livedata

import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 *
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue().
 *
 * Note that only one observer is going to be notified of changes.
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {

  private val mPending = AtomicBoolean(false)

  @MainThread
  override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
    // Observe the internal MutableLiveData
    super.observe(owner, { if (mPending.compareAndSet(true, false)) observer.onChanged(it) })
  }

  @MainThread
  override fun setValue(@Nullable t: T?) {
    mPending.set(true)
    super.setValue(t)
  }
}