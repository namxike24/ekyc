package ai.ftech.dev.base.eventbus

import org.greenrobot.eventbus.EventBus

object EventBusManager {
    fun register(eventHandler: IEventHandler) {
        EventBus.getDefault().register(eventHandler)
    }

    fun unregister(eventHandler: IEventHandler) {
        EventBus.getDefault().unregister(eventHandler)
    }

    fun post(event: IEvent) {
        EventBus.getDefault().post(event)
    }

    fun postPending(event: IEvent) {
        EventBus.getDefault().postSticky(event)
    }

    fun removeSticky(event: IEvent) {
        EventBus.getDefault().removeStickyEvent(event)
    }
}
