import os
import matplotlib.pyplot as plt
import matplotlib.patches as patches

os.makedirs(r"d:\ads\diagrams", exist_ok=True)

# -------------------------------------------------------------------------
# 1. System Architecture Diagram
# -------------------------------------------------------------------------
fig, ax = plt.subplots(figsize=(10, 6.5), dpi=300)
ax.set_xlim(0, 10)
ax.set_ylim(0, 8.5)
ax.axis('off')

# Title
ax.text(5, 8.1, "Figure 3.1: DS Studio Layered System Architecture", ha='center', va='center', 
        fontsize=14, fontweight='bold', fontfamily='serif')

# Layer 1: Presentation Layer
rect1 = patches.FancyBboxPatch((0.8, 5.8), 8.4, 1.8, boxstyle="round,pad=0.2", 
                               ec="#1E40AF", fc="#DBEAFE", lw=2)
ax.add_patch(rect1)
ax.text(1.2, 7.3, "1. Presentation UI Layer (JavaFX 21 Modular Client)", fontsize=11, fontweight='bold', fontfamily='serif', color="#1E3A8A")
boxes_l1 = [
    ("Dashboard &\nNavigation", 1.2),
    ("Visualizer Canvas\n(Nodes, Pointers)", 3.4),
    ("Synchronized\nJava Code Lab", 5.6),
    ("DS Quest &\nQuiz Centre", 7.8)
]
for title, x in boxes_l1:
    b = patches.FancyBboxPatch((x, 6.0), 1.8, 1.0, boxstyle="round,pad=0.1", ec="#3B82F6", fc="#EFF6FF", lw=1.5)
    ax.add_patch(b)
    ax.text(x + 0.9, 6.5, title, ha='center', va='center', fontsize=8.5, fontfamily='serif', fontweight='bold', color="#1E293B")

# Arrow Layer 1 -> Layer 2
ax.annotate('', xy=(5, 5.1), xytext=(5, 5.7),
            arrowprops=dict(facecolor='#1E40AF', edgecolor='#1E40AF', arrowstyle="<|-|>", lw=2))
ax.text(5.2, 5.4, "Reads Immutable Snapshots & Sends User Action", fontsize=8.5, fontfamily='serif', fontstyle='italic', color="#334155")

# Layer 2: Execution-Trace Engine
rect2 = patches.FancyBboxPatch((1.5, 3.8), 7.0, 1.2, boxstyle="round,pad=0.2", 
                               ec="#065F46", fc="#D1FAE5", lw=2)
ax.add_patch(rect2)
ax.text(5, 4.6, "2. Execution-Trace Snapshot Engine", ha='center', va='center', 
        fontsize=11, fontweight='bold', fontfamily='serif', color="#064E3B")
ax.text(5, 4.15, "OperationTrace  •  TraceStep (Micro-steps, Pointers, Active Code Line, O(N) Bounds)", 
        ha='center', va='center', fontsize=8.5, fontfamily='serif', color="#047857")

# Arrow Layer 2 -> Layer 3
ax.annotate('', xy=(5, 3.1), xytext=(5, 3.7),
            arrowprops=dict(facecolor='#065F46', edgecolor='#065F46', arrowstyle="<|-|>", lw=2))
ax.text(5.2, 3.4, "Executes Algorithm & Dispatches State Snapshots", fontsize=8.5, fontfamily='serif', fontstyle='italic', color="#334155")

# Layer 3: Pure Core Data Structures
rect3 = patches.FancyBboxPatch((0.8, 1.7), 8.4, 1.3, boxstyle="round,pad=0.2", 
                               ec="#7C3AED", fc="#EDE9FE", lw=2)
ax.add_patch(rect3)
ax.text(1.2, 2.7, "3. Core Algorithmic Layer (Pure Java - Zero UI Dependency)", fontsize=10.5, fontweight='bold', fontfamily='serif', color="#5B21B6")
structs = ["Array", "Stack", "Queue", "Circular Queue", "Singly List", "Doubly List", "Circular List", "Sorts"]
for i, s in enumerate(structs):
    x_pos = 1.0 + (i * 1.05)
    sb = patches.FancyBboxPatch((x_pos, 1.9), 0.95, 0.6, boxstyle="round,pad=0.05", ec="#8B5CF6", fc="#F5F3FF", lw=1.2)
    ax.add_patch(sb)
    ax.text(x_pos + 0.47, 2.2, s, ha='center', va='center', fontsize=7.5, fontfamily='serif', fontweight='bold', color="#4C1D95")

