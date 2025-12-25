package transcribe

interface Transcriber<In, Out> {
    fun transcribe(input: In): TranscribeResult<Out>
}
