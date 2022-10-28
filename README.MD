# RSALogin
RSALogin is a forge mod which restricts players' access to a server, including non-Premium ones. However, it's required to combine it with Minecraft Vanilla access utilities: whitelist, IP list...

It was born because of necessity: a server of mine required a login factor and there were no, 100% working, login mods. Therefore, this mod is **NOT** expected to have new versions/patches, although it may receive an occasional update.

## Introduction
RSALogin has two main features:
* Identify users by a unique id. In practice, it works as a login protocol.
* Prevent user login when another player with the same name is logged in.

## How it works?
It is based on a simple login protocol: every user has a password and uses it to identify itself (username included). However, we don't need to worry the user about password creation: RSALogin creates it. This password will be stored in **.minecraft** directory, in a file called **.rsalogin_key**.

Once the client connects to the server, it is required to send the previously generated key to the server. As it is intended to make this a secure communication, the password is hashed (using SHA256) and encrypted (using RSA) applying the server's public key (communicated previously to the client).

Finally, the server receives the key. Then, it is decrypted (using server's private key) and hashed again to store it (when the server is closing) in a file called **rsalogin_players.json**, where all logged-in users are located.

Take into account that **.rsalogin_key** is read the first time the user joins a server, whereas **rsalogin_players.json** is read when the server is starting.

## Installation
Place the **.jar** file into the **mods** directory on both server-side and client-side.

## Commands
* */rsalogin save* - It forces the storage from in-game login-registered players into **rsalogin_players.json**.
* */rsalogin reload* - It forces in-game player list reload from **rsalogin_players.json**.
* */rsalogin list* - It displays in-game player list.
* */rsalogin unregister playerName* - It removes a player from in-game player list.

## Tips
To use multiple Minecraft clients, you need to copy the **.rsalogin_key** file to all your clients, into **.minecraft** directory. Notice that the file will be created a single time, when connecting to any Minecraft server for the first time.