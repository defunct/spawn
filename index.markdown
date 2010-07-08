# Spawn

Spawn in inspired by Mix. Mix is a build tool for Java that wants nothing more
to work with your environment. Mix is not Pure Java. Mix thinks purity is
inherently evil. Mix is all multi-culti and ready to talk POSIX.

Yeah, but, it sure is hard to talk to environment in Java.

Example code.

More stuff.

## How It Works

All programs emit a byte stream. If some of the output is character based, then
characters are emitted, but a character sink that writes to a byte stream is
also one of the outputs.

You can add pipes that will process the stream line by line as characters,
that's what a pipe is. There is no byte based equlivalent, because if you were
operating on bytes, you should not be doing so through this library.

There once was an interface called `Pump` that took an output stream that I
believe was supposed to initiate a pipeline.

There is no pipline processing of standard error.
