
# Marvelist

Studying Marvels API for developers using:
- Kotlin
- RXJava (I could have used Kotlin Coroutines for this POC but I decided to study something new :) )
- Retrofit
- OkHTTPClient
- Espresso

### Architecture

For being just a quick study, I decided to create a small and clean architecture for the project, which consists:

1. Controller: Binds data and UI.
	Activity
	Adapter
	Fragment

2. Model: All data models. Notice that each one contains its own container and wrapper.
	Character
	Comic

3. Network: Rest config for Marvel API access and all interesting services for us as interfaces.

### Features

- List all Marvel characters and comics;
- Search for a specific one;
- Get to know more about it on Marvel's web site;
- Available for screen readers;
- Respecting WCAG rules.
