import os
import matplotlib.pyplot as plt
import matplotlib.patches as patches
import matplotlib.lines as lines

os.makedirs(r"d:\ads\diagrams", exist_ok=True)

# Helper to draw StarUML subtle background grid
def setup_staruml_canvas(fig, ax, xlim, ylim, title=""):
    ax.set_xlim(0, xlim)
    ax.set_ylim(0, ylim)
    ax.set_facecolor('#FFFFFF')
    fig.patch.set_facecolor('#FFFFFF')
    
    # Draw subtle StarUML grid dots/lines
    for x in range(0, int(xlim) + 1):
        ax.axvline(x, color='#F0F2F5', lw=0.5, zorder=0)
    for y in range(0, int(ylim) + 1):
        ax.axhline(y, color='#F0F2F5', lw=0.5, zorder=0)
        
    ax.axis('off')
    if title:
        # StarUML diagram title tab at top-left
        tab = patches.Polygon([[0.4, ylim - 0.2], [4.5, ylim - 0.2], [4.7, ylim - 0.65], [0.4, ylim - 0.65]],
                              ec='#B0BEC5', fc='#ECEFF1', lw=1.2, zorder=1)
        ax.add_patch(tab)
        ax.text(0.6, ylim - 0.45, title, fontsize=9.5, fontweight='bold', fontfamily='sans-serif', color='#37474F')

# =========================================================================
# 1. STARUML ARCHITECTURE / COMPONENT & PACKAGE DIAGRAM
# =========================================================================
fig, ax = plt.subplots(figsize=(11, 7.5), dpi=300)
setup_staruml_canvas(fig, ax, 12, 9, "pkg [Package] DS_Studio_Architecture")

def draw_staruml_package(x, y, w, h, name, stereotype="<<subsystem>>"):
    # Package tab
    tab_w = min(2.4, w * 0.45)
    tab_h = 0.35
    tab = patches.Rectangle((x, y + h), tab_w, tab_h, ec='#37474F', fc='#FFF9C4', lw=1.2, zorder=2)
    ax.add_patch(tab)
    ax.text(x + 0.1, y + h + 0.12, stereotype, fontsize=7, fontfamily='sans-serif', color='#5D4037')
    
    # Main package box
    body = patches.Rectangle((x, y), w, h, ec='#37474F', fc='#FFFDE7', lw=1.2, zorder=2)
    ax.add_patch(body)
    ax.text(x + w/2, y + h - 0.3, name, ha='center', va='center', fontsize=9.5, fontweight='bold', fontfamily='sans-serif', color='#263238')
    ax.plot([x, x + w], [y + h - 0.55, y + h - 0.55], color='#B0BEC5', lw=0.8, zorder=2)

def draw_staruml_component(x, y, w, h, name, stereotype="<<component>>"):
    box = patches.Rectangle((x, y), w, h, ec='#263238', fc='#E1F5FE', lw=1.2, zorder=3)
    ax.add_patch(box)
    # Component icon in top right
    ico_w, ico_h = 0.32, 0.24
    ico = patches.Rectangle((x + w - ico_w - 0.08, y + h - ico_h - 0.08), ico_w, ico_h, ec='#263238', fc='#FFFFFF', lw=0.8, zorder=4)
    ax.add_patch(ico)
    ax.add_patch(patches.Rectangle((x + w - ico_w - 0.12, y + h - 0.15), 0.08, 0.05, ec='#263238', fc='#263238', zorder=5))
    ax.add_patch(patches.Rectangle((x + w - ico_w - 0.12, y + h - 0.25), 0.08, 0.05, ec='#263238', fc='#263238', zorder=5))
    
    ax.text(x + w/2, y + h - 0.25, stereotype, ha='center', va='center', fontsize=7, fontfamily='sans-serif', color='#0277BD')
    ax.text(x + w/2, y + 0.25, name, ha='center', va='center', fontsize=8.5, fontweight='bold', fontfamily='sans-serif', color='#01579B')

# 4 Main Packages
# Layer 1: Presentation Layer
draw_staruml_package(0.8, 5.8, 10.4, 2.2, "ui :: Presentation Layer (JavaFX 21)", "<<subsystem>>")
draw_staruml_component(1.1, 6.0, 2.2, 1.2, "DashboardView\n& HeaderBar")
draw_staruml_component(3.6, 6.0, 2.3, 1.2, "VisualizerLabView\n(Canvas & State)")
draw_staruml_component(6.2, 6.0, 2.3, 1.2, "CodeLabView &\nAlgorithmsView")
draw_staruml_component(8.8, 6.0, 2.1, 1.2, "QuizCenterView\n& DSQuestView")

