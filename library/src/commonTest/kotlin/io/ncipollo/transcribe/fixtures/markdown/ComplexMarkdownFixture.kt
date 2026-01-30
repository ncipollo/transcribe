package io.ncipollo.transcribe.fixtures.markdown

object ComplexMarkdownFixture {
    const val COMPLEX_MARKDOWN = """This content exists outside of a section.

# Section 1
This is normal paragraph text. Nothing interesting here.

# Section 2
The following are bullets:
- Bullet 1
- Bullet 2
- Bullet 3
    - Sub-bullet 1
    - Sub-bullet 2

And here we have numeric bullets:
1. The number one.
2. The number two
    1. Sub-bullet

And here's a task list:
- [ ] Action 1
- [ ] Action 2
- [x] Checked
    - [ ] Sub

# Section 3
| **Column 1** | **Column 2** | **Column 3** |
| --- | --- | --- |
| Row 1, Column 1 | Row 1, Column 2 | Row 1, Column 3 |
| Row 2, Column 1 | Row 2, Column 2 | Row 2, Column 3 |

# Section 4
![Test](images/att1_test_image.png)

# Section 5
This will have subsections.

## Sub 1

### H3 Section
Wow, I'm nested!

# Sub 2
Subsection 2.

# Section 6
**Lots** of *random* elements.
```swift
let thing = MyThing()
use(thing)
```

<details>
<summary>Collapse</summary>
- Content which is kinda hidden.
</details>
- 🔵 STATUS 1
- 🔴 STATUS 2 text after

# Section 7
![Untitled Diagram-1766526257160.drawio](images/untitled_diagram-1766526257160.drawio.png)

# Section 8
*This text has* `lots` *of* ***marks*** *applied to it.*

# Section 9
> **This** and *that.*
> Next line.
> - Bullet 1
> - Bullet 2
# *Section* 10 and **Bold**
"""
}
