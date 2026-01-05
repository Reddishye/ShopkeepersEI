# ShopkeepersEI - ExecutableItems integration for Shopkeepers ![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/Reddishye/ShopkeepersEI/total)

[![Development Build](https://github.com/Reddishye/ShopkeepersEI/actions/workflows/prerelease.yml/badge.svg)](https://github.com/Reddishye/ShopkeepersEI/actions/workflows/prerelease.yml) [![Release](https://github.com/Reddishye/ShopkeepersEI/actions/workflows/release.yml/badge.svg)](https://github.com/Reddishye/ShopkeepersEI/actions/workflows/release.yml)

This is a Bukkit plugin that integrates **ExecutableItems** with the **Shopkeepers** plugin: With this plugin, shopkeepers can automatically update any ExecutableItems they contain to their latest version.

## Prerequisites

- This plugin might only support [Paper](https://papermc.io/) currently.
- [Shopkeepers plugin](https://www.spigotmc.org/resources/shopkeepers.80756/)
- [ExecutableItems plugin](https://www.spigotmc.org/resources/custom-items-plugin-executable-items.77578/)

## Usage

While this plugin is running, execute the [`/shopkeepers updateItems`](https://github.com/Shopkeepers/Shopkeepers-Wiki/wiki/Commands#shopkeeper-updateitems) command: This updates any ExecutableItems inside shopkeepers to their latest version.

## Other Commands

### `/shopkeepersei`

Base command.

Alias: `sei`  
Permission: `shopkeepersEI.about`

### `/shopkeepersei reload`

Reloads the plugin config.

Permission: `shopkeepersEI.reload`  

## Installation

1. Download the plugin Jar (TODO actually host it somewhere)
2. Place the Jar into your server's `plugins` folder
3. Restart your server