# Arrow Layer 1 -> Layer 4 (Side / DB)
rect4 = patches.FancyBboxPatch((2.5, 0.2), 5.0, 1.0, boxstyle="round,pad=0.15", 
                               ec="#9A3412", fc="#FFEDD5", lw=2)
ax.add_patch(rect4)
ax.text(5, 0.8, "4. Persistence Layer (SQLite Database: ~/.dsstudio/ds_studio.db)", 
        ha='center', va='center', fontsize=9.5, fontweight='bold', fontfamily='serif', color="#7C2D12")
ax.text(5, 0.45, "User Profile  •  Quiz Attempts History  •  Badges & XP", 
        ha='center', va='center', fontsize=8.5, fontfamily='serif', color="#9A3412")

# Arrow Layer 3 -> Layer 4
ax.annotate('', xy=(5, 1.3), xytext=(5, 1.6),
            arrowprops=dict(facecolor='#9A3412', edgecolor='#9A3412', arrowstyle="<|-|>", lw=1.8))

plt.tight_layout()
plt.savefig(r"d:\ads\diagrams\architecture_diagram.png", bbox_inches='tight', dpi=300)
plt.close()
print("architecture_diagram.png created")

# -------------------------------------------------------------------------
# 2. System Flowchart Diagram
# -------------------------------------------------------------------------
fig, ax = plt.subplots(figsize=(8.5, 11), dpi=300)
ax.set_xlim(0, 10)
ax.set_ylim(0, 13)
ax.axis('off')

ax.text(5, 12.6, "Figure 3.2: DS Studio Operational System Flowchart", ha='center', va='center', 
        fontsize=14, fontweight='bold', fontfamily='serif')

def draw_oval(y, text, color="#2563EB", fill="#EFF6FF"):
    b = patches.FancyBboxPatch((3.2, y), 3.6, 0.7, boxstyle="round,pad=0.35", ec=color, fc=fill, lw=2)
    ax.add_patch(b)
    ax.text(5, y + 0.35, text, ha='center', va='center', fontsize=9, fontweight='bold', fontfamily='serif', color="#1E293B")

def draw_rect(y, text, color="#059669", fill="#ECFDF5", width=4.4):
    x = 5 - (width / 2)
    b = patches.FancyBboxPatch((x, y), width, 0.75, boxstyle="round,pad=0.1", ec=color, fc=fill, lw=1.8)
    ax.add_patch(b)
    ax.text(5, y + 0.37, text, ha='center', va='center', fontsize=8.5, fontweight='bold', fontfamily='serif', color="#1E293B")

def draw_diamond(y, text):
    diamond = patches.Polygon([[5, y + 0.8], [7.2, y + 0.4], [5, y], [2.8, y + 0.4]], 
                              ec="#D97706", fc="#FFFBEB", lw=2)
    ax.add_patch(diamond)
    ax.text(5, y + 0.4, text, ha='center', va='center', fontsize=8, fontweight='bold', fontfamily='serif', color="#78350F")

def draw_arrow(y_start, y_end, label=""):
    ax.annotate('', xy=(5, y_end), xytext=(5, y_start),
                arrowprops=dict(facecolor='#475569', edgecolor='#475569', arrowstyle="-|>", lw=1.8))
    if label:
        ax.text(5.2, (y_start + y_end)/2, label, fontsize=8, fontfamily='serif', fontweight='bold', color="#047857")

# Steps
draw_oval(11.5, "START (Launch DS Studio)")
draw_arrow(11.5, 10.9)

draw_rect(10.15, "Select Data Structure & Operation\n(e.g., Circular Queue -> Enqueue)")
draw_arrow(10.15, 9.55)

draw_rect(8.8, "Input Parameters (Value & Index)\nClick 'Execute' Button")
draw_arrow(8.8, 8.2)

draw_diamond(7.2, "Valid Inputs &\nBounds Check?")

# Arrow Yes
draw_arrow(7.2, 6.3, "YES")

# Arrow No (Loopback)
ax.annotate('', xy=(7.2, 9.17), xytext=(7.2, 7.6),
            arrowprops=dict(facecolor='#DC2626', edgecolor='#DC2626', arrowstyle="-|>", lw=1.8))
ax.text(7.35, 8.4, "NO (Show Warning)", fontsize=7.5, fontfamily='serif', fontweight='bold', color="#DC2626")

