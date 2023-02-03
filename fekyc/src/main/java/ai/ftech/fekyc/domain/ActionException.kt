package ai.ftech.fekyc.domain

open class ActionException : Exception {

    constructor() : super()

    constructor(message: String?) : super(message) {}

    constructor(t: Throwable?) : super(t) {}

    constructor(message: String?, t: Throwable?) : super(message, t) {}
}
