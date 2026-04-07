# ChainBreaker (连锁破坏)

A NeoForge mod for Minecraft 1.21.1 that allows players to easily mine veins of ores or chop down entire trees with a single block break.

## Features Currently Implemented

*   **Core Mining Logic**: Efficient BFS (Breadth-First Search) algorithm to find and break connected blocks of the same type.
*   **Player State Synchronization**: Seamlessly toggle the chain-mining mode via a dedicated hotkey (`ChainKeyPacket`), perfectly synchronized between client and server.
*   **Visual Feedback**: High-quality, custom block outline renderer (`ChainBreakerHighLightRenderer`) that accurately highlights all blocks that will be broken in the chain.
*   **Durability & Hunger Integration**: Fully integrated with vanilla Minecraft mechanics. It correctly deducts tool durability for each block broken and stops automatically to protect the tool from breaking.
*   **Custom Items & Creative Tab**: Added a custom creative mode tab ("连锁破坏") and a base item.

## Roadmap / Future Plans

We are currently evolving the mod from a simple utility into a fully integrated survival experience. The upcoming features include:

1.  **The "Chain Gem" (连锁宝石)**
    *   Rename the current `chain_core` base item to `chain_gem`.
    *   This gem will serve as the core magical component for all chain-breaking tools.
2.  **Natural World Generation**
    *   Add a new custom block: `Chain Ore` (连锁矿石).
    *   Configure NeoForge world generation so this ore spawns naturally deep underground. Players will need to mine it to obtain the Chain Gem.
3.  **Crafting Custom Tools**
    *   Implement custom tools (e.g., `Chain Pickaxe`, `Chain Axe`) that extend vanilla tool classes.
    *   Use the Datagen system to automatically generate crafting recipes. Players will use the Chain Gem along with other materials to craft these specialized tools.
    *   **Mechanic Update**: The chain-mining ability will be restricted *only* to these specialized crafted tools, making them valuable late-game items.

## Development

This mod is built using NeoForge 21.1.220 for Minecraft 1.21.1.

### Setup
Run `./gradlew genIntellijRuns` (or the equivalent for your IDE) to generate the run configurations.

### Build
Run `./gradlew build` to compile the mod into a `.jar` file located in `build/libs/`.