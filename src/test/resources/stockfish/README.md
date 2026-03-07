# Stockfish Dockerfile

## Build image
```bash
docker build -t stockfish-tcp .
```

##  Run container
```bash
docker run -d -p 5555:5555 -l stockfish stockfish-tcp
```

## Interact with container

### Useful links
- See [UCI-&-Commands](https://official-stockfish.github.io/docs/stockfish-wiki/UCI-&-Commands.html) for all commands
- Install [ncat](https://nmap.org/ncat/) to connect via tcp with stockfish

### Example
```bash
# connect to docker container
ncat localhost 5555

# option calculate the best 3 moves
setoption name MultiPV value 3

# inject position
position fen 1r4r1/5p1k/p2p1q1p/2b1nPQ1/p7/6RP/B1R2PPK/2B5 b - - 0 1

# calc next best move with depth 5
go depth 5
```