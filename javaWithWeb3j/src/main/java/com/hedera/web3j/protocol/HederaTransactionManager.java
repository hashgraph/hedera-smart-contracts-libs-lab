/*-
 *
 * Smart Contracts Libs Labs
 *
 * Copyright (C) 2019 - 2021 Hedera Hashgraph, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hedera.web3j.protocol;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.TransactionManager;

/** Transaction manager implementation for noop on smart contracts. */
public class HederaTransactionManager extends TransactionManager {
  private final Web3j web3j;
  private String fromAddress;

  public HederaTransactionManager(Web3j web3j, String fromAddress) {
    super(web3j, fromAddress);
    this.web3j = web3j;
    this.fromAddress = fromAddress;
  }

  @Override
  public EthSendTransaction sendTransaction(
          BigInteger gasPrice,
          BigInteger gasLimit,
          String to,
          String data,
          BigInteger value,
          boolean constructor)
          throws IOException {
    throw new UnsupportedOperationException(
            "Unsupported operation");
  }

  @Override
  public EthSendTransaction sendEIP1559Transaction(
          long chainId,
          BigInteger maxPriorityFeePerGas,
          BigInteger maxFeePerGas,
          BigInteger gasLimit,
          String to,
          String data,
          BigInteger value,
          boolean constructor)
          throws IOException {
    throw new UnsupportedOperationException(
            "Unsupported operation");
  }

  @Override
  public String sendCall(String to, String data, DefaultBlockParameter defaultBlockParameter)
          throws IOException {
    throw new UnsupportedOperationException(
            "Unsupported operation");
  }

  @Override
  public EthGetCode getCode(
          final String contractAddress, final DefaultBlockParameter defaultBlockParameter)
          throws IOException {
    throw new UnsupportedOperationException(
            "Unsupported operation");
  }
}
