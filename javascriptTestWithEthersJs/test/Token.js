const { expect } = require("chai");
const { assert } = require("chai");

const { deploy,
    call,
    query,
    setMaxTransactionFee,
    setMaxQueryPayment,
    setDefaultGas,
    setAbi,
    setClient,
    setContractId,
    getClient,
    getOperatorId,
    getOperatorKey} = require("./HederaTestSupport");
const {AccountId, ContractId, AccountCreateTransaction, Hbar, PrivateKey} = require("@hashgraph/sdk");

// specific to this test
const tokenJson = require("../artifacts/contracts/Token.sol/Token.json");
let toAccountAddress;

describe("Token contract", function () {
    // set the timeout to 0 to allow for "long" running asynchronous calls
    this.timeout(0);

    before(async () => {
        console.log(`starting tests`);
        // you may want to override default tx fee and payments (in hbar) globally here
        // setMaxTransactionFee(10);
        // setMaxQueryPayment(10);
        // or do it per call/query

        // set the default gas
        setDefaultGas(200_000);

        // deploy the contract
        // if you already know the contract Id, you may do this instead
        // setContractId(ContractId.fromString("0.0.contractNumber"));

        // constructor parameters
        let parameters = [];
        // deploy the contract
        const contractId = await deploy(tokenJson, parameters);

        // create a new account to transfer to
        const transactionResponse = await new AccountCreateTransaction()
            .setKey(PrivateKey.fromString(getOperatorKey()))
            .setInitialBalance(Hbar.fromTinybars(1))
            .execute(getClient());

        const receipt = await transactionResponse.getReceipt(getClient());

        toAccountAddress = receipt.accountId.toSolidityAddress();
    });

    it("Transactions - Deployment should assign the total supply of tokens to the owner", async function () {
        // call contract functions (usually only for state modifying functions)
        // set function input parameters
        let parameters = [AccountId.fromString("0.0.11093").toSolidityAddress()];
        let ownerBalance = await call("balanceOf", parameters);
        // total supply has no parameters
        let totalSupply = await call("totalSupply", []);

        expect(totalSupply.toString()).to.equal(ownerBalance[0].toString()); // comparing big numbers
    });

    it("Query - Deployment should assign the total supply of tokens to the owner", async function () {
        // call contract functions (usually only for state modifying functions)
        // set function input parameters
        let parameters = [AccountId.fromString("0.0.11093").toSolidityAddress()];
        // query contract functions (only for non-state modifying functions)
        let ownerBalance = await query("balanceOf", parameters);
        let totalSupply = await query("totalSupply", []);
        expect(totalSupply.toString()).to.equal(ownerBalance[0].toString()); // comparing big numbers
    });

    it("Transfer to account", async function () {
        // call contract function
        // set function input parameters
        let parameters = [toAccountAddress, 100];
        await call("transfer", parameters);

        // query for result
        const toBalance = await query("balanceOf", [toAccountAddress]);

        expect("100").to.equal(toBalance[0].toString()); // comparing big numbers
    });

    it("Fail transfer to account", async function () {
        try {
            // call contract function
            // set function input parameters
            let parameters = [toAccountAddress, 10000000000];
            await call("transfer", parameters);
            assert.fail("call should have reverted");
        } catch (e) {
            expect(true).to.equal(true);
        }
    });
});
