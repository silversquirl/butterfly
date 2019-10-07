# Butterfly

Butterfly is a Minecraft mod for 1.14.4 Fabric that reverts certain changes to behaviour, improving the quality of technical play.

## Usage

Butterfly is alpha quality software.
If you're feeling brave and want to try it out, you can build it using the command `./gradlew remapJar` - the resulting JAR can be found in `build/libs/butterfly-1.0.0.jar`.
Be warned, however, that there's no guarantee it won't destroy your world and/or eat your laundry.

## Bugs

Below is a list of bugs that have been un-fixed and changes that have been reverted:

- Water reduces more light than other transparent blocks
- Dragon eggs break blocks when dropped in lazy chunks
- A 5x5 grid of loaded chunks load the centre chunk for entity processing
- Hoppers attempting to deposit items across chunk borders load the chunk they point into

## Contributing

Whether or not you have experience with Minecraft modding or just coding in general, any support is greatly appreciated. Here's how you can help out:

- Work on an un-fix or reversion from our [issues] page
- Suggest un-fixes or reversions on the relevant [issue][the-big-issue]
- Test current un-fixes and reversions to ensure they are faithful to the original behaviour

[issues]: https://github.com/vktec/butterfly/issues
[the-big-issue]: https://github.com/vktec/butterfly/issues/4
