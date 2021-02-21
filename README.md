# LazyText Editor ![Lazy_Text_Editor_Logo](github_res/icon.png)

Lightweight text editor in the making written in Java using Swing

![Lazy_Text_Editor_demo](github_res/Capture.png)

# Current Features Available
* Cut/Copy/Paste
* Undo/Redo
* Keyboard Shortcuts
* Line Numbers
* XML Theme System
  * Current Themes available:
  * Light
  * Dark
  * Monokai
* Ability remember last set theme
* File Reading/Writing
  * Close currently loaded files
  * Reload newly 'Saved As' files
* Find Text
  * Regex support
  * Highlight Found Text
  * Walk throughout highlighted text
* Find and Replace Text
* Status bar
  * Display current line and column number
  * Display File Extension
  * Display File Encoding

# Contributing

* This repo uses the *git branching model** for contributions
    * Instructions:
    * Create a branch
    * Make some changes
    * Open PR, highlighting changes to be merged.
    * Wait for changes to be approved.
    * Merge

* This ensures the project's codebase is kept consistent & easy to understand

1. Use descriptive variable & method names

2. Add comment at the end of a method/class depicting its end point
   on a new line as such:
   
   `// End of methodName/className`

# Building From Source
1. Import project into Intellij
2. Compile via **Maven**

# Building Custom Themes

* First use `new_theme_template.xml` as a base to create a theme
  then fill in values with desired **hexadecimal** values all except 
  `<baseFont>` and `<customcomponents>` are required

  ![theme_template](github_res/xml_template.png)

* Second, place **new_theme.xml** file in within the **themes/** directory in **res/**
  
  ![placing_new_theme_in_directory](github_res/adding_new_theme.png)

* Lastly select your newly added theme from the `Themes` menu (via `CTRL+K`) and you're done

  ![selecting_new_theme](github_res/selecting_newly_added_theme.png)


## Applying a Theme to New Components

* To theme newly added components you will have to include it within the xml file
  under `<customcomponents>` and make custom methods that theme all components you wish to include and use the 
  `static` method, `Theme.getCustomComponentParsedThemeValues("nameOfComponent");` & `getColorValue("hexadecimalString")`.
  Which returns a `String[]` of values specified by you for your component and convert any hexadecimal string
  into it's corresponding color.

  ![theme_new_components](github_res/theming_new_components.png)