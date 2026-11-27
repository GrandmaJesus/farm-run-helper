# Farm Run Helper

Farm Run Helper builds a step-by-step farm run from the patches, teleports and unlocks you have, then guides you through it in a side panel, a bit like a repeatable quest guide.

It was inspired by the herb run and tree run helpers in the [Quest Helper](https://github.com/Zoinkwiz/quest-helper) plugin. Those helpers are great, and this plugin builds on the same idea to cover all of farming: every patch type, bird houses and seaweed, custom runs, and routes that adapt to your own unlocks and teleports.

## Features

- **Ready-made runs:**
  - **Herb run:** every herb patch.
  - **Tree run:** tree, fruit tree, hardwood, calquat, celastrus and redwood patches.
  - **Full Farm run:** every patch type you have unlocked, including the Fossil Island bird houses and seaweed. Only Hespori and the Canifis mushroom patch are left out (add them with a Custom run).
  - **Bird house run:** just the four Fossil Island bird houses.
  - **Custom run:** choose exactly which patch types to include.
- **Every patch type:** herbs, flowers, allotments, bushes, cacti, belladonna, mushrooms, seaweed, anima, Hespori, trees, fruit trees, hardwood trees, calquat, celastrus, redwood and Fossil Island bird houses.
- **Routes that match your account:** quests, achievement diaries, fairy ring access, planted spirit trees, your house location, mounted items and house portals all decide which locations are included and how you get there. Anything you can't reach yet is skipped, with a note saying what's missing.
- **Teleport choice for each location:** pick a teleport for every stop, or leave it on Auto to use the first one you have unlocked. Once you've opened your bank and seed vault, Auto also checks that you own the teleport item.
- **Trips:** if a run needs more inventory space than you allow, it's split into trips with a bank stop between each.
- **Item checklist:** shows what to bring for each trip, and turns green as you pick the items up.
- **Planting order:** each stop lists its patches in the order to plant them. The plugin highlights the next patch (the closest one of that type) and outlines its seed or sapling in your inventory, then moves on once you plant. If you've chosen a compost, the next step after each planting is to compost that patch, with your compost outlined in the inventory. Use **Skip** for a patch or compost step you can't do right now.
- **Patch timers:** reads each patch's real state from the game and shows the time left above growing patches, or Ready, Diseased or Dead where a patch needs you. Patches that are already growing are skipped automatically.
- **Low space warning:** tells you to note your produce at the tool leprechaun when your inventory is nearly full and patches are still left at your stop.
- **Auto-advance:** moves on from a bank step once you're carrying everything for the trip (runes in a rune pouch count), and from a stop once everything there is planted (and composted), skipped or still growing, and when you arrive at a later stop. With RuneLite's Time Tracking plugin on, stops where everything is still growing are skipped before you travel there, with a note saying when they'll be ready (untick a skipped step to go there anyway).
- **Travel highlights:** on the way to your next stop, outlines its teleport item in your inventory or equipment. For routes through your house, it outlines your house tab or Construction cape, then highlights the mounted item, portal or nexus or exit portal once you're inside. Spirit trees and fairy rings are highlighted when your route uses them. If you've ticked House spirit tree or House fairy ring and none is nearby, your house tab is outlined first.
- **Multi-part routes:** routes like Fairy ring DKS then Larry's boat highlight each part in turn: the next spirit tree, fairy ring, mushtree, rowboat or person to talk to.
- **Fossil Island:** the bird houses come first (Verdant Valley, then Mushroom Meadow, then the Tar Swamp entrance), then a short walk to the hardwood patches, then on to the seaweed rowboat. When the next one is in another part of the island, the magic mushtree is highlighted, along with the destination to pick in its menu.
- **Farmer's outfit:** if you own any of it, the trip tells you to wear it and the exported setup includes it.
- **Menu highlights:** the fairy ring code in the fairy ring travel log, the destination in the magic mushtree and quetzal whistle menus, and what to say in dialogues (like picking Weiss when talking to Larry).
- **Bank and seed vault:** outlines the seeds, saplings and other items you still need for the current trip.
- **Teleport highlights:** the spell in your spellbook, the destination in jewellery, Xeric's talisman, jewellery box, portal nexus and spirit tree menus, and right-click teleport options like "Farm Teleport" on an Ardougne cloak.
- **Walking path:** highlights the tiles from you to the next thing to click, or to the next patch before it's in view. Paths are fixed: they're worked out once from the game map and ship with the plugin, so the same route always gets the same path. If you've wandered off it, the nearest one is shown.
- **Inventory Setups export:** copies the current trip as an import string for the Inventory Setups plugin, with the route in the setup's notes.

## Getting started

1. Open the plugin's settings and fill in **Quests**, **Diaries & unlocks** and **Travel**. Hover over any setting to see what it unlocks.
2. Open the Farm Run Helper side panel and use **Seeds** to choose what to plant. Options your Farming, Hunter or Crafting level can't use yet are greyed out.
3. Use **Teleports** to change how you reach any location, or leave them on Auto. Routes that need something you haven't unlocked are greyed out.
4. Open your bank and seed vault once so the plugin knows which teleport items you own.
5. Pick a run and follow the steps. Use **Next** and **Previous**, or tick steps off as you go.

## Settings

| Section | What it controls |
|---|---|
| Custom run contents | Patch types included in a Custom run |
| Quests | Quests that unlock patches, spirit trees and teleports |
| Diaries & unlocks | Diary tiers and other unlocks, such as the Corsair Cove rowboat |
| Travel | Fairy ring access, planted spirit trees, house location, house spirit tree and fairy ring, mounted items, and whether each house teleport uses a portal space or your nexus |
| Trips & items | Inventory space per trip, where to bank between trips, compost and secateurs |
| Display | Auto-advance, patch highlights, the walking path, patch timers and the low space warning |

Seeds and teleports are chosen in the side panel (**Seeds** and **Teleports** buttons), where options you can't use yet are greyed out.

## Notes

- Patches are skipped automatically if your Farming level is too low for the seed you chose, or for that part of the Farming Guild.
- Patch states are read the same way as RuneLite's built-in Time Tracking plugin, using its patch data (BSD 2-Clause, credited in those source files). If Time Tracking has worked out your account's farming tick offset, timers use it and are exact; otherwise they can be a few minutes out.
- Bird houses are tracked like Time Tracking does: the plugin remembers when you filled each one (saved to your RuneLite profile), so it can tell a bird house that's still filling from a full one you need to collect. A bird house that was already full the first time the plugin sees it is shown as Ready.
- The walking paths are worked out from the Shortest Path plugin's collision map (BSD 2-Clause; its licence is in `paths-LICENSE.txt` next to them).
- The plugin only reads your inventory, equipment, bank and seed vault. It never performs any actions for you.

## License

BSD 2-Clause. See [LICENSE](LICENSE).