# Layer 2: Execution Trace Engine
draw_staruml_package(1.8, 3.6, 8.4, 1.5, "trace :: Execution-Trace Snapshot Engine", "<<framework>>")
draw_staruml_component(2.5, 3.8, 3.2, 0.85, "OperationTrace\n<<trace recorder>>")
draw_staruml_component(6.3, 3.8, 3.2, 0.85, "TraceStep\n<<immutable snapshot>>")

# Layer 3: Core Algorithmic Layer
draw_staruml_package(0.8, 1.2, 6.8, 1.7, "core.structures :: Core ADS Implementation", "<<subsystem>>")
comp_list = [
    ("ArrayStructure", 1.1, 1.9),
    ("StackStructure", 2.7, 1.9),
    ("QueueStructure", 4.3, 1.9),
    ("LinkedLists", 5.9, 1.4),
    ("CircularQueue", 1.1, 1.35),
    ("SortingAlgorithms", 3.2, 1.35)
]
for cname, cx, cy in comp_list:
    box = patches.Rectangle((cx, cy), 1.4, 0.45, ec='#37474F', fc='#EDE7F6', lw=1.0, zorder=3)
    ax.add_patch(box)
    ax.text(cx + 0.7, cy + 0.22, cname, ha='center', va='center', fontsize=7.5, fontweight='bold', fontfamily='sans-serif', color='#4A148C')

# Layer 4: Persistence Layer
draw_staruml_package(8.2, 1.2, 3.0, 1.7, "db :: Persistence", "<<database>>")
draw_staruml_component(8.5, 1.45, 2.4, 0.95, "DatabaseManager\n(SQLite JDBC)")

# StarUML Standard Dependency / Interface Connectors (Dashed arrows with <<use>>)
# Presentation -> Trace
ax.annotate('', xy=(6.0, 5.1), xytext=(6.0, 5.8),
            arrowprops=dict(facecolor='#37474F', edgecolor='#37474F', arrowstyle="->", linestyle='dashed', lw=1.5))
ax.text(6.15, 5.45, "<<use>>\nrequests trace", fontsize=7.5, fontfamily='sans-serif', color='#37474F')

# Trace -> Core Structures
ax.annotate('', xy=(4.2, 2.9), xytext=(4.2, 3.6),
            arrowprops=dict(facecolor='#37474F', edgecolor='#37474F', arrowstyle="->", linestyle='dashed', lw=1.5))
ax.text(4.35, 3.25, "<<call>>\nexecutes algorithm", fontsize=7.5, fontfamily='sans-serif', color='#37474F')

# Presentation -> DB
ax.annotate('', xy=(9.7, 2.9), xytext=(9.7, 5.8),
            arrowprops=dict(facecolor='#37474F', edgecolor='#37474F', arrowstyle="->", linestyle='dashed', lw=1.5))
ax.text(9.85, 4.35, "<<record>>\nXP & quiz history", fontsize=7.5, fontfamily='sans-serif', color='#37474F')

plt.tight_layout()
plt.savefig(r"d:\ads\diagrams\staruml_architecture.png", bbox_inches='tight', dpi=300)
plt.close()
print("staruml_architecture.png generated")

# =========================================================================
# 2. STARUML UML ACTIVITY / FLOWCHART DIAGRAM
# =========================================================================
fig, ax = plt.subplots(figsize=(8.5, 11), dpi=300)
setup_staruml_canvas(fig, ax, 10, 13, "act [Activity] Operational_Workflow")

def draw_activity_state(x, y, w, h, text):
    box = patches.FancyBboxPatch((x, y), w, h, boxstyle="round,pad=0.2", ec='#37474F', fc='#FFFDE7', lw=1.3, zorder=3)
    ax.add_patch(box)
    ax.text(x + w/2, y + h/2, text, ha='center', va='center', fontsize=8.5, fontfamily='sans-serif', color='#212121')

def draw_flow_arrow(x1, y1, x2, y2, label=""):
    ax.annotate('', xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(facecolor='#263238', edgecolor='#263238', arrowstyle="-|>", lw=1.3))
    if label:
        ax.text((x1 + x2)/2 + 0.15, (y1 + y2)/2, label, fontsize=8, fontfamily='sans-serif', fontweight='bold', color='#1565C0')

