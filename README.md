# Smart contracts libs labs

Smart contract examples in various languages showing use of Hedera SDKs in tandem with public libraries to extend the SDKs' capabilities.

* generate function calls using the contract's ABI
* interpret contract responses using the contract's ABI
* interpret event information using the contract's ABI

## Prerequisites

* Node v16.13.0
* Yarn v1.22.10

## Setup prior to running the examples

```shell
yarn install
cd ../.env.sample ../.env
```

edit `.env` file and set your testnet operator id and key

## Compiled solidity

The contract used for these examples is `stateful.sol` which is located in `./contracts`.
For convenience, the contract was compiled using `Remix` and the output from the compilation included in this project

* `abi` => `./contracts/abi.json`
* `bytecode` => `./contracts/bytecode.json`

## Included examples

* Javascript SDK with [Ethers.js](https://docs.ethers.io/v5/)
* Javascript SDK with [web3.js](https://web3js.readthedocs.io/en/v1.7.0/)
* Java SDK with [web3.j](https://docs.web3j.io/4.8.7/)
* Others TBD (Golang, Javascript with [abi-decoder](https://github.com/ConsenSys/abi-decoder))
