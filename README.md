
# Aether Potions

This is a free spigot plugin intends to somewhat revolutionize custom potion systems on minecraft servers.




## Features

- Lightweight as feather.
- Unlimited drinkable and splash potions.
- Custom particles; type, amount and time for potions.
- Cooldown for potions.
- Execute commands when you drink.
- Decide whether bottle will be wiped out or not.
- Select the color of potion.
- Make a custom name and lore for every potion.
- Execute commands after effect expires.


## Installation

#### Requirements
- WorldGuard & WorldEdit
- Preferably 1.18 or higher, but i think it supports 1.13+
#### Plugin Installation
- Put the AetherPotions to plugins folder.
- Start & stop the server to generate config files.
- You can start and configure while server is online. You can use `/aetherpotions reload` command to reload configurations.
## FAQ

#### Can you stack potions?
I specifically made them non stackable, as it is generally an unsafe operation to stack non stackable even though i can allow it. Like when you give it with command, players can store "stacked" potions, it can cause some unfair things etc..

#### How can I troubleshoot basic config issues?

I have a dedicated potion validation checker in the plugin. You can use the command `/aetherpotions info <id> [detailed(true/false)]` to get info about the potion. If this potion has common config errors, it will show.

[detailed] variable just enables more detailed such as full lore, after effect details etc..


## Support

Although I don't use my email often(info@kardasland.com), my primary community will be on [Discord](https://discord.gg/SMJBw55Fbh) server.


## Authors

- [@kardasland](https://www.github.com/kardasland)


## Deployment

Pretty simple. Just clone and let maven do the rest.

```bash
  mvn install
```


## Acknowledgements

- [NBTEditor by BananaPuncher714](https://github.com/BananaPuncher714/NBTEditor)

