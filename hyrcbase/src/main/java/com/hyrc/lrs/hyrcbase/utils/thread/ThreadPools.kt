package com.hyrc.lrs.hyrcbase.utils.thread

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.hyrc.lrs.hyrcbase.base.BaseApplication.Companion.context
import java.lang.ref.WeakReference
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * @description 作用:
 * @date: 2020/12/11
 * @author:
 */
class ThreadPools private constructor(context: Context?) {
    private fun initThreadPool() {
        val threadPoolExecutor: ThreadPoolExecutor = object : ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS.toLong(), TimeUnit.SECONDS,
                sPoolWorkQueue, sThreadFactory, RejectedHandler()) {
            override fun execute(command: Runnable) {
                super.execute(command)
                Log.i(TAG, "ActiveCount=$activeCount")
                Log.i(TAG, "PoolSize=$poolSize")
                Log.i(TAG, "Queue=" + queue.size)
            }
        }
        //允许核心线程空闲超时时被回收
        threadPoolExecutor.allowCoreThreadTimeOut(true)
        THREAD_POOL_EXECUTOR = threadPoolExecutor
    }

    private inner class RejectedHandler : RejectedExecutionHandler {
        override fun rejectedExecution(r: Runnable, executor: ThreadPoolExecutor) {
            //可在这里做一些提示用户的操作
            Toast.makeText(mContext.get(), "当前执行的任务过多，请稍后再试", Toast.LENGTH_SHORT).show()
        }
    }

    private val mContext: WeakReference<Context?>
    fun execute(command: Runnable?) {
        THREAD_POOL_EXECUTOR!!.execute(command)
    }

    companion object {
        private val TAG = ThreadPools::class.java.simpleName
        private var THREAD_POOL_EXECUTOR: ExecutorService? = null
        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
        private val CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4))
        private val MAXIMUM_POOL_SIZE = CPU_COUNT * 2
        private const val KEEP_ALIVE_SECONDS = 60
        private val sPoolWorkQueue: BlockingQueue<Runnable> = LinkedBlockingQueue(8)
        private val sThreadFactory: ThreadFactory = object : ThreadFactory {
            private val mCount = AtomicInteger(1)
            override fun newThread(r: Runnable): Thread {
                return Thread(r, "MangoTask #" + mCount.getAndIncrement())
            }
        }
        var instance: ThreadPools? = null
            get() {
                if (field == null) {
                    field = ThreadPools(context)
                }
                return field
            }
            private set
    }

    init {
        mContext = WeakReference(context)
        initThreadPool()
    }
}