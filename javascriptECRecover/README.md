# Hedera Javascript SDK with Ethers.js

Example use of ECRecover

## Run the example

The example runs through the following steps
* Deploying the contract to Hedera 
* Hash a string and sign using an ECDSA private key
* Calls the contract's `VerifyMessage` function (uses both a call and query)
* Compares the result with the ethereum address extracted from the ECDSA private key

* Uses an alternative library from OpenZepplin in `alternativeRecoverTest.js`

```shell
npm install
npx hardhat compile

node ecRecoverTest.js
node alternativeRecoverTest.js
```

will output

```shell
Deploying the contract
new contract ID: 0.0.4061760
Wallet address from private key 0x7C1376A97b8Faa9aCa4BC486d8738B8517231D23
Response from smart contract (execute) = 0x7C1376A97b8Faa9aCa4BC486d8738B8517231D23
Response from smart contract (query) = 0x7C1376A97b8Faa9aCa4BC486d8738B8517231D23
```
