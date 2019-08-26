# evt4j<!-- omit in toc -->

Official Java SDK for everiToken public chain.

> This SDK also has an [**example package**](https://github.com/everitoken/evt4j/blob/master/src/main/java/io/everitoken/sdk/java/example/) set up, which lists various useful code examples for quick references on how to interact with everiToken public chain.

- [Install](#install)
  - [use with Maven project](#use-with-maven-project)
  - [use with Gradle project](#use-with-gradle-project)
  - [other](#other)
- [Usage overview](#usage-overview)
- [PrivateKey usage](#privatekey-usage)
- [Api usage](#api-usage)
- [Action usage](#action-usage)
- [EvtLink usage](#evtlink-usage)
  - [EvtLink generation](#evtlink-generation)
- [Deploy](#deploy)

## Install

### use with Maven project

In project `pom.xml` file

> You can also check [evt4j-maven-project-demo](https://github.com/everitoken/evt4j-maven-project-demo) for insights on how to use evt4j with maven project

```xml
<dependencies>
    <dependency>
        <groupId>io.everitoken.sdk</groupId>
        <artifactId>chain-sdk</artifactId>
        <version>1.4.4</version>
    </dependency>
</dependencies>
```

### use with Gradle project

In project `build.gradle` file

> You can also check [evt4j-gradle-project-demo](https://github.com/everitoken/evt4j-gradle-project-demo) for insights on how to use evt4j with gradle project

```gradle
dependencies {
    // other dependencies
    implementation 'io.everitoken.sdk:chain-sdk:1.4.4'
}
```

### other

Build jar with all the dependencies, run the following command

`mvn clean compile assembly:single`

It will generate jar with all dependencies under `target` folder

Use maven command to install `jar` as dependency

```console
$ mvn install:install-file -Dfile=path/to/jar/file \
                           -DgroupId=io.everitoken.sdk \
                           -DartifactId=chain-sdk \
                           -Dversion=version \
                           -Dpackaging=jar

```

## Usage overview

Here is the code example which highlights the common usage of the SDK.

<details>
<summary>Click to see full code example</summary>

```java

package io.everitoken.sdk.java.example;

import java.util.Arrays;
import java.util.List;

import io.everitoken.sdk.java.Address;
import io.everitoken.sdk.java.Api;
import io.everitoken.sdk.java.Asset;
import io.everitoken.sdk.java.PrivateKey;
import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.abi.TransferFungibleAction;
import io.everitoken.sdk.java.dto.NodeInfo;
import io.everitoken.sdk.java.dto.TransactionData;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.service.TransactionConfiguration;
import io.everitoken.sdk.java.service.TransactionService;

class BasicUsage {
public static void main(String[] args) {
        // generate a key pair
        PrivateKey privateKey = PrivateKey.randomPrivateKey();
        PublicKey publicKey = privateKey.toPublicKey();

        // construct a NetParams to interact with the node
        NetParams netParams = new TestNetNetParams();

        // init Api instance
        Api api = new Api(netParams);

        // get information of the node
        try {
            NodeInfo info = api.getInfo();
            System.out.println(info.getChainId());
        } catch (ApiResponseException e) {
            System.out.println(e.getRaw());
        }

        // get balance of all fungible tokens (for example: EVT Token) for a public key
        try {
            // do something with balance list
            List<Asset> balances = api.getFungibleBalance(Address.of(publicKey));
        } catch (ApiResponseException e) {
            System.out.println(e.getRaw());
        }

        // transfer fungible tokens to others

        // construct an action to represent the transfer
        TransferFungibleAction transferFungibleAction = TransferFungibleAction.of("2.00002 S#20",
                "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND",
                "EVT8aNw4NTvjBL1XR6hgy4zcA9jzh1JLjMuAw85mSbW68vYzw2f9H", "testing java");

        try {
            // init transaction service with net parameters
            TransactionService transactionService = TransactionService.of(netParams);

            // init transaction configuration
            TransactionConfiguration trxConfig = new TransactionConfiguration(1000000, publicKey,
                    KeyProvider.of(privateKey.toWif()));

            // push this action to the node and get back an transaction
            TransactionData txData = transactionService.push(trxConfig, Arrays.asList(transferFungibleAction));
            System.out.println(txData.getTrxId());
        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }
}

```

</details>

## PrivateKey usage

**static** `randomPrivateKey`

<details>
<summary>Click to see code example</summary>

```java
import io.everitoken.sdk.java.PrivateKey;

PrivateKey privateKey = PrivateKey.randomPrivateKey();
```

</details>

**static** `seedPrivateKey`

<details>
<summary>Click to see code example</summary>

```java
import io.everitoken.sdk.java.PrivateKey;

PrivateKey seedPrivateKey = PrivateKey.seedPrivateKey("a random string");
```

</details>

**static** `of`

<details>
<summary>Click to see code example</summary>

```java
import io.everitoken.sdk.java.PrivateKey;

PrivateKey privateKeyFromWif = PrivateKey.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D");
```

</details>

**static** `isValidPrivateKey`

<details>
<summary>Click to see code example</summary>

```java
import io.everitoken.sdk.java.PrivateKey;

boolean valid = PrivateKey.isValidPrivateKey("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D");

```

</details>

`toPublicKey`

<details>
<summary>Click to see code example</summary>

```java
import io.everitoken.sdk.java.PrivateKey;
import io.everitoken.sdk.java.PublicKey;

PrivateKey privateKey = PrivateKey.randomPrivateKey();
PublicKey publicKey = privateKey.toPublicKey();

```

</details>

`toWif`

<details>
<summary>Click to see code example</summary>

```java
import io.everitoken.sdk.java.PrivateKey;

PrivateKey privateKey = PrivateKey.randomPrivateKey();
System.out.println(privateKey.toWif());

```

</details>

## Api usage

By instantiate an `Api` instance, you will be able to use it to interact with the specified remote node.

> Refer to [ApiExample.java](https://github.com/everitoken/evt4j/blob/master/src/main/java/io/everitoken/sdk/java/example/ApiExample.java) in our [example package](https://github.com/everitoken/evt4j/blob/master/src/main/java/io/everitoken/sdk/java/example/) for detailed code examples.

## Action usage

An **Action** in an instruction to perform a given task on everiToken public chain. In order to send **Action**, the workflow is to:

1. construct the given action locally
2. instantiate an `TransactionService` to push the action (or actions) to the chain

> Refer to [example package](https://github.com/everitoken/evt4j/blob/master/src/main/java/io/everitoken/sdk/java/example/) for more code examples of each **Action**.

<details>
<summary>Here is the code example showing how to create a domain on everiToken public chain</summary>

```java
// instantiate net parameter, can also be main net
final NetParams netParam = new TestNetNetParams();

// specify the content of the action
final String actionData = "...";
final JSONObject json = new JSONObject(actionData);

// use json data to build the NewDomainAction, alternatively you can also build with other constructs, check NewDomainAction class for more details
final NewDomainAction newDomainAction = NewDomainAction.ofRaw(json.getString("name"), json.getString("creator"),
        json.getJSONObject("issue"), json.getJSONObject("transfer"), json.getJSONObject("manage"));

try {
    // init *TransactionService* with a net parameter
    TransactionService transactionService = TransactionService.of(netParam);

    // construct *TransactionConfiguration*
    TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
            PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"),
            KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D"));

    // push the action to chain. Note: you can also pass multiple actions here
    TransactionData txData = transactionService.push(trxConfig, Arrays.asList(newDomainAction));

    // get the transaction data
    System.out.println(txData.getTrxId());
} catch (final ApiResponseException ex) {
    System.out.println(ex.getRaw());
}

```

</details>

## EvtLink usage

### EvtLink generation

`EvtLink` is the place to generate and parse QR Codes using `EVT Link`'s syntax. `EVT Link` can be used for `everiPass`, `everiPay`, `Address Code for Receiver`.

For further information, read [Documentation for EvtLink / everiPass / everiPay](https://www.everitoken.io/developers/deep_dive/evtlink,_everipay,_everipass).

**static** `getEvtLinkForEveriPass`

Generate `EvtLink` for everiPass.

<details>
<summary>Click to see code example</summary>

```java

NetParams netParams = new TestNetNetParams();

// Init new EvtLink instance with given net param
EvtLink evtLink = new EvtLink(netParams);

// make sure the domain and token you use exist and has correct authorize keys
// replace "domainName" and "tokenName" with your custom values
EvtLink.EveriPassParam everiPassParam = new EvtLink.EveriPassParam(true, "domainName", "tokenName");

String passText = evtLink.getEvtLinkForEveriPass(everiPassParam,
        SignProvider.of(KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D")));

// will print out the content of evt link
System.out.println(passText);

try {
    // will print PNG image data url, e.g. "data:image/png;base64,..."
    System.out.println(Utils.getQrImageDataUri(passText));
} catch (Exception e) {
    // handle exception creating QR image data
}

```

</details>

**static** `getEvtLinkForEveriPay`

Generate a `EvtLink` for `EveriPay` is similar to the one shown above for `EveriPass`.

<details>
<summary>Click to see code example</summary>

```java
String uniqueLinkId = EvtLink.getUniqueLinkId();
int symbolId = 1;
int maxAmount = 100;

// init Api connection to the node
NetParams netParams = new TestNetNetParams();

// init evtLink instance with api instance
EvtLink evtLink = new EvtLink(netParams);

// init everiPayParam which representing the everiPay action, which contains
//      - symbolId (e.g. 1 for Evt),
//      - uniqueLinkId (which can be generated with the helper function (getUniqueLinkId) from EvtLink class),
//      - maxAmount
EvtLink.EveriPayParam everiPayParam = new EvtLink.EveriPayParam(symbolId, uniqueLinkId, maxAmount);

// the string generated here can be encoded in to QR code, refer to example "getEvtLinkForEveriPay" for code snippet
String payText = evtLink.getEvtLinkForEveriPay(everiPayParam,
        SignProvider.of(KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D")));

```

</details>

## Deploy

- setup user `settings.xml` for maven, where username and password should be specified
- obtain the gpg key and password
- run `mvn clean javadoc:jar deploy` which will deploy a **stage** version
- run `mvn nexus-staging:release`

> use `master mvn clean compile assembly:single` to build with all dependencies
