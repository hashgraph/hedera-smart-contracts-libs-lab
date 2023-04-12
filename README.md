# Smart contracts libs labs

Smart contract examples in various languages showing use of Hedera SDKs in tandem with public libraries to extend the SDKs' capabilities.

* generate function calls using the contract's ABI
* interpret contract responses using the contract's ABI
* interpret event information using the contract's ABI
* process errors reported by smart contracts in the event of a REVERT

## Prerequisites

* Node v16.13.0
* Yarn v1.22.10
* Java v14.0.2

## Setup prior to running the examples

```shell
cp .env.sample .env
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
* Javascript SDK with [abi-decoder](https://github.com/ConsenSys/abi-decoder)
  * Only suitable for event decoding
* Java SDK with [web3.j](https://docs.web3j.io/4.8.7/)
* Unit testing using Chai+Mocha and Ethers.js
* Unit testing using Chai+Mocha and Web3.js
* Javascript Decode revert errors
* ECRecover example
* Others TBD (Golang)

## Contributing

Contributions are welcome. Please see the [contributing guide](.github/CONTRIBUTING.md) to see how you can get involved.

## Code of Conduct

This project is governed by the Contributor Covenant Code of Conduct. By participating, you are expected to uphold this code of conduct. Please report unacceptable behavior to oss@hedera.com.

## License

[Apache License 2.0](LICENSE)
