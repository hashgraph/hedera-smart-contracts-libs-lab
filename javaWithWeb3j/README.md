# Hedera Java SDK with Web3.j

com.hedera.examples.Example use of the Hedera Java SDK along with the Web3.j library.

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

```shell
./gradlew build
./gradlew runExample
```

will output

```shell
> Task :runExample
Deploying contract
contract bytecode file: 0.0.29563673
new contract ID: 0.0.29563674

get_message Query
Hello Hedera

Calling set_message with 'Hello again parameter value

get_message Query
Hello again

get_message transaction
Hello again


```
