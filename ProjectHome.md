## FXL GUI API ##

| **This repository is not active anymore. Please visit** https://bitbucket.org/Dangelmayr/fxlgui/ **to access the latest version.** |
|:-----------------------------------------------------------------------------------------------------------------------------------|

This open source project represents a research effort combining fluent interfaces, portability, and humble graphical user interface programming. These principles form the basis of the featured application programming interface.
Examples and a showcase can be found at http://fxl.co/.

Core concept of the API is the notion of the **fluent** interface. All elements of the GUI API are written in a fluent interface style. For example, to add a blue, bold label in Arial to a panel we state: `panel.add().label().text("Label").font().family().arial().weight().bold().color().blue();`

**Portability** allows us to provide implementations for different platforms. Currently, we feature Swing as well as GWT. Thus, a corresponding GUI application runs as a desktop application and - without changes - in the browser. In addition, we are currently examining mobile platforms, in particular Android.

Our primary goal is to allow for the rapid development of portable applications. One benefit of platform-independence is, for example, that we can develop and test a web application using Swing. Amongst others, this can significantly reduce idle time during compilation or deployment cycles.

Furthermore, a basic design principle of the API is conciseness. All GUI components are designed **humble** or lightweight. They represent direct, minimalistic interfaces to the elements of the encapsulated GUI platform. In general, higher-level abstractions are neglected on purpose. Presentation frameworks or architectural patterns like MVC can be realized on top of the GUI platform layer, but they are not enforced by it.

The GUI API consists solely of interfaces. Using dependency injection, we can replace the current implementation with a mock implementation. This allows for **exhaustive testing of client code** (e.g. the actual application). Unit tests can verify API calls and emulate appropriate user input - thus further reducing the need for manual testing.

**FXL GUI API is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. FXL GUI API is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.**