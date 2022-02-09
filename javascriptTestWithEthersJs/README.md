# Hedera Javascript unit testing with HardHat, Ethers.js, chai and mocha

Example use of the Hedera JavaScript SDK along with Ethers.js to achieve unit testing of contracts.

We will use Hardhat to compile the contracts into bytecode + ABI.

## Setup

```shell
yarn install
```

## Run the example

The example runs through the following steps to test the contract
* Deploys the `Token.sol` contract
* Creates an account
* Calls the `balanceOf` contract function and tests the output 
* Calls the `totalSupply` contract function and tests the output
* Queries the `balanceOf` contract function and tests the output
* Queries the `totalSupply` contract function and tests the output
* Calls the `transfer` contract function, sending 100 to tne account created at the beginning
* Queries the `balanceOf` contract function for the account and tests the output
* Calls the `transfer` contract function with an amount higher than total supply, checks the contract reverts

```shell
yarn install
npx hardhat
```

choose `Create an empty hardhat.config.js`

```shell
npx hardhat compile
```

will compile the sample `contracts\Token.sol` and place the output in `artifacts`

then to run the tests which are in `test`

```shell
npm test
```

will output 

```shell
  Token contract
starting tests

Deploying the contract
new contract ID: 0.0.29644269
calling balanceOf with [0000000000000000000000000000000000002b55]
[ BigNumber { _hex: '0x0f4240', _isBigNumber: true } ]
calling totalSupply with []
[ BigNumber { _hex: '0x0f4240', _isBigNumber: true } ]
    ✔ Transactions - Deployment should assign the total supply of tokens to the owner (9316ms)
querying balanceOf with [0000000000000000000000000000000000002b55]
[ BigNumber { _hex: '0x0f4240', _isBigNumber: true } ]
querying totalSupply with []
[ BigNumber { _hex: '0x0f4240', _isBigNumber: true } ]
    ✔ Query - Deployment should assign the total supply of tokens to the owner (1693ms)
calling transfer with [0000000000000000000000000000000001c455ee,100]
[]
querying balanceOf with [0000000000000000000000000000000001c455ee]
[ BigNumber { _hex: '0x64', _isBigNumber: true } ]
    ✔ Transfer to account (4446ms)
calling transfer with [0000000000000000000000000000000001c455ee,10000000000]
    ✔ Fail transfer to account (1722ms)


  4 passing (30s)
```

## Additional information

The `test/HederaTestSupport.js` script contains a number of supporting functions to help with the creation of test scripts.

An example test suite in `test/Token.js` executes the tests themselves, for example

We import the artifact created by HardHat

```javascript
const tokenJson = require("../artifacts/contracts/Token.sol/Token.json");
```

Setup the test by deploying the contract 

```javascript
describe("Token contract", function () {
    // set the timeout to 0 to allow for "long" running asynchronous calls
    this.timeout(0);

    before(async () => {
        console.log(`starting tests`);
        // set the default gas
        setDefaultGas(200_000);
        // deploy the contract
        // constructor parameters
        let parameters = [];
        // deploy the contract
        const contractId = await deploy(tokenJson, parameters);
```

and creating an account

```javascript
        // create a new account to transfer to
        const transactionResponse = await new AccountCreateTransaction()
            .setKey(PrivateKey.fromString(getOperatorKey()))
            .setInitialBalance(Hbar.fromTinybars(1))
            .execute(getClient());

        const receipt = await transactionResponse.getReceipt(getClient());

        toAccountAddress = receipt.accountId.toSolidityAddress();
    });
```

then start the unit tests themselves

```javascript
    it("Transactions - Deployment should assign the total supply of tokens to the owner", async function () {
        // call contract functions (usually only for state modifying functions)
        // set function input parameters
        let parameters = [AccountId.fromString("0.0.11093").toSolidityAddress()];
        let ownerBalance = await call("balanceOf", parameters);
        // total supply has no parameters
        let totalSupply = await call("totalSupply", []);

        expect(totalSupply.toString()).to.equal(ownerBalance[0].toString()); // comparing big numbers
    });
```

