package ai.ftech.ekyc.utils

import ai.ftech.ekyc.domain.event.IFbaseEvent
import kotlinx.coroutines.flow.MutableSharedFlow

object ShareFlowEventBus {
    var events = MutableSharedFlow<IFbaseEvent?>()
        private set

    suspend fun emitEvent(event: IFbaseEvent) {
        events.emit(event)
    }
}