draw_rect(5.5, "Execute Core Algorithm in Java\n(Boundary check & pointer reconnection)")
draw_arrow(5.5, 4.85)

draw_rect(4.1, "Capture Micro-Steps into\nOperationTrace (TraceStep Snapshots)")
draw_arrow(4.1, 3.45)

draw_rect(2.7, "Render Visualization Canvas & Pointers\nSynchronize Java Code Line Highlighting", width=5.2)
draw_arrow(2.7, 2.05)

draw_rect(1.3, "Playback Controls (Play / Next / Prev)\nUpdate XP & Record Log in SQLite DB", width=5.2)
draw_arrow(1.3, 0.75)

draw_oval(0.05, "END (Ready for Next Operation)")

plt.tight_layout()
plt.savefig(r"d:\ads\diagrams\system_flowchart.png", bbox_inches='tight', dpi=300)
plt.close()
print("system_flowchart.png created")

# -------------------------------------------------------------------------
# 3. Use Case Diagram
# -------------------------------------------------------------------------
fig, ax = plt.subplots(figsize=(9, 6.5), dpi=300)
ax.set_xlim(0, 10)
ax.set_ylim(0, 8)
ax.axis('off')

ax.text(5, 7.6, "Figure 3.3: DS Studio Use Case Diagram", ha='center', va='center', 
        fontsize=14, fontweight='bold', fontfamily='serif')

# System Boundary Box
rect = patches.FancyBboxPatch((2.8, 0.4), 6.8, 6.7, boxstyle="round,pad=0.2", 
                              ec="#334155", fc="#F8FAFC", lw=2)
ax.add_patch(rect)
ax.text(6.2, 6.8, "DS Studio Desktop System Boundary", ha='center', va='center', 
        fontsize=11, fontweight='bold', fontfamily='serif', color="#1E293B")

# Actor: Student / MCA Learner
ax.plot(1.2, 4.3, 'o', ms=20, mfc='#38BDF8', mec='#0284C7', mew=2) # head
ax.plot([1.2, 1.2], [4.0, 3.2], color='#0284C7', lw=3)              # body
ax.plot([0.7, 1.7], [3.7, 3.7], color='#0284C7', lw=3)              # arms
ax.plot([1.2, 0.8], [3.2, 2.3], color='#0284C7', lw=3)              # left leg
ax.plot([1.2, 1.6], [3.2, 2.3], color='#0284C7', lw=3)              # right leg
ax.text(1.2, 1.9, "Student / MCA Learner\n(User Actor)", ha='center', va='center', 
        fontsize=9, fontweight='bold', fontfamily='serif', color="#0F172A")

use_cases = [
    ("Browse Data Structure Curriculum & Theory", 5.9),
    ("Execute Dynamic Visualizer Operations", 5.0),
    ("Step-by-Step Execution Trace Playback", 4.1),
    ("Inspect Real Synchronized Java Code", 3.2),
    ("Solve DS Quest Categorized Challenges", 2.3),
    ("Attempt Timed Examination Quizzes", 1.4),
    ("View Local Learning Analytics & Badges", 0.6)
]

for uc_text, y in use_cases:
    ellipse = patches.FancyBboxPatch((3.5, y - 0.25), 5.4, 0.6, boxstyle="round,pad=0.25", 
                                     ec="#6366F1", fc="#EEF2FF", lw=1.5)
    ax.add_patch(ellipse)
    ax.text(6.2, y + 0.05, uc_text, ha='center', va='center', 
            fontsize=8.5, fontweight='bold', fontfamily='serif', color="#312E81")
    
    # Connecting line from Actor to Use Case
    ax.plot([1.7, 3.5], [3.7, y + 0.05], color='#64748B', linestyle='--', lw=1.2)

plt.tight_layout()
plt.savefig(r"d:\ads\diagrams\usecase_diagram.png", bbox_inches='tight', dpi=300)
plt.close()
print("usecase_diagram.png created")

# -------------------------------------------------------------------------
# 4. Entity-Relationship (E-R) Database Schema Diagram
# -------------------------------------------------------------------------
fig, ax = plt.subplots(figsize=(9.5, 6), dpi=300)
ax.set_xlim(0, 10)
ax.set_ylim(0, 7)
ax.axis('off')

