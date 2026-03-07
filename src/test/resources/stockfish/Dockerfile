FROM debian:stable-slim AS build

ARG STOCKFISH_GITHUB_TAG=sf_18
ARG ARCH=x86-64-sse41-popcnt

RUN apt-get update && \
    apt-get install -y \
    git \
    make \
    g++ \
    wget

# Download and build Stockfish
RUN mkdir stockfish &&  \
    git clone --depth 1 --branch ${STOCKFISH_GITHUB_TAG} https://github.com/official-stockfish/Stockfish.git /stockfish

WORKDIR /stockfish/src

RUN make -j build ARCH=${ARCH}

# ---- runtime stage ----
FROM debian:stable-slim

RUN apt-get update && apt-get install -y \
     socat

COPY --from=build /stockfish/src/stockfish /usr/local/bin/stockfish

EXPOSE 5555

# starts tcp server and forward each incoming connection to a new Stockfish process (fork)
CMD ["sh", "-lc", "socat TCP-LISTEN:5555,reuseaddr,fork EXEC:'/usr/local/bin/stockfish',pty,stderr"]
