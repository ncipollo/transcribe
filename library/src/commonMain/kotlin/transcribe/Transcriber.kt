package transcribe

interface Transcriber<In, Out, Context> {
    fun transcribe(
        input: In,
        context: Context,
    ): TranscribeResult<Out>
}
