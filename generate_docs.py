import os
import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml import parse_xml
from docx.oxml.ns import nsdecls

def create_document():
    doc = docx.Document()

    # Set page margins (1 inch on all sides)
    for section in doc.sections:
        section.top_margin = Inches(1)
        section.bottom_margin = Inches(1)
        section.left_margin = Inches(1)
        section.right_margin = Inches(1)

    # Configure Normal Style (Times New Roman, 12pt, 1.15 line spacing, 6pt space after)
    style_normal = doc.styles['Normal']
    font_normal = style_normal.font
    font_normal.name = 'Times New Roman'
    font_normal.size = Pt(12)
    font_normal.color.rgb = RGBColor(0, 0, 0)
    style_normal.paragraph_format.line_spacing = 1.15
    style_normal.paragraph_format.space_after = Pt(6)

    def add_title(text):
        p = doc.add_paragraph()
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p.paragraph_format.space_before = Pt(18)
        p.paragraph_format.space_after = Pt(12)
        run = p.add_run(text)
        run.font.name = 'Times New Roman'
        run.font.size = Pt(18)
        run.bold = True
        run.font.color.rgb = RGBColor(0, 0, 0)
        return p

    def add_subtitle(text):
        p = doc.add_paragraph()
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p.paragraph_format.space_after = Pt(24)
        run = p.add_run(text)
        run.font.name = 'Times New Roman'
        run.font.size = Pt(14)
        run.italic = True
        run.font.color.rgb = RGBColor(60, 60, 60)
        return p

    def add_h1(text):
        p = doc.add_paragraph()
        p.paragraph_format.space_before = Pt(16)
        p.paragraph_format.space_after = Pt(6)
        p.paragraph_format.keep_with_next = True
        run = p.add_run(text)
        run.font.name = 'Times New Roman'
        run.font.size = Pt(14)
        run.bold = True
        run.font.color.rgb = RGBColor(0, 0, 0)
        return p

    def add_h2(text):
        p = doc.add_paragraph()
        p.paragraph_format.space_before = Pt(12)
        p.paragraph_format.space_after = Pt(4)
        p.paragraph_format.keep_with_next = True
        run = p.add_run(text)
        run.font.name = 'Times New Roman'
        run.font.size = Pt(14)
        run.bold = True
        run.font.color.rgb = RGBColor(0, 0, 0)
        return p

    def add_p(text):
        p = doc.add_paragraph()
        p.paragraph_format.line_spacing = 1.15
        p.paragraph_format.space_after = Pt(6)
        run = p.add_run(text)
        run.font.name = 'Times New Roman'
        run.font.size = Pt(12)
        run.font.color.rgb = RGBColor(0, 0, 0)
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
            r_bold.font.color.rgb = RGBColor(0, 0, 0)
        run = p.add_run(text)
        run.font.name = 'Times New Roman'
        run.font.size = Pt(12)
        run.font.color.rgb = RGBColor(0, 0, 0)
        return p

    def add_image_figure(img_path, caption_text, width=Inches(6.0)):
        if os.path.exists(img_path):
            p_img = doc.add_paragraph()
            p_img.alignment = WD_ALIGN_PARAGRAPH.CENTER
            p_img.paragraph_format.space_before = Pt(8)
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
            r.font.color.rgb = RGBColor(40, 40, 40)

    def set_cell_margins(cell, top=100, bottom=100, left=150, right=150):
        tcPr = cell._tc.get_or_add_tcPr()
        tcMar = parse_xml(f'<w:tcMar {nsdecls("w")}><w:top w:w="{top}" w:type="dxa"/><w:bottom w:w="{bottom}" w:type="dxa"/><w:left w:w="{left}" w:type="dxa"/><w:right w:w="{right}" w:type="dxa"/></w:tcMar>')
        tcPr.append(tcMar)

    def set_cell_shading(cell, color_hex):
        shading = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{color_hex}"/>')
        cell._tc.get_or_add_tcPr().append(shading)

    # ==================== COVER / TITLE ====================
    add_title("DS STUDIO")
    add_subtitle("Interactive Advanced Data Structures Learning & Visualization Platform\nMCA Mini-Project Documentation")
    
    meta_p = doc.add_paragraph()
    meta_p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    meta_p.paragraph_format.space_after = Pt(36)
    r_meta = meta_p.add_run("Course: MCA (Master of Computer Applications)\nSubject: Advanced Data Structures (ADS)\nPrimary Technology: Java 21, JavaFX 21, SQLite 3.45\nAcademic Year: 2026")
    r_meta.font.name = 'Times New Roman'
    r_meta.font.size = Pt(12)

    doc.add_page_break()

    # ==================== INDEX TABLE ====================
    idx_title = doc.add_paragraph()
    idx_title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    idx_title.paragraph_format.space_after = Pt(18)
    r_idx = idx_title.add_run("INDEX")
    r_idx.font.name = 'Times New Roman'
    r_idx.font.size = Pt(16)
    r_idx.bold = True

    index_data = [
        ("1", "Introduction\n1.1 Problem definition\n1.2 Objectives of Project", "1"),
        ("2", "System Study\n2.1 Existing System\n2.2 Disadvantages of Existing system\n2.3 Proposed System", "3"),
        ("3", "System Design\nFlow chart & Diagrams\n  - Figure 3.1: System Architecture Diagram\n  - Figure 3.2: Operational System Flowchart\n  - Figure 3.3: Use Case Diagram\n  - Figure 3.4: SQLite E-R Database Schema", "5"),
        ("4", "Screen Layout / Screen shots", "9"),
        ("5", "Conclusion", "13"),
        ("6", "Future Enhancements", "14"),
        ("7", "References", "15")
    ]

    table = doc.add_table(rows=1, cols=3)
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    table.autofit = False

    # Header Row
    hdr_cells = table.rows[0].cells
    hdr_cells[0].width = Inches(1.0)
    hdr_cells[1].width = Inches(4.5)
    hdr_cells[2].width = Inches(1.0)

    headers = ["Sr. No.", "Contents", "Page No."]
    for i, h in enumerate(headers):
        p = hdr_cells[i].paragraphs[0]
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        r = p.add_run(h)
        r.font.name = 'Times New Roman'
        r.font.size = Pt(14)
        r.bold = True
        set_cell_shading(hdr_cells[i], "E2E8F0")
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
    borders = parse_xml(f'<w:tblBorders {nsdecls("w")}><w:top w:val="single" w:sz="6" w:space="0" w:color="000000"/><w:bottom w:val="single" w:sz="6" w:space="0" w:color="000000"/><w:left w:val="single" w:sz="6" w:space="0" w:color="000000"/><w:right w:val="single" w:sz="6" w:space="0" w:color="000000"/><w:insideH w:val="single" w:sz="4" w:space="0" w:color="CCCCCC"/><w:insideV w:val="single" w:sz="4" w:space="0" w:color="CCCCCC"/></w:tblBorders>')
    tblPr.append(borders)

    doc.add_page_break()

    # ==================== 1. INTRODUCTION ====================
    add_h1("1. Introduction")
    add_p("Advanced Data Structures (ADS) form the fundamental foundation of computer science, modern software engineering, and algorithmic problem-solving. Every efficient software application relies on carefully selected data structures to store, organize, retrieve, and manipulate data with minimal time and memory consumption.")
    add_p("DS Studio is an interactive, Java-based desktop educational platform created to transform how computer science students study and experience Data Structures. Rather than relying exclusively on static whiteboard drawings or textbook paragraphs, DS Studio provides live, animated visualizations of internal memory state changes synchronized directly with real Java code execution.")

    add_h2("1.1 Problem Definition")
    add_p("Data structures are inherently dynamic and abstract concepts. When operations such as element insertion, array shifting, stack overflow, circular modulo wrapping, or linked list pointer reconnections take place, memory references and pointers change continuously.")
    add_p("However, conventional teaching methods face severe limitations:")
    add_bullet("Students struggle to visualize pointer manipulation during head/tail insertions and node deletions in linked lists.", "Abstract Memory Operations: ")
    add_bullet("Static printed code snippets do not communicate how a loop index variable moves or how elements shift during runtime.", "Lack of Dynamic Trace: ")
    add_bullet("Learners often memorize syntax without understanding runtime edge cases, such as queue boundary overflow or stack underflow.", "Rote Learning: ")
    add_bullet("Students typically read theory from one source, look for code samples elsewhere, and solve practice questions on separate portals with zero unified feedback.", "Fragmented Learning Tools: ")

    add_h2("1.2 Objectives of Project")
    add_p("The primary objective of DS Studio is to create a unified, self-contained educational desktop software developed purely in Java that bridges the gap between theoretical data structure concepts and real programming execution.")
    add_p("The key objectives are summarized below:")
    add_bullet("To implement authentic linear data structures (Arrays, Stacks, Queues, Circular Queues, Singly, Doubly, and Circular Linked Lists) using pure object-oriented Java.", "Visual Demonstration: ")
    add_bullet("To capture granular state snapshots during every algorithmic operation and animate them step by step with interactive playback controls (Play, Pause, Step Next, Step Prev, Speed Control).", "Execution-Trace Engine: ")
    add_bullet("To display side-by-side standard Java code that highlights the active instruction matching the visual diagram state in real time.", "Synchronized Code Lab: ")
    add_bullet("To offer interactive problem-solving modes through 'DS Quest', covering Concept Quests, Output Prediction, Debug Quests, Dry Runs, and Big-O Complexity challenges.", "Gamified Knowledge Testing: ")
    add_bullet("To integrate an offline SQLite persistence database that tracks learning progress, user XP points, badges, daily streaks, and examination quiz attempts locally.", "Offline Academic Tracking: ")

    doc.add_page_break()

    # ==================== 2. SYSTEM STUDY ====================
    add_h1("2. System Study")
    add_p("Before designing DS Studio, an in-depth study of conventional computer science learning workflows and existing educational software was conducted.")

    add_h2("2.1 Existing System")
    add_p("In the existing academic system, students typically learn Data Structures through:")
    add_bullet("Traditional textbooks and lecture notes containing static diagrams and mathematical recurrence relations.", "Printed Media: ")
    add_bullet("Standard web-based coding portals (e.g., LeetCode, GeeksforGeeks, HackerRank) that test final code outputs against test cases but offer no internal visual tracing.", "Online Judges: ")
    add_bullet("Isolated browser applets or third-party web animations that demonstrate simple diagrams but use JavaScript, do not show real Java code, and require an active internet connection.", "Web Animation Demos: ")

    add_h2("2.2 Disadvantages of Existing System")
    add_p("The existing learning approaches suffer from several practical disadvantages:")
    add_bullet("Static textbook diagrams cannot demonstrate the live movement of pointers (such as HEAD, TAIL, TOP, FRONT, REAR).", "Static Nature: ")
    add_bullet("Commercial online coding judges are output-oriented. If an algorithm fails an edge case, the student cannot see which pointer broke or where memory was lost.", "No Internal Insight: ")
    add_bullet("Web tools require continuous internet access and frequently change or deprecate due to third-party dependencies.", "Internet Dependency: ")
    add_bullet("Existing tools do not connect the visual diagram directly to standard Java syntax, leaving students confused during lab exams and viva voce questions.", "Disconnection from Code: ")
    add_bullet("No integrated tracking exists to record student practice attempts, topic-wise accuracy, and badge achievements on the local desktop machine.", "Absence of Progress Persistence: ")

    add_h2("2.3 Proposed System")
    add_p("The proposed system, DS Studio, provides an integrated desktop environment engineered specifically for MCA and Computer Science curricula. It operates completely offline and is developed natively in Java.")
    add_p("Key advantages of the proposed system include:")
    add_bullet("The core algorithms execute genuine data structure logic and generate immutable state snapshot traces rather than simulated visual effects.", "Genuine Algorithmic Execution: ")
    add_bullet("The user interface and visualizer engine are completely decoupled from the data structure logic, maintaining strict object-oriented design principles.", "Decoupled Architecture: ")
    add_bullet("Students can input custom values and custom array indices to observe how boundaries and overflow conditions behave dynamically.", "Interactive User Inputs: ")
    add_bullet("Integrated modules include the Visualizer Lab, Java Code Lab, DS Quest, Practice Centre, Examination Quiz Centre, and Complexity Guide.", "Comprehensive All-in-One Studio: ")
    add_bullet("Zero internet dependency is achieved through a local SQLite database that stores user profile data, badges, XP, and detailed quiz logs in the student's home directory.", "Local Offline Storage: ")

    doc.add_page_break()

    # ==================== 3. SYSTEM DESIGN ====================
    add_h1("3. System Design (Flow chart & Diagrams)")
    add_p("The system design of DS Studio adheres to modern software architecture standards, focusing on high cohesion, loose coupling, and clean separation between algorithm execution, user interface rendering, and data persistence.")

    add_h2("3.1 System Architecture Diagram")
    add_p("DS Studio employs an Execution-Trace Snapshot Design Pattern. The application is organized into four interconnected layers, as depicted in Figure 3.1 below:")
    add_bullet("Presentation Layer (JavaFX 21 Modular Client): Comprises the 13 navigation screens, animated canvas container, and synchronized code view.", "Layer 1: ")
    add_bullet("Execution-Trace Snapshot Engine: Generates immutable OperationTrace and TraceStep objects containing pointer positions, highlighted indices, and active lines of code.", "Layer 2: ")
    add_bullet("Core Algorithmic Layer: Pure Java classes (Array, Stack, Queue, Circular Queue, Linked Lists, Sorts) operating independently with zero UI dependencies.", "Layer 3: ")
    add_bullet("Persistence Layer: Offline SQLite database caching student progress, XP, badges, and quiz attempts locally.", "Layer 4: ")

    add_image_figure(r"d:\ads\diagrams\staruml_architecture.png", "Figure 3.1: DS Studio Component & Package Architecture (StarUML Style)", width=Inches(6.2))

    add_h2("3.2 Operational System Flowchart")
    add_p("Figure 3.2 illustrates the end-to-end operational activity flowchart of DS Studio from application startup to algorithm execution, snapshot rendering, and progress persistence.")

    add_image_figure(r"d:\ads\diagrams\staruml_flowchart.png", "Figure 3.2: DS Studio Operational Activity Flowchart (StarUML Style)", width=Inches(4.8))

    add_h2("3.3 Use Case Diagram")
    add_p("Figure 3.3 presents the formal Use Case Diagram representing the interactions between the primary actor (Student / MCA Learner) and the core capabilities provided by the DS Studio desktop platform.")

    add_image_figure(r"d:\ads\diagrams\staruml_usecase.png", "Figure 3.3: DS Studio Use Case Diagram (StarUML Style)", width=Inches(6.0))

    add_h2("3.4 Database Design & Entity-Relationship (E-R) Diagram")
    add_p("The application utilizes an embedded SQLite relational database (ds_studio.db) stored locally in the student's home directory. Figure 3.4 illustrates the Entity-Relationship (E-R) diagram connecting the PROFILE, QUIZ_ATTEMPTS, ACHIEVEMENTS, and LESSON_PROGRESS entities.")

    add_image_figure(r"d:\ads\diagrams\staruml_erd.png", "Figure 3.4: SQLite Database Entity-Relationship Schema (StarUML Style)", width=Inches(6.2))

    doc.add_page_break()

    # ==================== 4. SCREEN LAYOUT / SCREEN SHOTS ====================
    add_h1("4. Screen Layout / Screen shots")
    add_p("DS Studio features an ergonomic, dark-themed user interface organized into 13 primary sections accessible via the permanent left-side navigation sidebar.")

    add_h2("4.1 Dashboard View")
    add_p("The Dashboard serves as the central mission control for the student. It includes:")
    add_bullet("A welcoming greeting card displaying the student's name, motivational message, and a quick launch button for the Visualizer Lab.", "Welcome Hero Card: ")
    add_bullet("Four responsive cards highlighting Total XP Earned, Daily Streak (in days), Active Linear Structure Modules (7 modules), and Available Algorithms (5 sorts/searches).", "Real-Time Metric Cards: ")
    add_bullet("A featured problem that rotates daily to encourage regular practice, rewarding immediate XP.", "Daily Challenge Card: ")
    add_bullet("One-click shortcut chips enabling instant access to specific structure visualizers.", "Quick Visualizer Launchpad: ")

    add_h2("4.2 Interactive Visualizer Lab View")
    add_p("The Visualizer Lab is the flagship educational module of DS Studio. It consists of:")
    add_bullet("Data structure dropdown, operation selector, value input field, index input field, and an 'Execute' button.", "Input & Operation Toolbar: ")
    add_bullet("Play/Pause toggle button, Step Next, Step Prev, Reset, and a playback speed selector (0.5x, 1.0x, 2.0x).", "Playback Animation Controls: ")
    add_bullet("A centered interactive display area that dynamically renders array blocks, stack beaker columns, queue slots, or linked list nodes with pointer arrows (HEAD, TAIL, TOP, FRONT, REAR).", "Visualizer Canvas: ")
    add_bullet("Active time complexity badge, auxiliary space complexity badge, step counter (e.g., Step 3 / 8), and detailed descriptive explanation.", "Step Status & Metadata: ")
    add_bullet("A synchronized side panel displaying verified Java implementation code with line tracking.", "Java Code Panel: ")

    add_h2("4.3 Java Code Lab View")
    add_p("The Code Lab provides clean, verified Java implementations for academic exams and lab assignments. It features:")
    add_bullet("Module dropdown covering Array shifts, Linear/Binary Search, Stack LIFO, Queue Modulo math, and Singly/Doubly Linked Lists.", "Module Selection: ")
    add_bullet("A stylized code window with title bar and syntax formatting.", "Formatted Code Viewer: ")
    add_bullet("A dedicated button that writes the complete Java source code to the operating system clipboard with instant notification feedback.", "One-Click Copy Integration: ")

    add_h2("4.4 DS Quest View")
    add_p("DS Quest transforms conceptual testing into an interactive challenge. Key elements include:")
    add_bullet("Category filtering (Concept Quest, Output Prediction Quest, Debug Quest, Dry Run Quest, Complexity Quest) and topic filtering.", "Quest Filtering: ")
    add_bullet("Curated questions featuring formatted code snippets, multiple-choice radio options, and difficulty badges.", "Challenge Cards: ")
    add_bullet("Immediate visual notification showing XP gained and detailed academic explanations for right and wrong answers.", "Solution & Verification Callout: ")

    add_h2("4.5 Examination Quiz Centre View")
    add_p("The Quiz Centre replicates university and competitive exam conditions:")
    add_bullet("5-minute countdown timer that shifts color from slate/cyan to warning amber and alert red as time expires.", "Dynamic Countdown Timer: ")
    add_bullet("Interactive numbered button palette showing question status (Current = Indigo Ring, Answered = Emerald, Marked for Review = Amber, Unanswered = Slate).", "Question Palette Grid: ")
    add_bullet("Summary showing total score, accuracy percentage, and XP points saved directly to SQLite.", "Performance Results Card: ")

    add_h2("4.6 Practice Centre & Complexity Guide Views")
    add_p("The Practice Centre offers topic-filtered flashcard questions with on-demand solution and logic reveal buttons. The Complexity Guide displays a master matrix table comparing Best, Average, and Worst-case time and space bounds across all linear structures.")

    doc.add_page_break()

    # ==================== 5. CONCLUSION ====================
    add_h1("5. Conclusion")
    add_p("DS Studio successfully achieves its foundational vision of delivering an interactive, visually rich, and pedagogically sound desktop learning studio for Advanced Data Structures.")
    add_p("By strictly implementing the entire platform in pure Java and JavaFX, the project demonstrates that complex computer science concepts can be taught effectively without relying on third-party web frameworks, external internet connections, or cloud backends. The application decouples pure algorithmic logic from presentation rendering through the Execution-Trace Snapshot pattern, ensuring that animations reflect actual memory state mutations rather than artificial graphics.")
    add_p("The combination of visual animations, synchronized real Java source code, gamified DS Quest challenges, timed examination simulations, and local SQLite data persistence creates an engaging learning experience suitable for university students preparing for theory examinations, practical programming laboratories, and technical viva voce evaluations.")

    doc.add_page_break()

    # ==================== 6. FUTURE ENHANCEMENTS ====================
    add_h1("6. Future Enhancements")
    add_p("While DS Studio completely covers foundational Linear Data Structures and elementary Sorting Algorithms, several valuable enhancements are planned for future versions:")
    add_bullet("Extending the visualization engine to support Binary Search Trees (BST), AVL Trees, Red-Black Trees, and B-Trees with dynamic balance rotations.", "1. Non-Linear Tree Visualizations: ")
    add_bullet("Adding visual graph models supporting Breadth-First Search (BFS), Depth-First Search (DFS), Dijkstra's Shortest Path, and Minimum Spanning Trees (Prim's and Kruskal's algorithms).", "2. Graph Algorithms & Traversals: ")
    add_bullet("Implementing visual divide-and-conquer animations for Merge Sort and Quick Sort, including recursive call-stack visualizations.", "3. Advanced Sorting Visualizations: ")
    add_bullet("Embedding an in-app Java compiler (using javax.tools.JavaCompiler) so students can write their own methods and visualize their custom algorithms inside the canvas.", "4. Interactive User Code Playground: ")
    add_bullet("Introducing automated PDF performance report generation so students can export their quiz results and progress certificates for academic lab records.", "5. Academic Report Export: ")

    doc.add_page_break()

    # ==================== 7. REFERENCES ====================
    add_h1("7. References")
    add_p("1. Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2022). Introduction to Algorithms (4th ed.). MIT Press.")
    add_p("2. Sedgewick, R., & Wayne, K. (2011). Algorithms (4th ed.). Addison-Wesley Professional.")
    add_p("3. Bloch, J. (2018). Effective Java (3rd ed.). Addison-Wesley Professional.")
    add_p("4. Oracle Corporation. (2024). Java Platform, Standard Edition & JavaFX 21 Documentation. Oracle Technology Network.")
    add_p("5. Horstmann, C. S. (2022). Core Java Volume I – Fundamentals (12th ed.). Prentice Hall.")
    add_p("6. SQLite Development Team. (2024). SQLite Database Engine Architecture & JDBC Driver Documentation. https://www.sqlite.org")

    output_path = r"d:\ads\DS_Studio_StarUML_Project_Documentation.docx"
    doc.save(output_path)
    print(f"Document with all StarUML diagrams successfully created at {output_path}")

    # Also try to update other files if not locked
    for alt_path in [r"d:\ads\DS_Studio_MCA_Project_Documentation.docx", r"d:\ads\DS_Studio_Project_Documentation.docx"]:
        try:
            doc.save(alt_path)
            print(f"Also updated {alt_path}")
        except Exception:
            pass

if __name__ == "__main__":
    create_document()
