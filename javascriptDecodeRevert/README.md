# Hedera Javascript decode revert

On some occasions, a smart contract will revert and the error unfortunately doesn't contain much information to help determine the reason for the contract reverting.

Hedera stores an `error_message` in the transaction record which is available from a mirror node after the transaction failed.

The error message contains the hash of a solidity function, along with parameters which can be decoded using online tools such as https://www.4byte.directory/signatures/

The project queries a mirror node for the last result of a given contract id at this endpoing (https://testnet.mirrornode.hedera.com/api/v1/contracts/${contractId}/results) and

* Determines the hash of the function contained within the `error_message`
* Queries the www.4byte.directory API for the signature of a function corresponding to this hash
* Creates an ABI from the signature
* Uses `abiDecoder` to decode the parameters passed to the function
* If one of the parameters is an address, it outputs the corresponding Hedera entity Id (ContractId, AccountId ...)
* If one of the parameters is bytes, it attempts to recursively determine if that was another error function call (this happens if a contract calls a contract that calls a contract)
* Outputs the results (each level of nesting is preceded with ..)

## Note

* This may be subject to using a recent version of solidity
* Only the last result for the given contract is fetched from the mirror node

## Run the example

the script takes two command line parameters

* the network being used (mainnet-public, testnet or previewnet)
* the ID of the contract (e.g. 0.0.234234)

```shell
yarn install

node index.js testnet 0.0.34843606
```

will output (note this example has nested contract calls)

```shell
Error is BootstrapCallFailedError
Parameter (address) = 0x000000000000000000000000000000000213abd6
=> Hedera address 0.0.34843606

Parameter (bytes) = 0xd19d65df000000000000000000000000000000000000000000000000000000000213abdc00000000000000000000000000000000000000000000000000000000000000400000000000000000000000000000000000000000000000000000000000000000

..Error is BootstrapCallFailedError
..Parameter (address) = 0x000000000000000000000000000000000213abdc
..=> Hedera address 0.0.34843612

..Parameter (bytes) = null
```