# Initial Node (Solid black circle)
init_circle = patches.Circle((5.0, 11.8), 0.18, ec='#000000', fc='#000000', zorder=4)
ax.add_patch(init_circle)
ax.text(5.0, 12.15, "[Initial Node]", ha='center', va='center', fontsize=7.5, fontfamily='sans-serif', color='#757575')

draw_flow_arrow(5.0, 11.6, 5.0, 11.0)

# Action 1: Select Module
draw_activity_state(3.2, 10.3, 3.6, 0.7, "Select Data Structure & Operation\n(e.g., Circular Queue, Modulo Enqueue)")
draw_flow_arrow(5.0, 10.3, 5.0, 9.6)

# Action 2: Enter Input
draw_activity_state(3.3, 8.9, 3.4, 0.7, "Enter Value & Target Index\nClick 'Execute' Button")
draw_flow_arrow(5.0, 8.9, 5.0, 8.2)

# Decision Diamond
diamond = patches.Polygon([[5.0, 8.2], [6.2, 7.7], [5.0, 7.2], [3.8, 7.7]], ec='#37474F', fc='#FFF9C4', lw=1.3, zorder=3)
ax.add_patch(diamond)
ax.text(5.0, 7.7, "Valid Bounds\n& Format?", ha='center', va='center', fontsize=7.5, fontweight='bold', fontfamily='sans-serif', color='#263238')

# Branch [Invalid] -> Loopback
ax.plot([6.2, 7.8, 7.8, 5.0], [7.7, 7.7, 9.25, 9.25], color='#C62828', lw=1.3, linestyle='-', zorder=2)
ax.plot([5.0], [9.25], marker='<', color='#C62828', markersize=6)
ax.text(6.4, 7.85, "[Invalid Input]", fontsize=7.5, fontfamily='sans-serif', fontweight='bold', color='#C62828')
ax.text(7.9, 8.5, "Show Error Badge\nPrompt Re-entry", fontsize=7.5, fontfamily='sans-serif', color='#C62828')

# Branch [Valid] -> Down
draw_flow_arrow(5.0, 7.2, 5.0, 6.4, "[Valid]")

# Action 3: Execute Pure Java
draw_activity_state(2.8, 5.6, 4.4, 0.8, "Execute Core Algorithm in Pure Java\n(ArrayStructure / Stack / Queue / LinkedList)")
draw_flow_arrow(5.0, 5.6, 5.0, 4.9)

# Action 4: Record Micro-Step Traces
draw_activity_state(2.9, 4.1, 4.2, 0.8, "Generate Immutable TraceStep List\n(Records pointers, shifted indices, Java code lines)")
draw_flow_arrow(5.0, 4.1, 5.0, 3.4)

# Action 5: Render Canvas & Playback
draw_activity_state(2.6, 2.5, 4.8, 0.9, "Animate Graphical Canvas Nodes & Pointers\nSynchronize Active Line in Code Lab Panel\n(Supports Play, Pause, Step Next, Speed Slider)")
draw_flow_arrow(5.0, 2.5, 5.0, 1.8)

# Action 6: Persist Progress
draw_activity_state(3.1, 1.0, 3.8, 0.8, "Award XP & Update Daily Streak\nSave Attempt Log in Local SQLite DB")
draw_flow_arrow(5.0, 1.0, 5.0, 0.45)

# Final Activity Node (Bullseye: outer circle with solid inner circle)
final_outer = patches.Circle((5.0, 0.25), 0.22, ec='#000000', fc='#FFFFFF', lw=1.5, zorder=4)
final_inner = patches.Circle((5.0, 0.25), 0.14, ec='#000000', fc='#000000', zorder=5)
ax.add_patch(final_outer)
ax.add_patch(final_inner)
ax.text(5.0, -0.05, "[Activity Final]", ha='center', va='center', fontsize=7.5, fontfamily='sans-serif', color='#757575')

plt.tight_layout()
plt.savefig(r"d:\ads\diagrams\staruml_flowchart.png", bbox_inches='tight', dpi=300)
plt.close()
print("staruml_flowchart.png generated")

# =========================================================================
# 3. STARUML USE CASE DIAGRAM
# =========================================================================
fig, ax = plt.subplots(figsize=(10.5, 7.5), dpi=300)
setup_staruml_canvas(fig, ax, 12, 9, "uc [UseCase] DS_Studio_UseCases")

