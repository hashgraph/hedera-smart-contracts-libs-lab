# Hedera Javascript decode revert

On some occasions, a smart contract will revert and the error unfortunately doesn't contain much information to help determine the reason for the contract reverting.

Hedera stores an `error_message` in the transaction record which is available from a mirror node after the transaction failed.

The error message contains the hash of a solidity function or a plain text error, along with parameters which can be decoded using online tools such as https://www.4byte.directory/signatures/

The script queries a mirror node for the last result of a given `contract id` at this endpoint `/api/v1/contracts/${contractIdOrAddress}/results` or if a `transaction Id` is provided, it will first query `/api/v1/contracts/${contractIdOrAddress}/results/${transactionIdOrHash}` then for each result will query `/api/v1/contracts/${contractIdOrAddress}/results` 

* Determines the hash of the function contained within the `error_message`
* Queries the www.4byte.directory API for the signature of a function corresponding to this hash
* Creates an ABI from the signature
* Uses `abiDecoder` to decode the parameters passed to the function
* If one of the parameters is an address, it outputs the corresponding Hedera entity Id (ContractId, AccountId ...)
* If one of the parameters is bytes, it attempts to recursively determine if that was another error function call (this happens if a contract calls a contract that calls a contract)
* Outputs the results (each level of nesting is preceded with ..)

## Note

* This may be subject to using a recent version of solidity
* Only the last result for the given contract is fetched from the mirror node if a `contractId` is supplied

## Run the example

the script takes one command line parameter and then prompts for either a contractId or transactionId

* the network being used (mainnet-public, testnet or previewnet)

When prompted, input either a contractId (e.g. 0.0.234234) or a transactionId (e.g. 0.0.28526596@1652950405.995377309)

```shell
yarn install

node index.js testnet
```

will output (note this example has nested contract calls) the following for a `contractId`

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

or if a transactionId is supplied

```shell
Client api call result
Error is BootstrapCallFailedError
Parameter (address) = 0x000000000000000000000000000000000213abd6
=> Hedera address 0.0.34843606

Parameter (bytes) = 0xd19d65df000000000000000000000000000000000000000000000000000000000213abdc00000000000000000000000000000000000000000000000000000000000000400000000000000000000000000000000000000000000000000000000000000000

..Error is BootstrapCallFailedError
..Parameter (address) = 0x000000000000000000000000000000000213abdc
..=> Hedera address 0.0.34843612

..Parameter (bytes) = null
```

in the event the errors don't correspond to a known signature, the following will be output

```shell
Client api call result
{
  callResult: '0x',
  error_message: '0x',
  from: '0.0.11093 (0x0000000000000000000000000000000000002b55)',
  to: '0.0.34908280 (0x000000000000000000000000000000000214a878)',
  blockHash: '0x7e901df3b3a9a2f7464a958a35bd4731e26b53ca828abcc3eabbb6538e52058a966e727bd195cf37b95e19095ece6307',
  result: 'CONTRACT_REVERT_EXECUTED',
  status: '',
  contractId: '0.0.34908280'
}

Nested contract call
{
  callResult: '0x00000000000000000000000000000000000000000000000000000000000000b8',
  error_message: 'TOKEN_NOT_ASSOCIATED_TO_ACCOUNT',
  from: '0.0.11093 (0x0000000000000000000000000000000000002b55)',
  to: '0.0.359 (0x0000000000000000000000000000000000000167)',
  blockHash: '0x7e901df3b3a9a2f7464a958a35bd4731e26b53ca828abcc3eabbb6538e52058a966e727bd195cf37b95e19095ece6307',
  result: 'TOKEN_NOT_ASSOCIATED_TO_ACCOUNT',
  status: '',
  contractId: '0.0.359'
}
```
