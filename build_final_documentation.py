import os
import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import parse_xml
from docx.oxml.ns import nsdecls

def build_final_documentation():
    doc = docx.Document()

    # Set page margins: 1 inch on all sides (Standard Academic Specification)
    for section in doc.sections:
        section.top_margin = Inches(1.0)
        section.bottom_margin = Inches(1.0)
        section.left_margin = Inches(1.0)
        section.right_margin = Inches(1.0)
        
        # Configure Header & Footer
        footer = section.footer
        p_ft = footer.paragraphs[0]
        p_ft.alignment = WD_ALIGN_PARAGRAPH.RIGHT
        r_ft = p_ft.add_run("DS Studio — MCA Advanced Data Structures Project Documentation")
        r_ft.font.name = 'Times New Roman'
        r_ft.font.size = Pt(9.5)
        r_ft.italic = True
        r_ft.font.color.rgb = RGBColor(120, 120, 120)

    # Configure Normal Style (Times New Roman, 12pt, 1.15 line spacing, 6pt after)
    style_normal = doc.styles['Normal']
    font_normal = style_normal.font
    font_normal.name = 'Times New Roman'
    font_normal.size = Pt(12)
    font_normal.color.rgb = RGBColor(20, 20, 20)
    style_normal.paragraph_format.line_spacing = 1.15
    style_normal.paragraph_format.space_after = Pt(6)

    def add_title(text):
        p = doc.add_paragraph()
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p.paragraph_format.space_before = Pt(24)
        p.paragraph_format.space_after = Pt(12)
        run = p.add_run(text)
        run.font.name = 'Times New Roman'
        run.font.size = Pt(20)
        run.bold = True
        run.font.color.rgb = RGBColor(15, 23, 42)
        return p

    def add_subtitle(text):
        p = doc.add_paragraph()
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p.paragraph_format.space_after = Pt(20)
        run = p.add_run(text)
        run.font.name = 'Times New Roman'
        run.font.size = Pt(13)
        run.italic = True
        run.font.color.rgb = RGBColor(71, 85, 105)
        return p

    def add_h1(text):
        p = doc.add_paragraph()
        p.paragraph_format.space_before = Pt(18)
        p.paragraph_format.space_after = Pt(6)
        p.paragraph_format.keep_with_next = True
        run = p.add_run(text)
        run.font.name = 'Times New Roman'
        run.font.size = Pt(14)
        run.bold = True
        run.font.color.rgb = RGBColor(15, 23, 42)
        return p

    def add_h2(text):
        p = doc.add_paragraph()
        p.paragraph_format.space_before = Pt(13)
        p.paragraph_format.space_after = Pt(4)
        p.paragraph_format.keep_with_next = True
        run = p.add_run(text)
        run.font.name = 'Times New Roman'
        run.font.size = Pt(14)
        run.bold = True
        run.font.color.rgb = RGBColor(30, 41, 59)
        return p

    def add_p(text):
        p = doc.add_paragraph()
        p.paragraph_format.line_spacing = 1.15
        p.paragraph_format.space_after = Pt(6)
        run = p.add_run(text)
        run.font.name = 'Times New Roman'
        run.font.size = Pt(12)
        run.font.color.rgb = RGBColor(20, 20, 20)
        return p

    def add_bullet(text, bold_prefix=""):
        p = doc.add_paragraph(style='List Bullet')
        p.paragraph_format.line_spacing = 1.15
        p.paragraph_format.space_after = Pt(4)
        if bold_prefix:
            r_bold = p.add_run(bold_prefix)
            r_bold.font.name = 'Times New Roman'
            r_bold.font.size = Pt(12)
            r_bold.bold = True
            r_bold.font.color.rgb = RGBColor(15, 23, 42)
        run = p.add_run(text)
        run.font.name = 'Times New Roman'
        run.font.size = Pt(12)
        run.font.color.rgb = RGBColor(20, 20, 20)
        return p

    def add_image_figure(img_path, caption_text, width=Inches(6.2)):
        if os.path.exists(img_path):
            p_img = doc.add_paragraph()
            p_img.alignment = WD_ALIGN_PARAGRAPH.CENTER
            p_img.paragraph_format.space_before = Pt(10)
            p_img.paragraph_format.space_after = Pt(4)
            p_img.add_run().add_picture(img_path, width=width)

            p_cap = doc.add_paragraph()
            p_cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
            p_cap.paragraph_format.space_after = Pt(14)
            r = p_cap.add_run(caption_text)
            r.font.name = 'Times New Roman'
            r.font.size = Pt(11)
            r.italic = True
            r.bold = True
            r.font.color.rgb = RGBColor(51, 65, 85)

    def set_cell_margins(cell, top=100, bottom=100, left=140, right=140):
        tcPr = cell._tc.get_or_add_tcPr()
        tcMar = parse_xml(f'<w:tcMar {nsdecls("w")}><w:top w:w="{top}" w:type="dxa"/><w:bottom w:w="{bottom}" w:type="dxa"/><w:left w:w="{left}" w:type="dxa"/><w:right w:w="{right}" w:type="dxa"/></w:tcMar>')
        tcPr.append(tcMar)

    def set_cell_shading(cell, color_hex):
        shading = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{color_hex}"/>')
        cell._tc.get_or_add_tcPr().append(shading)

    # ==================== COVER PAGE ====================
    add_title("DS STUDIO — INTERACTIVE DATA STRUCTURES PLATFORM")
    add_subtitle("Master of Computer Applications (MCA) Mini-Project Documentation\nCourse: Advanced Data Structures & Algorithm Design (ADS)\nTechnology Stack: Java 21 LTS, JavaFX 21, SQLite 3.45 via JDBC, Maven")

    p_box = doc.add_paragraph()
    p_box.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_box.paragraph_format.space_before = Pt(18)
    p_box.paragraph_format.space_after = Pt(36)
    r_box = p_box.add_run(
        "----------------------------------------------------------------------------------------------------\n"
        "Project Type: Interactive Educational Software & Execution-Trace Visualization Suite\n"
        "Architecture Pattern: Decoupled Execution-Trace Micro-Snapshot Pattern\n"
        "Storage Engine: Zero-Configuration Embedded SQLite Local Persistence Engine\n"
        "Academic Year: 2026\n"
        "----------------------------------------------------------------------------------------------------"
    )
    r_box.font.name = 'Times New Roman'
    r_box.font.size = Pt(11)
    r_box.font.color.rgb = RGBColor(71, 85, 105)

    doc.add_page_break()

    # ==================== INDEX PAGE ====================
    p_idx_title = doc.add_paragraph()
    p_idx_title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_idx_title.paragraph_format.space_after = Pt(18)
    r_idx_title = p_idx_title.add_run("INDEX")
    r_idx_title.font.name = 'Times New Roman'
    r_idx_title.font.size = Pt(16)
    r_idx_title.bold = True

    index_data = [
        ("1", "Introduction\n1.1 Problem definition\n1.2 Objectives of Project", "1"),
        ("2", "System Study\n2.1 Existing System\n2.2 Disadvantages of Existing system\n2.3 Proposed System", "3"),
        ("3", "System Design\nFlow chart & Diagrams\n  • Figure 3.1: StarUML System Architecture (Component & Package)\n  • Figure 3.2: StarUML Operational System Flowchart & Activity\n  • Figure 3.3: StarUML Use Case Diagram (Actor & Capabilities)\n  • Figure 3.4: StarUML SQLite Relational Entity-Relationship (ERD)", "6"),
        ("4", "Screen Layout / Screen shots\n4.1 Dashboard View\n4.2 Interactive Visualizer Lab View\n4.3 Synchronized Java Code Lab View\n4.4 Algorithms Sorting Lab View\n4.5 DS Quest Mission Control\n4.6 Examination Quiz Centre\n4.7 Mastery Progress & Analytics View", "11"),
        ("5", "Conclusion", "16"),
        ("6", "Future Enhancements", "17"),
        ("7", "References", "18")
    ]

    table = doc.add_table(rows=1, cols=3)
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    table.autofit = False

    # Header Row
    hdr_cells = table.rows[0].cells
    hdr_cells[0].width = Inches(1.0)
    hdr_cells[1].width = Inches(4.5)
    hdr_cells[2].width = Inches(1.0)

    for i, h in enumerate(["Sr. No.", "Contents", "Page No."]):
        p = hdr_cells[i].paragraphs[0]
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        r = p.add_run(h)
        r.font.name = 'Times New Roman'
        r.font.size = Pt(14)
        r.bold = True
        set_cell_shading(hdr_cells[i], "F1F5F9")
        set_cell_margins(hdr_cells[i], top=120, bottom=120, left=120, right=120)

    for sr, content, pg in index_data:
        row_cells = table.add_row().cells
        row_cells[0].width = Inches(1.0)
        row_cells[1].width = Inches(4.5)
        row_cells[2].width = Inches(1.0)

        p0 = row_cells[0].paragraphs[0]
        p0.alignment = WD_ALIGN_PARAGRAPH.CENTER
        r0 = p0.add_run(sr)
        r0.font.name = 'Times New Roman'
        r0.font.size = Pt(12)

        p1 = row_cells[1].paragraphs[0]
        r1 = p1.add_run(content)
        r1.font.name = 'Times New Roman'
        r1.font.size = Pt(12)

        p2 = row_cells[2].paragraphs[0]
        p2.alignment = WD_ALIGN_PARAGRAPH.CENTER
        r2 = p2.add_run(pg)
        r2.font.name = 'Times New Roman'
        r2.font.size = Pt(12)

        for cell in row_cells:
            set_cell_margins(cell, top=100, bottom=100, left=120, right=120)

    tblPr = table._tbl.tblPr
    borders = parse_xml(f'<w:tblBorders {nsdecls("w")}><w:top w:val="single" w:sz="6" w:space="0" w:color="000000"/><w:bottom w:val="single" w:sz="6" w:space="0" w:color="000000"/><w:left w:val="single" w:sz="6" w:space="0" w:color="000000"/><w:right w:val="single" w:sz="6" w:space="0" w:color="000000"/><w:insideH w:val="single" w:sz="4" w:space="0" w:color="E2E8F0"/><w:insideV w:val="single" w:sz="4" w:space="0" w:color="E2E8F0"/></w:tblBorders>')
    tblPr.append(borders)

    doc.add_page_break()

    # ==================== 1. INTRODUCTION ====================
    add_h1("1. Introduction")
    add_p("Data Structures and Algorithm Design represent the core cognitive pillar of Computer Science, Software Engineering, and Modern Computing Systems. Every software application—from database query planners and operating system task schedulers to web browsers and graphics rendering pipelines—fundamentally relies on specialized data organizations to store, access, modify, and navigate information with provable asymptotic efficiency.")
    add_p("DS Studio is an advanced interactive educational desktop platform engineered to revolutionize how undergraduate and postgraduate students comprehend, test, and master data structures. Rather than depending exclusively on static blackboard illustrations or printed textbooks, DS Studio bridges the gap between abstract algorithmic concepts and physical execution through real-time state visualization synchronized line-by-line with standard Java code.")

    add_h2("1.1 Problem Definition")
    add_p("Data structures are inherently dynamic, spatial, and pointer-driven abstractions. When operations such as element insertion, array index shifting, circular queue modulo wrap-around, or linked list node reconnection execute, contiguous and non-contiguous memory allocations undergo instantaneous mutation.")
    add_p("In conventional educational environments, students encounter severe learning hurdles:")
    add_bullet("Students struggle to visualize how pointers (HEAD, TAIL, TOP, FRONT, REAR) decouple, shift, and reconnect during memory operations.", "Invisible Memory Mutations: ")
    add_bullet("Standard textbooks present final algorithms. Students cannot observe intermediate micro-steps, such as how elements shift one index to the right during array insertion.", "Lack of Incremental Micro-Traces: ")
    add_bullet("Students frequently memorize code syntax for examination purposes without understanding critical boundary conditions such as stack underflow, stack overflow, or false queue fullness.", "Rote Syntax Memorization: ")
    add_bullet("Learners typically read theoretical concepts from books, inspect fragmented code online, and practice tests on separate platforms with zero integrated tracking.", "Disjointed Learning Tools: ")

    add_h2("1.2 Objectives of Project")
    add_p("The primary objective of DS Studio is to design, develop, and deliver an integrated, native Java desktop application that combines theoretical curriculum study, dynamic visual animation, verified Java code inspection, and gamified examination testing into a single self-contained educational suite.")
    add_p("The targeted project objectives include:")
    add_bullet("Develop authentic implementations of core linear data structures (Arrays, Stacks, Queues, Circular Queues, Singly Linked Lists, Doubly Linked Lists, Circular Linked Lists) and fundamental sorting algorithms in pure Java.", "1. Authentic Algorithmic Realization: ")
    add_bullet("Construct a decoupled execution-trace engine that records immutable state snapshots during each algorithmic micro-step (element shifts, pointer changes, code lines, Big-O bounds).", "2. Execution-Trace Snapshot Engine: ")
    add_bullet("Provide an interactive animation canvas equipped with comprehensive playback controls (Play, Pause, Step Next, Step Prev, Reset, Speed Adjustment).", "3. Interactive Playback Controls: ")
    add_bullet("Synchronize active Java code lines with graphical state transitions in real time to bridge theoretical understanding with practical programming.", "4. Synchronized Java Code Lab: ")
    add_bullet("Incorporate gamified knowledge assessment through 'DS Quest' (Concept Quests, Output Prediction, Debug Quests, Dry Runs, and Big-O Complexity challenges).", "5. Gamified Interactive Testing: ")
    add_bullet("Simulate university examination conditions via a timed Quiz Centre featuring dynamic timers, interactive question palettes, and performance grading.", "6. Examination Simulator: ")
    add_bullet("Embed a zero-configuration local SQLite database to permanently persist student learning streaks, XP points, unlocked badges, and quiz attempts without requiring external servers or internet connectivity.", "7. Zero-Dependency Offline Persistence: ")

    doc.add_page_break()

    # ==================== 2. SYSTEM STUDY ====================
    add_h1("2. System Study")
    add_p("A thorough investigation of existing software solutions and conventional computer science pedagogy was undertaken prior to formulating the technical architecture of DS Studio.")

    add_h2("2.1 Existing System")
    add_p("Students currently acquire Data Structures knowledge through three prevailing mediums:")
    add_bullet("University textbooks and lecture slides (e.g., Cormen et al., Horowitz & Sahni) containing static asymptotic proofs, mathematical notations, and two-dimensional diagrams.", "Printed Academic Literature: ")
    add_bullet("Online coding platforms (e.g., LeetCode, HackerRank, CodeChef) that evaluate submitted source code strictly against black-box test cases without illustrating intermediate memory mutations.", "Automated Online Judges: ")
    add_bullet("Independent web applets or browser-based animation websites (e.g., VisuAlgo, University of San Francisco CS Tool) that demonstrate algorithmic transitions using JavaScript.", "Third-Party Web Visualizers: ")

    add_h2("2.2 Disadvantages of Existing System")
    add_p("A rigorous analysis reveals several critical deficiencies in the existing learning ecosystem:")
    add_bullet("Static textbook diagrams cannot demonstrate the live movement of pointers or dynamic element displacement during runtime execution.", "Absence of Dynamic Runtime Demonstration: ")
    add_bullet("Competitive programming judges provide boolean pass/fail verdicts or output comparisons. When a student's pointer disconnects or leaks memory, the judge cannot illustrate where the failure occurred.", "Zero Internal State Visibility: ")
    add_bullet("Existing web visualizers depend on continuous internet access, frequently break due to browser deprecations, and do not connect graphical changes to real Java syntax.", "Fragile Internet Dependency & Code Disconnection: ")
    add_bullet("Existing tools lack integrated local persistence. When the browser tab closes, student progress, practice records, and quiz performance metrics are entirely lost.", "Lack of Learner Analytics Persistence: ")

    add_h2("2.3 Proposed System")
    add_p("The proposed system, DS Studio, is an autonomous desktop environment built exclusively in Java 21 LTS and JavaFX 21, providing full offline independence and deep conceptual visibility.")
    add_p("Distinguishing architectural advantages of the proposed system include:")
    add_bullet("The visualizer does not simulate artificial animations; genuine Java algorithms execute internal mutations and emit immutable state snapshots.", "Authentic Execution Architecture: ")
    add_bullet("Complete separation of concerns between core algorithm logic, trace generation, UI canvas rendering, and relational persistence.", "Modular Object-Oriented Decoupling: ")
    add_bullet("Students can inject custom numbers and arbitrary target indices to observe how array boundaries, queue modulo wrap-around, and linked list traversals respond in real time.", "Arbitrary Custom Parameter Injection: ")
    add_bullet("Dual-pane interface displaying graphical memory nodes alongside active lines of real Java code.", "Real-Time Visual & Code Synchronization: ")
    add_bullet("Integrated local SQLite database storing student profiles, XP points, streaks, badges, and detailed quiz logs in the user's home directory.", "Local Embedded Relational Storage: ")

    doc.add_page_break()

    # ==================== 3. SYSTEM DESIGN ====================
    add_h1("3. System Design (Flow chart & Diagrams)")
    add_p("The system design of DS Studio adheres to international UML 2.5 standards and modern software engineering principles, emphasizing high cohesion, loose coupling, and robust architectural modularity. The diagrams below were formulated using StarUML design conventions.")

    add_h2("3.1 System Architecture Diagram")
    add_p("DS Studio employs an Execution-Trace Snapshot Architectural Pattern, decomposing the system into four autonomous layers as illustrated in Figure 3.1:")
    add_bullet("Presentation UI Layer (JavaFX 21 Modular Client): Manages the 13 modular views, interactive canvas nodes, synchronized code panels, and playback control events.", "1. ")
    add_bullet("Execution-Trace Snapshot Engine: Captures immutable OperationTrace and TraceStep objects containing pointer positions, highlighted element indices, active code line numbers, and asymptotic complexity bounds.", "2. ")
    add_bullet("Core Algorithmic Layer: Pure Java implementations of data structures (ArrayStructure, StackStructure, QueueStructure, CircularQueueStructure, SinglyLinkedListStructure, DoublyLinkedListStructure, CircularLinkedListStructure, SortingAlgorithms) possessing zero UI dependencies.", "3. ")
    add_bullet("Persistence Layer: Zero-configuration SQLite database engine accessed through JDBC, managing user identity, XP rewards, achievements, and attempt histories.", "4. ")

    add_image_figure(r"d:\ads\diagrams\staruml_architecture.png", "Figure 3.1: DS Studio Component & Package Architecture (StarUML Style)", width=Inches(6.2))

    add_h2("3.2 Operational System Flowchart & Activity Workflow")
    add_p("Figure 3.2 illustrates the operational activity flowchart of DS Studio, detailing decision branching, validation checks, algorithm execution, trace dispatch, and persistence events.")

    add_image_figure(r"d:\ads\diagrams\staruml_flowchart.png", "Figure 3.2: DS Studio Operational Activity Flowchart (StarUML Style)", width=Inches(4.8))

    add_h2("3.3 Use Case Diagram")
    add_p("Figure 3.3 models the interactions between the primary actor (Student / MCA Learner) and the functional use cases encapsulated within the DS Studio system boundary.")

    add_image_figure(r"d:\ads\diagrams\staruml_usecase.png", "Figure 3.3: DS Studio Use Case Diagram (StarUML Style)", width=Inches(6.0))

    add_h2("3.4 Database Design & Entity-Relationship (E-R) Diagram")
    add_p("The local persistence layer utilizes an embedded SQLite relational database (ds_studio.db) stored in ~/.dsstudio/. Figure 3.4 illustrates the formal Entity-Relationship schema connecting the PROFILE, QUIZ_ATTEMPTS, ACHIEVEMENTS, and LESSON_PROGRESS tables.")

    add_image_figure(r"d:\ads\diagrams\staruml_erd.png", "Figure 3.4: SQLite Database Entity-Relationship Schema (StarUML Style)", width=Inches(6.2))

    doc.add_page_break()

    # ==================== 4. SCREEN LAYOUT / SCREEN SHOTS ====================
    add_h1("4. Screen Layout / Screen shots")
    add_p("DS Studio incorporates an ergonomic, dark-themed user interface structured into 13 dedicated sections accessible via a permanent sidebar navigation bar.")

    add_h2("4.1 Dashboard View")
    add_p("The Dashboard serves as the central command cockpit. It features:")
    add_bullet("Displays student greeting, motivational tagline, and an immediate 'Launch Visualizer Lab' call-to-action button.", "Welcome Hero Banner: ")
    add_bullet("Four synchronized metric cards displaying Total XP Earned, Daily Streak (in days), Active Linear Modules (7 modules), and Available Sorts/Searches (5 algorithms).", "Real-Time Metric Cards: ")
    add_bullet("Presents a rotating conceptual challenge with XP rewards.", "Daily Challenge Card: ")
    add_bullet("One-click shortcut chips enabling instantaneous launch of specific visualizer structures.", "Quick Launchpad: ")

    add_h2("4.2 Interactive Visualizer Lab View")
    add_p("The Visualizer Lab is the centerpiece of the educational suite. It contains:")
    add_bullet("Data structure selector, operation selector, parameter value field, target index field, and the 'Execute' trigger button.", "Input & Operation Toolbar: ")
    add_bullet("Play/Pause toggle button, Step Next, Step Prev, Reset, and a 0.5x / 1.0x / 2.0x playback speed slider.", "Playback Animation Controls: ")
    add_bullet("Interactive centered canvas dynamically rendering array cells, stack columns, queue slots, or linked list node blocks with pointer arrows (HEAD, TAIL, TOP, FRONT, REAR).", "Visualizer Canvas: ")
    add_bullet("Active time complexity badge, auxiliary space complexity badge, step counter (e.g., Step 3 / 8), and detailed textual explanations.", "Step Status & Metadata: ")
    add_bullet("Dedicated code panel highlighting active lines of real Java code corresponding to the current visualizer state.", "Synchronized Code Panel: ")

    add_h2("4.3 Java Code Lab View")
    add_p("The Code Lab provides verified, standard Java implementations designed for academic laboratory examinations. It includes a module selector, formatted code container with a title bar, and a 'Copy Java Code' button integrated with the operating system clipboard.")

    add_h2("4.4 DS Quest & Examination Quiz Centre Views")
    add_p("DS Quest delivers challenge-based gamification across five categories (Concept, Output Prediction, Debug, Dry Run, and Complexity Quests). The Quiz Centre provides a 5-minute timed examination environment featuring an urgent color-shifting countdown timer, an interactive question palette grid, and automatic score logging to SQLite.")

    add_h2("4.5 Mastery Progress & Analytics View")
    add_p("The Progress View presents student level progression, earned versus locked badge milestones, and historical quiz attempts queried directly from the local SQLite database.")

    doc.add_page_break()

    # ==================== 5. CONCLUSION ====================
    add_h1("5. Conclusion")
    add_p("DS Studio successfully achieves its overarching goal of delivering an authentic, visually rich, and pedagogically robust desktop learning studio for Advanced Data Structures.")
    add_p("By strictly implementing the entire solution in pure Java and JavaFX, the project confirms that complex computer science concepts can be taught interactively without relying on heavy web frameworks, remote servers, or external internet connectivity. The application completely decouples algorithmic execution from presentation rendering through the Execution-Trace Snapshot Pattern, guaranteeing that visual animations accurately mirror real Java memory mutations.")
    add_p("The integration of visual trace animations, synchronized real Java source code, gamified DS Quest challenges, university-style timed examination simulations, and embedded SQLite persistence creates an ideal self-study and demonstration environment for MCA students preparing for laboratory practicals and technical viva examinations.")

    doc.add_page_break()

    # ==================== 6. FUTURE ENHANCEMENTS ====================
    add_h1("6. Future Enhancements")
    add_p("While DS Studio completely implements foundational Linear Data Structures and elementary Sorting Algorithms, several prospective enhancements are identified for future versions:")
    add_bullet("Extending the visualization canvas to render self-balancing trees, including Binary Search Trees (BST), AVL Trees, Red-Black Trees, and B-Trees with dynamic tree rotation animations.", "1. Non-Linear Tree Visualizations: ")
    add_bullet("Incorporating graphical graph representations (Adjacency Matrix and Adjacency List) supporting Breadth-First Search (BFS), Depth-First Search (DFS), Dijkstra's Shortest Path, and Minimum Spanning Trees (Prim's and Kruskal's).", "2. Graph Algorithms & Shortest Path Traversals: ")
    add_bullet("Implementing visual divide-and-conquer animations for Merge Sort and Quick Sort, including visual recursive call-stack traces.", "3. Advanced Divide-and-Conquer Sorting: ")
    add_bullet("Embedding an in-app Java Compiler (via javax.tools.JavaCompiler) allowing students to enter custom algorithmic methods and watch their custom logic execute on the visualizer canvas.", "4. In-App Interactive Code Compiler: ")
    add_bullet("Adding automated PDF certificate and performance report generation so students can export their quiz results and learning transcripts for formal university submission.", "5. Academic Performance Report Export: ")

    doc.add_page_break()

    # ==================== 7. REFERENCES ====================
    add_h1("7. References")
    add_p("1. Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2022). Introduction to Algorithms (4th ed.). MIT Press.")
    add_p("2. Sedgewick, R., & Wayne, K. (2011). Algorithms (4th ed.). Addison-Wesley Professional.")
    add_p("3. Knuth, D. E. (1997). The Art of Computer Programming, Volume 1: Fundamental Algorithms (3rd ed.). Addison-Wesley.")
    add_p("4. Bloch, J. (2018). Effective Java (3rd ed.). Addison-Wesley Professional.")
    add_p("5. Horstmann, C. S. (2022). Core Java Volume I – Fundamentals (12th ed.). Prentice Hall.")
    add_p("6. Oracle Corporation. (2024). Java Platform, Standard Edition & JavaFX 21 Developer Guide. Oracle Technology Network.")
    add_p("7. SQLite Development Team. (2024). SQLite Architecture, Query Processing and JDBC Integration. https://www.sqlite.org")
    add_p("8. IEEE Computer Society. (2020). Guide to the Software Engineering Body of Knowledge (SWEBOK Guide v3.0). IEEE.")

    output_path = r"d:\ads\DS_Studio_Final_Project_Documentation.docx"
    doc.save(output_path)
    print(f"Final publication-grade documentation successfully created at {output_path}")

    # Also try to update other docx files if not locked
    for alt_path in [r"d:\ads\DS_Studio_StarUML_Project_Documentation.docx", r"d:\ads\DS_Studio_MCA_Project_Documentation.docx", r"d:\ads\DS_Studio_Project_Documentation.docx"]:
        try:
            doc.save(alt_path)
            print(f"Also updated {alt_path}")
        except Exception:
            pass

if __name__ == "__main__":
    build_final_documentation()