# System Boundary Rectangle
boundary = patches.Rectangle((3.5, 0.6), 8.0, 7.8, ec='#37474F', fc='#FAFAFA', lw=1.5, zorder=1)
ax.add_patch(boundary)
ax.text(3.7, 8.1, "DS Studio System", fontsize=10.5, fontweight='bold', fontfamily='sans-serif', color='#263238')

# StarUML Human Actor (Clean vector stickman)
def draw_staruml_actor(x, y, name):
    # Head
    head = patches.Circle((x, y + 0.65), 0.22, ec='#263238', fc='#FFFDE7', lw=1.5, zorder=3)
    ax.add_patch(head)
    # Torso
    ax.plot([x, x], [y + 0.43, y - 0.25], color='#263238', lw=1.8, zorder=3)
    # Arms
    ax.plot([x - 0.35, x + 0.35], [y + 0.2, y + 0.2], color='#263238', lw=1.8, zorder=3)
    # Left Leg
    ax.plot([x, x - 0.25], [y - 0.25, y - 0.85], color='#263238', lw=1.8, zorder=3)
    # Right Leg
    ax.plot([x, x + 0.25], [y - 0.25, y - 0.85], color='#263238', lw=1.8, zorder=3)
    # Label
    ax.text(x, y - 1.15, name, ha='center', va='center', fontsize=9, fontweight='bold', fontfamily='sans-serif', color='#263238')

draw_staruml_actor(1.8, 4.8, "Student /\nMCA Learner")

# Use Cases (StarUML signature light yellow/cream rounded ellipses)
usecases = [
    ("Browse DS Curriculum & Formal Definitions", 5.2, 7.5),
    ("Execute Data Structure Operations Interactively", 5.2, 6.4),
    ("Step-by-Step Execution Trace Playback", 5.2, 5.3),
    ("Inspect Real Java Implementation Code", 5.2, 4.2),
    ("Solve DS Quest Categorized Challenges", 5.2, 3.1),
    ("Attempt Timed Examination Quizzes", 5.2, 2.0),
    ("View Learning Analytics & Unlocked Badges", 5.2, 0.9)
]

for title, ux, uy in usecases:
    # Use Case Oval
    oval = patches.FancyBboxPatch((ux, uy), 5.6, 0.72, boxstyle="round,pad=0.35", ec='#37474F', fc='#FFFDE7', lw=1.2, zorder=3)
    ax.add_patch(oval)
    ax.text(ux + 2.8, uy + 0.36, title, ha='center', va='center', fontsize=8.5, fontfamily='sans-serif', color='#212121')
    
    # Association line (Solid line from actor torso to use case boundary)
    ax.plot([2.15, ux], [5.0, uy + 0.36], color='#37474F', lw=1.2, zorder=2)

plt.tight_layout()
plt.savefig(r"d:\ads\diagrams\staruml_usecase.png", bbox_inches='tight', dpi=300)
plt.close()
print("staruml_usecase.png generated")

# =========================================================================
# 4. STARUML CLASS & ERD RELATIONAL SCHEMA DIAGRAM
# =========================================================================
fig, ax = plt.subplots(figsize=(11, 7.5), dpi=300)
setup_staruml_canvas(fig, ax, 12, 9, "erd [EntityRelationship] SQLite_Schema")

def draw_staruml_entity(x, y, w, title, pk_list, col_list):
    total_rows = len(pk_list) + len(col_list)
    header_h = 0.55
    row_h = 0.32
    body_h = (total_rows * row_h) + 0.3
    
    # Outer box
    box = patches.Rectangle((x, y), w, header_h + body_h, ec='#37474F', fc='#FFFFFF', lw=1.4, zorder=2)
    ax.add_patch(box)
    
    # Header box (StarUML ERD header color)
    hdr = patches.Rectangle((x, y + body_h), w, header_h, ec='#37474F', fc='#FFE082', lw=1.4, zorder=3)
    ax.add_patch(hdr)
    ax.text(x + w/2, y + body_h + 0.37, "<<entity>>", ha='center', va='center', fontsize=7, fontfamily='sans-serif', color='#5D4037')
    ax.text(x + w/2, y + body_h + 0.16, title, ha='center', va='center', fontsize=9.5, fontweight='bold', fontfamily='sans-serif', color='#263238')
    
    curr_y = y + body_h - 0.22
    # PK section
    for col_name, col_type in pk_list:
        ax.text(x + 0.2, curr_y, "PK", fontsize=7.5, fontweight='bold', fontfamily='sans-serif', color='#D84315')
        ax.text(x + 0.7, curr_y, col_name, fontsize=8, fontweight='bold', fontfamily='sans-serif', color='#212121')
        ax.text(x + w - 0.2, curr_y, col_type, ha='right', fontsize=8, fontfamily='monospace', color='#455A64')
        curr_y -= row_h
        
    # Divider between PK and attributes
    ax.plot([x, x + w], [curr_y + 0.08, curr_y + 0.08], color='#CFD8DC', lw=1.0, zorder=3)
    curr_y -= 0.06
    
    # Regular columns
    for col_name, col_type in col_list:
        ax.text(x + 0.7, curr_y, col_name, fontsize=8, fontfamily='sans-serif', color='#37474F')
        ax.text(x + w - 0.2, curr_y, col_type, ha='right', fontsize=8, fontfamily='monospace', color='#546E7A')
        curr_y -= row_h