ax.text(5, 6.6, "Figure 3.4: SQLite Database Entity-Relationship (E-R) Diagram", ha='center', va='center', 
        fontsize=14, fontweight='bold', fontfamily='serif')

# Table 1: PROFILE
t1 = patches.FancyBboxPatch((0.5, 2.2), 2.8, 3.6, boxstyle="round,pad=0.1", ec="#1E40AF", fc="#EFF6FF", lw=1.8)
ax.add_patch(t1)
ax.text(1.9, 5.5, "PROFILE (Table)", ha='center', va='center', fontsize=10, fontweight='bold', fontfamily='serif', color="#1E3A8A")
ax.plot([0.5, 3.3], [5.2, 5.2], color="#1E40AF", lw=1.2)
t1_fields = [
    ("PK  id : INTEGER", True),
    ("     username : TEXT", False),
    ("     level : INTEGER", False),
    ("     level_title : TEXT", False),
    ("     xp : INTEGER", False),
    ("     streak : INTEGER", False),
    ("     created_at : TIMESTAMP", False)
]
for idx, (f, pk) in enumerate(t1_fields):
    ax.text(0.7, 4.8 - (idx * 0.4), f, fontsize=8, fontfamily='serif', 
            fontweight='bold' if pk else 'normal', color="#1E293B")

# Table 2: QUIZ_ATTEMPTS
t2 = patches.FancyBboxPatch((6.7, 1.8), 2.8, 4.0, boxstyle="round,pad=0.1", ec="#065F46", fc="#ECFDF5", lw=1.8)
ax.add_patch(t2)
ax.text(8.1, 5.5, "QUIZ_ATTEMPTS (Table)", ha='center', va='center', fontsize=10, fontweight='bold', fontfamily='serif', color="#064E3B")
ax.plot([6.7, 9.5], [5.2, 5.2], color="#065F46", lw=1.2)
t2_fields = [
    ("PK  id : INTEGER", True),
    ("     topic : TEXT", False),
    ("     difficulty : TEXT", False),
    ("     score : INTEGER", False),
    ("     total_questions : INTEGER", False),
    ("     accuracy : REAL", False),
    ("     xp_earned : INTEGER", False),
    ("     attempt_timestamp : TEXT", False)
]
for idx, (f, pk) in enumerate(t2_fields):
    ax.text(6.9, 4.8 - (idx * 0.4), f, fontsize=8, fontfamily='serif', 
            fontweight='bold' if pk else 'normal', color="#1E293B")

# Table 3: ACHIEVEMENTS
t3 = patches.FancyBboxPatch((3.6, 0.4), 2.8, 2.6, boxstyle="round,pad=0.1", ec="#9A3412", fc="#FFF7ED", lw=1.8)
ax.add_patch(t3)
ax.text(5.0, 2.7, "ACHIEVEMENTS (Table)", ha='center', va='center', fontsize=10, fontweight='bold', fontfamily='serif', color="#9A3412")
ax.plot([3.6, 6.4], [2.4, 2.4], color="#9A3412", lw=1.2)
t3_fields = [
    ("PK  id : INTEGER", True),
    ("     badge_key : TEXT", False),
    ("     title : TEXT", False),
    ("     description : TEXT", False),
    ("     unlocked : BOOLEAN", False)
]
for idx, (f, pk) in enumerate(t3_fields):
    ax.text(3.8, 2.05 - (idx * 0.35), f, fontsize=8, fontfamily='serif', 
            fontweight='bold' if pk else 'normal', color="#1E293B")

# Relationships
ax.annotate('', xy=(6.7, 3.8), xytext=(3.3, 3.8),
            arrowprops=dict(facecolor='#475569', edgecolor='#475569', arrowstyle="-|>", lw=1.5, linestyle='--'))
ax.text(5.0, 4.0, "1 : N (User records Quiz Attempts)", ha='center', va='center', fontsize=7.5, fontfamily='serif', color="#334155")

ax.annotate('', xy=(3.6, 1.8), xytext=(2.2, 2.2),
            arrowprops=dict(facecolor='#475569', edgecolor='#475569', arrowstyle="-|>", lw=1.5, linestyle='--'))
ax.text(2.6, 1.6, "1 : N (Earns Badges)", ha='center', va='center', fontsize=7.5, fontfamily='serif', color="#334155")

plt.tight_layout()
plt.savefig(r"d:\ads\diagrams\er_diagram.png", bbox_inches='tight', dpi=300)
plt.close()
print("er_diagram.png created")
