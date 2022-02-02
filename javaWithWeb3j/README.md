# Hedera Java SDK with Web3.j

com.hedera.examples.Example use of the Hedera Java SDK along with an improved version of the Web3.j library.

## Running the examples

The example runs through the following steps
* Deploying the contract to Hedera with a `Hello Hedera` constructor parameter value (this emits a `SetMessage` event)
* Queries the contract for the current message value
* Sets the contract message to `Hello again` (this emits a `SetMessage` event)
* Uses a query to fetch the contract's current message value (`get_message`)
* Uses a transaction to fetch the contract's current message value (`get_message`)
* Calls `set_message` with the current date time as a parameter (this emits a `SetMessage` event)
* Fetches the emitted event using a record
* Pauses 10s
* Fetches all the events for the contract using a mirror node query

### Web3.j fork

A fork of web3.j has been created here in anticipation of a possible PR to the main project.

This fork adds some features to the web3.j code generators so that generated classes can be used alongside the Hedera Java SDK to deploy contracts, construct contract calls and decode responses. 

For now, it is necessary to install this fork locally as follows

_Note: the fork uses a version 5.6.3 of Gradle which requires java 12_

```shell
git clone https://github.com/gregscullard/web3j.git
cd web3j
git checkout abi_encode_decode
./gradlew publishToMavenLocal
```

The above commands will compile web3j as version `4.8.9-SNAPSHOT` which can be imported into your project.

### Compile and run the example

_Note: Remember to switch java versions if necessary_

* Build

```shell
./gradlew build
```

* Generate java code from ABI

This executes the `GenerateWrappers.java` class which takes input from the `./Solidity` folder containing the ABIi and bytecode for the contract.

The result of the command is a class called `Stateful` which is a java interface to the contract itself.

```shell
./gradlew codeGen
```

```shell
./gradlew runExample
```

will output

```shell
Deploying contract
contract bytecode file: 0.0.29602055
new contract ID: 0.0.29602056

get_message Query
Hello Hedera

Calling set_message with 'Hello again parameter value

get_message Query
Hello again

get_message transaction
Hello again

Getting event(s) from record
Calling set_message to trigger new event

get_message Query
Wed Feb 02 14:03:33 CET 2022

Events' data
From (AccountId): 0x0000000000000000000000000000000000002b55 (0.0.11093)
Message : Wed Feb 02 14:03:33 CET 2022

Getting event(s) from mirror
Waiting 10s to allow transaction propagation to mirror
From (AccountId): 0x0000000000000000000000000000000000002b55 (0.0.11093)
Message : Hello Hedera
From (AccountId): 0x0000000000000000000000000000000000002b55 (0.0.11093)
Message : Hello again
From (AccountId): 0x0000000000000000000000000000000000002b55 (0.0.11093)
Message : Wed Feb 02 14:03:33 CET 2022
```