# Draw the 4 Entities
# 1. PROFILE
draw_staruml_entity(0.8, 4.3, 3.4, "PROFILE",
                    [("id", "INTEGER")],
                    [("username", "TEXT"),
                     ("xp", "INTEGER"),
                     ("level", "INTEGER"),
                     ("streak", "INTEGER"),
                     ("last_active_date", "TEXT"),
                     ("theme", "TEXT")])

# 2. QUIZ_ATTEMPTS
draw_staruml_entity(7.8, 3.8, 3.6, "QUIZ_ATTEMPTS",
                    [("id", "INTEGER (AUTO)")],
                    [("topic", "TEXT"),
                     ("difficulty", "TEXT"),
                     ("score", "INTEGER"),
                     ("total_questions", "INTEGER"),
                     ("accuracy", "REAL"),
                     ("xp_earned", "INTEGER"),
                     ("attempt_timestamp", "DATETIME")])

# 3. ACHIEVEMENTS
draw_staruml_entity(4.2, 0.7, 3.6, "ACHIEVEMENTS",
                    [("code", "TEXT (PK)")],
                    [("title", "TEXT"),
                     ("description", "TEXT"),
                     ("unlocked", "INTEGER"),
                     ("unlocked_at", "TEXT")])

# 4. LESSON_PROGRESS
draw_staruml_entity(0.8, 0.7, 3.0, "LESSON_PROGRESS",
                    [("topic_id", "TEXT (PK)")],
                    [("completed", "INTEGER"),
                     ("bookmarked", "INTEGER"),
                     ("last_read_timestamp", "DATETIME")])

# Relationship connectors in StarUML style (Crow's foot / 1 to N notation)
# Profile -> Quiz Attempts (1 : N)
ax.plot([4.2, 6.0, 6.0, 7.8], [6.0, 6.0, 6.2, 6.2], color='#37474F', lw=1.3, zorder=1)
# 1 symbol
ax.plot([4.4, 4.4], [5.85, 6.15], color='#37474F', lw=1.3)
# N (crow's foot) at 7.8
ax.plot([7.6, 7.8], [6.05, 6.2], color='#37474F', lw=1.3)
ax.plot([7.6, 7.8], [6.35, 6.2], color='#37474F', lw=1.3)
ax.text(6.0, 6.45, "1 : N (records attempts)", ha='center', va='center', fontsize=7.5, fontfamily='sans-serif', color='#455A64')

# Profile -> Achievements (1 : N)
ax.plot([2.5, 2.5, 4.2], [4.3, 2.5, 2.5], color='#37474F', lw=1.3, zorder=1)
ax.plot([4.0, 4.2], [2.65, 2.5], color='#37474F', lw=1.3)
ax.plot([4.0, 4.2], [2.35, 2.5], color='#37474F', lw=1.3)
ax.text(3.35, 2.7, "1 : N (unlocks)", ha='center', va='center', fontsize=7.5, fontfamily='sans-serif', color='#455A64')

# Profile -> Lesson Progress (1 : N)
ax.plot([1.8, 1.8], [4.3, 3.2], color='#37474F', lw=1.3, zorder=1)
ax.plot([1.65, 1.8], [3.35, 3.2], color='#37474F', lw=1.3)
ax.plot([1.95, 1.8], [3.35, 3.2], color='#37474F', lw=1.3)
ax.text(1.9, 3.75, "1 : N (tracks)", ha='left', va='center', fontsize=7.5, fontfamily='sans-serif', color='#455A64')

plt.tight_layout()
plt.savefig(r"d:\ads\diagrams\staruml_erd.png", bbox_inches='tight', dpi=300)
plt.close()
print("staruml_erd.png generated")
